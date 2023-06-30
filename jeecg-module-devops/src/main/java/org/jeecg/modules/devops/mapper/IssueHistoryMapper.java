package org.jeecg.modules.devops.mapper;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.devops.entity.IssueHistory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @Description: issue历史表
 * @Author: jeecg-boot
 * @Date:   2023-04-17
 * @Version: V1.0
 */
public interface IssueHistoryMapper extends BaseMapper<IssueHistory> {

    public List<Integer> selectBatchNumBySort(@Param("publishlistId") String publishlistId, @Param("start") long start, @Param("size") long size);

    public Integer selectBatchNumCount(@Param("publishlistId") String publishlistId);
}
