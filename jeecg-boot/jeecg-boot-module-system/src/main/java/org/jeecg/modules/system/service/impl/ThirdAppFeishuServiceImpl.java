package org.jeecg.modules.system.service.impl;

import cn.hutool.core.util.ReflectUtil;
import com.lark.oapi.Client;
import com.lark.oapi.core.Config;
import com.lark.oapi.core.token.GlobalTokenManager;
import com.lark.oapi.core.utils.Lists;
import com.lark.oapi.service.contact.v3.model.Department;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.dto.message.MessageDTO;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.config.thirdapp.ThirdAppConfig;
import org.jeecg.modules.devops.feishu.FeiShuDepartmentTree;
import org.jeecg.modules.devops.feishu.FeiShuUtils;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.mapper.SysUserMapper;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.system.service.IThirdAppService;
import org.jeecg.modules.system.util.FeiShuVoMappingUtil;
import org.jeecg.modules.system.vo.thirdapp.SyncInfoVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;


@Slf4j
@Service
public class ThirdAppFeishuServiceImpl implements IThirdAppService {

    @Autowired
    ThirdAppConfig thirdAppConfig;

    @Autowired
    ISysDepartService sysDepartService;

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysUserServiceImpl sysUserService;

    @Override
    public String getAccessToken() {
        final Client client = FeiShuUtils.getClient();
        try {
            return GlobalTokenManager.getTokenManager().getAppAccessToken((Config) ReflectUtil.getFieldValue(client, "config"));
        } catch (Exception e) {
            throw new JeecgBootException(e);
        }
    }

    @Override
    public SyncInfoVo syncLocalDepartmentToThirdApp(String ids) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SyncInfoVo syncThirdAppDepartmentToLocal(String ids) {
        FeiShuDepartmentTree departmentTree;

        try {
            departmentTree = FeiShuDepartmentTree.getInstance();
        } catch (Exception e) {
            return SyncInfoVo.failInfo(e.getMessage());
        }

        for (final Department department : departmentTree.getAllDepartment()) {
            final SysDepart feiShuDepart = FeiShuVoMappingUtil.departmentToSysDepart(department);
            final SysDepart existedSysDepart = sysDepartService.getById(feiShuDepart.getId());
            if (existedSysDepart != null) {
                BeanUtils.copyProperties(feiShuDepart, existedSysDepart);
                sysDepartService.updateDepartDataById(existedSysDepart, JwtUtil.getUserNameByToken(SpringContextUtils.getHttpServletRequest()));
            } else {
                sysDepartService.saveDepartData(feiShuDepart, JwtUtil.getUserNameByToken(SpringContextUtils.getHttpServletRequest()));
            }
        }

        return SyncInfoVo.successInfo("successful!");
    }

    @Override
    public SyncInfoVo syncLocalUserToThirdApp(String ids) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SyncInfoVo syncThirdAppUserToLocal() {
        FeiShuDepartmentTree departmentTree;

        try {
            departmentTree = FeiShuDepartmentTree.getInstance();
        } catch (Exception e) {
            return SyncInfoVo.failInfo(e.getMessage());
        }

        final Set<FeiShuDepartmentTree.FeiShuUserWrapper> allUsers = departmentTree.getAllUsers();
        for (final FeiShuDepartmentTree.FeiShuUserWrapper userWrapper : allUsers) {
            final SysUser feiShuUser = FeiShuVoMappingUtil.userToSysUser(userWrapper.get());
            final SysUser existedSysUser = userMapper.selectById(feiShuUser.getId());
            if (existedSysUser != null) {
                BeanUtils.copyProperties(feiShuUser, existedSysUser);
                userMapper.updateById(existedSysUser);
            } else {
                userMapper.insert(feiShuUser);
            }
        }

        return SyncInfoVo.successInfo("successful!");
    }

    public SyncInfoVo syncDepartmentUser() {
        FeiShuDepartmentTree departmentTree;

        try {
            departmentTree = FeiShuDepartmentTree.getInstance();
        } catch (Exception e) {
            return SyncInfoVo.failInfo(e.getMessage());
        }

        class InternalFunc implements Function<FeiShuDepartmentTree.DepartmentNode, Map<FeiShuDepartmentTree.FeiShuUserWrapper, List<Department>>> {

            @Override
            public Map<FeiShuDepartmentTree.FeiShuUserWrapper, List<Department>> apply(FeiShuDepartmentTree.DepartmentNode departNode) {
                final Map<FeiShuDepartmentTree.FeiShuUserWrapper, List<Department>> internalMapping = new HashMap<>();
                departNode.getUsers().forEach(user -> {
                    internalMapping.compute(new FeiShuDepartmentTree.FeiShuUserWrapper(user), (k, v) -> {
                        if (v == null) {
                            return Lists.newArrayList(departNode.getDepart());
                        }
                        v.add(departNode.getDepart());
                        return v;
                    });
                });

                for (final FeiShuDepartmentTree.DepartmentNode childNode : departNode.getChildNodes()) {
                    this.apply(childNode).forEach((user, departs) -> {
                        internalMapping.compute(user, (k, v) -> {
                            if (v == null) {
                                return Lists.newArrayList(departs.toArray(new Department[0]));
                            }
                            v.addAll(departs);
                            return v;
                        });
                    });
                }

                return internalMapping;
            }
        }

        final Map<FeiShuDepartmentTree.FeiShuUserWrapper, List<Department>> mapping = new HashMap<>();
        mapping.put(new FeiShuDepartmentTree.FeiShuUserWrapper(departmentTree.getAdmin()), Lists.newArrayList(departmentTree.getTenantAsDepartment()));

        for (final FeiShuDepartmentTree.DepartmentNode departNode : departmentTree.getDepartmentNodes()) {
            new InternalFunc().apply(departNode).forEach((user, departs) -> {
                mapping.compute(user, (k, v) -> {
                    if (v == null) {
                        return departs;
                    }
                    v.addAll(departs);
                    return v;
                });
            });
        }

        mapping.forEach((user, departs) -> {
            final SysUser sysUser = userMapper.selectById(user.get().getUserId());
            sysUserService.editUser(sysUser, null, departs.stream().map(Department::getDepartmentId).collect(Collectors.joining(",")));
        });

        return SyncInfoVo.successInfo("successful!");
    }

    @Override
    public int removeThirdAppUser(List<String> userIdList) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean sendMessage(MessageDTO message, boolean verifyConfig) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean sendMessage(MessageDTO message) {
        throw new UnsupportedOperationException();
    }


}
