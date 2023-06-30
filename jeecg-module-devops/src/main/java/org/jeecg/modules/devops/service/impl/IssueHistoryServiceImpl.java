package org.jeecg.modules.devops.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.modules.devops.entity.IssueHistory;
import org.jeecg.modules.devops.mapper.IssueHistoryMapper;
import org.jeecg.modules.devops.service.IIssueHistoryService;
import org.jeecg.modules.devops.vo.IssueHistoryBatchVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: issue历史表
 * @Author: jeecg-boot
 * @Date:   2023-04-17
 * @Version: V1.0
 */
@Service
public class IssueHistoryServiceImpl extends ServiceImpl<IssueHistoryMapper, IssueHistory> implements IIssueHistoryService {

    @Autowired
    private IssueHistoryMapper issueHistoryMapper;

    @Override
    public List<IssueHistoryBatchVo> pageQueryBatch(String publishlistId, IPage page){
        List<Integer> batchNumList = issueHistoryMapper.selectBatchNumBySort(publishlistId, page.offset(), page.getSize());
        List<IssueHistoryBatchVo> batchVoList = new ArrayList<>();
        for(Integer batchNo : batchNumList){
            IssueHistoryBatchVo vo = new IssueHistoryBatchVo();
            vo.setBatchNum(batchNo);

            //如果batchNo是1，全是增集
            if(batchNo.equals(1)){

                List<IssueHistory> list = queryIssueHistoryByBatchNo(publishlistId, batchNo);
                vo.setAddIssueList(list);
                vo.setSubIssueList(new ArrayList<IssueHistory>());

                if(list.size() != 0){
                    vo.setCreateBy(list.get(0).getCreateBy());
                    vo.setCreateTime(list.get(0).getCreateTime());
                    vo.setUpdateBy(list.get(0).getUpdateBy());
                    vo.setUpdateTime(list.get(0).getUpdateTime());
                }
            }else{
                List<IssueHistory> current = queryIssueHistoryByBatchNo(publishlistId, batchNo);
                List<IssueHistory> previous = queryIssueHistoryByBatchNo(publishlistId, batchNo -1);
                //算法：找到2个集合的公共集g，previous - g = 减少集， current - g = 增加集
                List<IssueHistory> commonArray = getCommonArray(previous, current);
                List<IssueHistory> addArray = getLeftArray(current, commonArray);
                List<IssueHistory> subArray = getLeftArray(previous, commonArray);

                vo.setAddIssueList(addArray);
                vo.setSubIssueList(subArray);

                if(current.size() != 0){
                    vo.setCreateBy(current.get(0).getCreateBy());
                    vo.setCreateTime(current.get(0).getCreateTime());
                    vo.setUpdateBy(current.get(0).getUpdateBy());
                    vo.setUpdateTime(current.get(0).getUpdateTime());
                }
            }
            batchVoList.add(vo);
        }

        return batchVoList;
    }

    private List<IssueHistory> queryIssueHistoryByBatchNo(String publishlistId, Integer batchNo){
        QueryWrapper<IssueHistory> wrapper = new QueryWrapper<>();
        wrapper.eq("publishlist_id", publishlistId);
        wrapper.eq("batch_num",batchNo);
        wrapper.orderByAsc("create_time");
        List<IssueHistory> list = issueHistoryMapper.selectList(wrapper);
        return list;
    }

    private List<IssueHistory> getCommonArray(List<IssueHistory> previous, List<IssueHistory> current){
        List<IssueHistory> commonArray = new ArrayList<>();
        for(IssueHistory issueHistory : previous){
            if(isIssueHistoryInArray(issueHistory, current)){
                commonArray.add(issueHistory);
            }
        }

        return commonArray;
    }

    private Boolean isIssueHistoryInArray(IssueHistory issueHistory, List<IssueHistory> array){
        for(IssueHistory temp : array){
            if(temp.getIssueId().equals(issueHistory.getIssueId())){
                return true;
            }
        }
        return false;
    }

    /**
     * array - commonArray得到剩余类
     * @param array
     * @param commonArray
     * @return
     */
    private List<IssueHistory> getLeftArray(List<IssueHistory> array, List<IssueHistory> commonArray){
        List<IssueHistory> leftArray = new ArrayList<>();
        for(IssueHistory issueHistory : array){
            if(!isIssueHistoryInArray(issueHistory, commonArray)){
                leftArray.add(issueHistory);
            }
        }
        return leftArray;
    }

    public long queryBatchTotal(String publishlistId){
        Integer count = issueHistoryMapper.selectBatchNumCount(publishlistId);
        return Integer.toUnsignedLong(count);
    }
}
