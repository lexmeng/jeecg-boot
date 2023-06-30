package org.jeecg.modules.system.util;

import cn.hutool.core.util.NumberUtil;
import com.lark.oapi.service.contact.v3.model.Department;
import com.lark.oapi.service.contact.v3.model.User;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysUser;

@UtilityClass
public class FeiShuVoMappingUtil {

    public static final String DEFAULT_PASSWORD = "kylin@123456";

    @NonNull
    public static SysDepart departmentToSysDepart(Department department) {
        final SysDepart sysDepart = new SysDepart();
        sysDepart.setId(department.getDepartmentId());
        sysDepart.setParentId(department.getParentDepartmentId());
        sysDepart.setDepartName(department.getName());
        sysDepart.setDepartNameEn(department.getI18nName() != null ? department.getI18nName().getEnUs() : null);
        sysDepart.setDepartOrder(NumberUtil.isNumber(department.getOrder()) ? Integer.valueOf(department.getOrder()) : null);
        // 机构类别 1=公司，2=组织机构，3=岗位
        sysDepart.setOrgCategory(department.getParentDepartmentId() == null ? "1" : "2");
        // 状态（1启用，0不启用）
        sysDepart.setStatus("1");
        // 删除状态（0，正常，1已删除）
        sysDepart.setDelFlag(department.getStatus().getIsDeleted() ? "1" : "0");
        sysDepart.setDirectorUserIds(department.getLeaderUserId());
        return sysDepart;
    }

    public static SysUser userToSysUser(User user) {
        final SysUser sysUser = new SysUser();

        sysUser.setId(user.getUserId());
        sysUser.setUsername(user.getEmail());
        sysUser.setRealname(user.getName());

        final String salt = oConvertUtils.randomGen(8);
        final String passwordEncode = PasswordUtil.encrypt(sysUser.getUsername(), DEFAULT_PASSWORD, salt);
        sysUser.setSalt(salt);
        sysUser.setPassword(passwordEncode);

        sysUser.setAvatar(user.getAvatar().getAvatar240());
        sysUser.setSex(user.getGender());
        sysUser.setEmail(user.getEmail());
        sysUser.setPhone(user.getMobile());
        sysUser.setStatus(user.getStatus().getIsActivated() && !user.getStatus().getIsResigned() ? 1 : 2);
        sysUser.setDelFlag(user.getStatus().getIsResigned() ? 1 : 0);

        return sysUser;
    }

}
