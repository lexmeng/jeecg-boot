package org.jeecg.modules.publishlist.bpservice;

import com.lark.oapi.Client;
import com.lark.oapi.service.tenant.v2.model.Tenant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.list.SynchronizedList;
import org.jeecg.common.api.dto.message.BusMessageDTO;
import org.jeecg.common.api.dto.message.MessageDTO;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.vo.ComboModel;
import org.jeecg.modules.publishlist.entity.ItCostRuleMonth;
import org.jeecg.modules.publishlist.entity.ItCostRuleYear;
import org.jeecg.modules.publishlist.entity.ItSoftwareMonthlyCost;
import org.jeecg.modules.publishlist.entity.ItSoftwareRule;
import org.jeecg.modules.publishlist.exception.BussinessException;
import org.jeecg.modules.publishlist.service.IItCostRuleMonthService;
import org.jeecg.modules.publishlist.service.IItCostRuleYearService;
import org.jeecg.modules.publishlist.service.IItSoftwareMonthlyCostService;
import org.jeecg.modules.publishlist.service.IItSoftwareRuleService;
import org.jeecg.modules.publishlist.tools.FeishuMessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;

import org.jeecg.modules.publishlist.tools.FeiShuUtils;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class ItSoftwareMonthlyCostBPService {
    @Autowired
    private ISysBaseAPI sysBaseAPI;

    @Autowired
    private IItSoftwareMonthlyCostService iItSoftwareMonthlyCostService;

    @Autowired
    private IItCostRuleMonthService itCostRuleMonthService;

    @Autowired
    private IItCostRuleYearService itCostRuleYearService;

    @Autowired
    private IItSoftwareRuleService itSoftwareRuleService;

    private String softwareGitHub = "GitHub";

    private String softwareO365Business = "O365 Business";

    private String softwareO365E5 = "O365 E5";

    private String softwareJira = "Jira";
    private String softwareZoom = "Zoom";

    private String softwareFeishuQiJian = "飞书旗舰版";

    private String softwareFeiShuJiXiao = "飞书绩效";

    private String softwareHuiLianYi = "汇联易";

    private String softwareXinRenXinShi = "薪人薪事";

    private String softwareZoho = "Zoho";

    private String softwareJinDieK3 = "金蝶K3";
    private Map<String, Double> softwareUnitPrice = new HashMap<>();

    private Map<String, Integer> softwareTotalPrice = new HashMap<>();

    private Map<String, String> userDepartmentMap = new HashMap<>();

    private List<ItSoftwareRule> itSoftwareRuleList = new ArrayList<>();

    private void initialUnitPriceMap(){
        List<ItCostRuleMonth> itCostRuleMonthList = itCostRuleMonthService.list();
        for(ItCostRuleMonth itCostRuleMonth: itCostRuleMonthList){
            softwareUnitPrice.put(itCostRuleMonth.getSoftwareName(), itCostRuleMonth.getMonthPrice());
        }
        /*
        softwareUnitPrice.put(softwareGitHub, 28d);
        softwareUnitPrice.put(softwareO365Business, 90d);
        softwareUnitPrice.put(softwareO365E5, 145d);
        softwareUnitPrice.put(softwareJira, 90d);
        softwareUnitPrice.put(softwareZoom, 17d);
        */
    }

    private void initialTotalPriceMap(){
        List<ItCostRuleYear> itCostRuleYearList = itCostRuleYearService.list();
        for(ItCostRuleYear itCostRuleYear : itCostRuleYearList){
            softwareTotalPrice.put(itCostRuleYear.getSoftwareName(), itCostRuleYear.getTotalAnnualCost());
        }
        /*
        softwareTotalPrice.put(softwareFeishuQiJian, 237600);
        softwareTotalPrice.put(softwareFeiShuJiXiao, 82110);
        softwareTotalPrice.put(softwareHuiLianYi, 75600);
        softwareTotalPrice.put(softwareXinRenXinShi, 64800);
        softwareTotalPrice.put(softwareZoho, 177600);
        softwareTotalPrice.put(softwareJinDieK3, 2800);
        */
    }

    private void initialSoftwareRule(){
        itSoftwareRuleList = itSoftwareRuleService.list();
    }

    private void validate(){

        //检查软件数是否一致
        Set<String> softwareNameSet = new HashSet<>();
        for(ItSoftwareRule itSoftwareRule : itSoftwareRuleList){
            softwareNameSet.add(itSoftwareRule.getName());
        }

        Set<String> softwareNamePriceSet = new HashSet<>();
        softwareNamePriceSet.addAll(softwareUnitPrice.keySet());
        softwareNamePriceSet.addAll(softwareTotalPrice.keySet());

        if(!softwareNameSet.equals(softwareNamePriceSet)){
            FeishuMessageUtils.sendFeiShuMsg("it_software_rule表的软件数量和价格表的软件数量不匹配，任务失败！");
            throw new BussinessException("it_software_rule表的软件数量和价格表的软件数量不匹配，任务失败！");
        }
    }

    private void validateUser(List<ComboModel> userList){

        Set<String> allUserName = new HashSet<>();
        for(ComboModel user : userList){
            allUserName.add(user.getUsername());
        }

        Set<String> userNameList = new HashSet<>();
        for(ItSoftwareRule itSoftwareRule : itSoftwareRuleList){
            userNameList.add(itSoftwareRule.getOwner());
        }

        if(!allUserName.equals(userNameList)){
            FeishuMessageUtils.sendFeiShuMsg("it_software_rule表的用户集和系统的用户集不匹配，任务失败！");
            throw new BussinessException("it_software_rule表的用户集和系统的用户集不匹配，任务失败！");
        }
    }

    private Boolean findUserRule(String userName){
        for(ItSoftwareRule itSoftwareRule : itSoftwareRuleList){
            if(itSoftwareRule.getOwner().equals(userName)){
                if(itSoftwareRule.getYesOrNo().equals("YES")){
                    return true;
                }else{
                    return false;
                }
            }
        }
        FeishuMessageUtils.sendFeiShuMsg(userName + "没有包含在itSoftwareRule表中！");
        log.error(userName + "没有包含在itSoftwareRule表中！");
        throw new BussinessException(userName + "没有包含在itSoftwareRule表中！");
    }
    @Transactional
    private void saveBatchMap(Map<String, Double> unitPriceMap, String year, String month, String userName, String departmentName){
        Boolean isNeeded = findUserRule(userName);
        if(!isNeeded){
            return;
        }

        List<ItSoftwareMonthlyCost> list = new ArrayList<>();

        for(String softwareName : unitPriceMap.keySet()){
            ItSoftwareMonthlyCost cost = new ItSoftwareMonthlyCost();
            cost.setYear(Integer.parseInt(year));
            cost.setMonth(Integer.parseInt(month));
            cost.setDepartment(departmentName);
            cost.setSoftwareName(softwareName);
            cost.setDepartment(departmentName);
            cost.setCost(unitPriceMap.get(softwareName));
            list.add(cost);
        }
        iItSoftwareMonthlyCostService.saveBatch(list);
    }

    @Transactional
    public void generateMonthlyCost(String year, String month){
        initialUnitPriceMap();
        initialTotalPriceMap();
        initialSoftwareRule();
        validate();
        //如果已经有数据，则提示
        //todo
        //如果没数据
        List<ComboModel> userList = sysBaseAPI.queryAllUserBackCombo();
        log.info("一共有"+userList.size()+"个用户！");

        Integer userTotal = 0;//由于有张三这种测试用户，所以需要统计有部门的用户总数
        List<ComboModel> validUserList = new ArrayList<>();
        for(ComboModel user : userList){
            List<String> departmentNameList = sysBaseAPI.getDepartNamesByUsername(user.getUsername());
            if(departmentNameList == null || departmentNameList.isEmpty()){
                log.info(user.getUsername() + "没有部门！");
                sendMessage(user.getUsername() + "没有部门！",user.getUsername() + "没有部门！");
                continue;
            }
            //该用户不需要记录
            if(!findUserRule(user.getUsername())){
                log.info(user.getUsername() + "不需要记录");
                continue;
            }
            validUserList.add(user);
            userTotal++;

            if(departmentNameList.size() > 1){
                log.info("有用户属于多个部门！");
            }else{
                log.info(departmentNameList.get(0));
                userDepartmentMap.put(user.getUsername(), departmentNameList.get(0));
            }
        }

        validateUser(validUserList);

        for(ComboModel user : userList){
            List<String> departmentNameList = sysBaseAPI.getDepartNamesByUsername(user.getUsername());
            if(departmentNameList != null && !departmentNameList.isEmpty()){

                //1、save unit price to db
                saveBatchMap(softwareUnitPrice, year, month, user.getUsername(), userDepartmentMap.get(user.getUsername()));

                Map<String, Double> unitPriceMap = new HashMap<>();
                //2、save total price to db
                for(String softwareName : softwareTotalPrice.keySet()){
                    Double monthPrice = softwareTotalPrice.get(softwareName)/12d;
                    Double unitPrice = limitDoubleTwo(monthPrice/userTotal);
                    unitPriceMap.put(softwareName, unitPrice);
                }
                //save to db
                saveBatchMap(unitPriceMap, year, month, user.getUsername(), userDepartmentMap.get(user.getUsername()));
            }
        }

    }

    //validate,校验该month和year的数据量是否等于userTotal*11
    public Boolean validate(String year, String month){
        List<ComboModel> userList = sysBaseAPI.queryAllUserBackCombo();
        Integer userTotal = 0;//由于有张三这种测试用户，所以需要统计有部门的用户总数
        for(ComboModel user : userList){
            List<String> departmentNameList = sysBaseAPI.getDepartNamesByUsername(user.getUsername());
            if(departmentNameList == null || departmentNameList.isEmpty()){
                continue;
            }
            if(!findUserRule(user.getUsername())){
                continue;
            }
            userTotal++;
        }

        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("year", Integer.parseInt(year));
        queryMap.put("month", Integer.parseInt(month));
        List<ItSoftwareMonthlyCost> list = iItSoftwareMonthlyCostService.listByMap(queryMap);

        Integer itemTotal = userTotal*(softwareUnitPrice.size() + softwareTotalPrice.size());//单价软件数+总价软件数
        if(itemTotal.equals(Integer.valueOf(list.size()))){
            return true;
        }else{
            FeishuMessageUtils.sendFeiShuMsg("生成的总数不对。应该生成"+itemTotal+"。实际生成"+list.size());
            throw new BussinessException("生成的总数不对。应该生成"+itemTotal+"。实际生成"+list.size());
        }
    }

    //限制小数点后2位
    private Float limitFloatTwo(Float number){
        DecimalFormat df = new DecimalFormat("#.##");
        String formattedNumber = df.format(number);
        Float formattedFloat = Float.parseFloat(formattedNumber);
        return formattedFloat;
    }

    private Double limitDoubleTwo(Double number){
        DecimalFormat df = new DecimalFormat("#.##");
        String formattedNumber = df.format(number);
        Double formattedDouble = Double.parseDouble(formattedNumber);
        return formattedDouble;
    }

    public void sendMessage(String title, String content) {
        try{
            final Client client = FeiShuUtils.getClient();

            //Tenant tenant = FeiShuUtils.getRespBody(client.tenant().tenant().query()).getTenant();
            //FeiShuUtils.sendMessage(tenant.getTenantKey());

            MessageDTO messageDTO = new BusMessageDTO();
            messageDTO.setFromUser("lianfei.qu@kyligence.io");
            messageDTO.setToUser("lianfei.qu@kyligence.io");
            messageDTO.setTitle(title);
            messageDTO.setContent(content);
            sysBaseAPI.sendSysAnnouncement(messageDTO);
            FeishuMessageUtils.sendFeiShuMsg(content);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
