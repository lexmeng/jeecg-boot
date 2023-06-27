package org.jeecg.modules.publishlist.bpservice;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.publishlist.entity.ItFa;
import org.jeecg.modules.publishlist.entity.ItFixAssertsTransferRecord;
import org.jeecg.modules.publishlist.entity.ItFixAssetsNetValue;
import org.jeecg.modules.publishlist.service.IItFaService;
import org.jeecg.modules.publishlist.service.IItFixAssertsTransferRecordService;
import org.jeecg.modules.publishlist.service.IItFixAssetsNetValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ItFixAssertsBPService {

    @Autowired
    private IItFaService itFaService;

    @Autowired
    private IItFixAssertsTransferRecordService itFixAssertsTransferRecordService;

    @Autowired
    private IItFixAssetsNetValueService itFixAssetsNetValueService;

    @Transactional
    public void transfer(String fixAssertId, String toUserOwner, String toDepartment, String toUseStatus, String toLocation){

        QueryWrapper<ItFa> wrapper = new QueryWrapper<ItFa>();
        wrapper.eq("fixed_assets_number", fixAssertId);
        ItFa itFa = itFaService.getOne(wrapper);

        ItFixAssertsTransferRecord record = new ItFixAssertsTransferRecord();
        record.setFixAssertsId(fixAssertId);
        record.setFromOwnName(itFa.getUseOwner());
        record.setFromDepartment(itFa.getUseOrgCode());
        record.setFromUseStatus(itFa.getUseState());
        record.setFromLocation(itFa.getSite());
        record.setToOwnerName(toUserOwner);
        record.setToDepartment(toDepartment);
        record.setToUseStatus(toUseStatus);
        record.setToLocation(toLocation);

        itFa.setUseOwner(toUserOwner);
        itFa.setUseOrgCode(toDepartment);
        itFa.setUseState(toUseStatus);
        itFa.setSite(toLocation);
        itFaService.updateById(itFa);

        itFixAssertsTransferRecordService.save(record);
    }

    @Transactional
    public void depreciation(String fixAssertId){
        QueryWrapper<ItFa> wrapper = new QueryWrapper<ItFa>();
        wrapper.eq("fixed_assets_number", fixAssertId);
        ItFa itFa = itFaService.getOne(wrapper);

        Double originalValue = itFa.getEquNetValue();
        Double newValue = itFa.getEquNetValue();//todo

        itFa.setEquNetValue(newValue);
        itFaService.updateById(itFa);

        ItFixAssetsNetValue itFixAssetsNetValue = new ItFixAssetsNetValue();
        itFixAssetsNetValue.setFixAssertsId(fixAssertId);
        itFixAssetsNetValue.setNetValue(newValue);

        Integer year = DateUtils.getCalendar().get(Calendar.YEAR);
        Integer month = DateUtils.getCalendar().get(Calendar.MONTH);
        Integer day = DateUtils.getCalendar().get(Calendar.DAY_OF_MONTH);
        itFixAssetsNetValue.setYear(year);
        itFixAssetsNetValue.setMonth(month);
        itFixAssetsNetValue.setDay(day);

        itFixAssetsNetValueService.save(itFixAssetsNetValue);
    }

    @Transactional
    public void depreciationAll(){
        List<ItFa> list = itFaService.list();
        for(ItFa itFa : list){
            depreciation(itFa.getFixedAssetsNumber());
        }
    }
}
