package org.jeecg.modules.devops.domainservice.impl;

import org.jeecg.modules.devops.entity.Publishlist;
import org.jeecg.modules.devops.entity.PublishlistProject;
import org.jeecg.modules.devops.exception.BussinessException;
import org.jeecg.modules.devops.mapper.PublishlistMapper;
import org.jeecg.modules.devops.mapper.PublishlistProjectMapper;
import org.jeecg.modules.devops.domainservice.IPublishlistDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.List;


@Service
public class PublishlistDomainServiceImpl implements IPublishlistDomainService {
    @Autowired
    PublishlistMapper publishlistMapper;

    @Autowired
    PublishlistProjectMapper publishlistProjectMapper;

    @Autowired
    PublishlistStatusMachine statusMachine;

    /**
     * deprecated/removed
     * @param publishlist
     * @param projectList
     * @return
     */
    /*public int newPublishlist(Publishlist publishlist, List<PublishlistProject> projectList){
        validate(publishlist, projectList);

        String id = IdTool.generalId();
        publishlist.setId(id);

        publishlist.setPublishlistStage(statusMachine.init());
        publishlistMapper.insert(publishlist);

        int result=0;
        for(PublishlistProject project : projectList){
            project.setPublishlistId(id);
            project.setId(IdTool.generalId());
            result =publishlistProjectMapper.insert(project);
        }

        return result;
    }*/

    private void validate(Publishlist publishlist, List<PublishlistProject> projectList){
        String productLineName = publishlist.getProductLineName();
        if(productLineName == null || productLineName.equals("")){
            throw new BussinessException("产品线名称为空");
        }
        if(productLineName.contains("KE")||productLineName.contains("KC")){
            //pass
        }
        else{
            throw new BussinessException("产品线名称错误！");
        }

        String productName = publishlist.getProductName();
        if(productLineName.contains("KE")||productLineName.contains("KC")){
            //pass
        }
        else{
            throw new BussinessException("产品名称错误！");
        }

        String versionType = publishlist.getVersionType();
        if(versionType == null || versionType.equals("")){
            throw new BussinessException("发布单版本为空");
        }
        if(versionType.equals("GA")||versionType.equals("SP")||versionType.equals("HOTFIX")){
            //pass
        }else{
            throw new BussinessException("发布单版本类型错误！");
        }


    }

    @Transactional
    @Override
    public void develop(String publishlistId){
        Publishlist publishlist = publishlistMapper.selectById(publishlistId);

        String currentStatus = publishlist.getPublishlistStage();
        publishlist.setPublishlistStage(statusMachine.develop(currentStatus));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        publishlist.setPublishDatetime(sdf.format(System.currentTimeMillis()));
        updatePublislist(publishlist);

        publishlistMapper.updateById(publishlist);
    }

    @Transactional
    @Override
    public void test(String publishlistId){
        Publishlist publishlist = publishlistMapper.selectById(publishlistId);

        String currentStatus = publishlist.getPublishlistStage();
        publishlist.setPublishlistStage(statusMachine.test(currentStatus));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        publishlist.setPublishDatetime(sdf.format(System.currentTimeMillis()));
        updatePublislist(publishlist);

        publishlistMapper.updateById(publishlist);
    }


    @Transactional
    public void publish(String publishlistId){
        Publishlist publishlist = publishlistMapper.selectById(publishlistId);

        String currentStatus = publishlist.getPublishlistStage();
        publishlist.setPublishlistStage(statusMachine.publish(currentStatus));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        publishlist.setPublishDatetime(sdf.format(System.currentTimeMillis()));
        updatePublislist(publishlist);

        publishlistMapper.updateById(publishlist);
        return;
    }

    private void updatePublislist(Publishlist publishlist){
        //todo:更新更新时间和更新人，跟用户模块对接
    }

    public boolean isPublished(String publishlistId){
        Publishlist publishlist = publishlistMapper.selectById(publishlistId);

        return statusMachine.isPublished(publishlist.getPublishlistStage());
    }
}
