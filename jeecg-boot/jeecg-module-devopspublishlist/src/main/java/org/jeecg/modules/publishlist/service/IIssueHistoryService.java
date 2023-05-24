package org.jeecg.modules.publishlist.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.publishlist.entity.IssueHistory;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.publishlist.vo.IssueHistoryBatchVo;

import java.util.List;

/**
 * @Description: issue历史表
 * @Author: jeecg-boot
 * @Date:   2023-04-17
 * @Version: V1.0
 */
public interface IIssueHistoryService extends IService<IssueHistory> {

    public List<IssueHistoryBatchVo> pageQueryBatch(String publishlistId, IPage page);

    public long queryBatchTotal(String publishlistId);

}
