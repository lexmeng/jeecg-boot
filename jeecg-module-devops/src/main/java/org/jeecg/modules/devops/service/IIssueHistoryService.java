package org.jeecg.modules.devops.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.modules.devops.vo.IssueHistoryBatchVo;
import org.jeecg.modules.devops.entity.IssueHistory;
import com.baomidou.mybatisplus.extension.service.IService;

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
