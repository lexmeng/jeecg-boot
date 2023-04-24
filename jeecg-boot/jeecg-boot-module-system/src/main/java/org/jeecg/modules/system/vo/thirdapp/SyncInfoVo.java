package org.jeecg.modules.system.vo.thirdapp;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 同步结果信息，包含成功的信息和失败的信息
 *
 * @author sunjianlei
 */
@Data
@ApiModel(value = "第三方 APP 同步结果", description = "包含第三方 APP 数据同步失败或成功的消息")
public class SyncInfoVo {

    /**
     * 成功的信息
     */
    private List<String> successInfo;
    /**
     * 失败的信息
     */
    private List<String> failInfo;

    public SyncInfoVo() {
        this.successInfo = new ArrayList<>();
        this.failInfo = new ArrayList<>();
    }

    public static SyncInfoVo merge(SyncInfoVo v1, SyncInfoVo v2) {
        final SyncInfoVo vo = new SyncInfoVo();
        v1.getFailInfo().forEach(vo::addFailInfo);
        v1.getSuccessInfo().forEach(vo::addSuccessInfo);

        v2.getFailInfo().forEach(vo::addFailInfo);
        v2.getSuccessInfo().forEach(vo::addSuccessInfo);
        return vo;
    }

    public static SyncInfoVo successInfo(String info) {
        final SyncInfoVo vo = new SyncInfoVo();
        vo.addSuccessInfo(info);
        return vo;
    }

    public static SyncInfoVo failInfo(String info) {
        final SyncInfoVo vo = new SyncInfoVo();
        vo.addFailInfo(info);
        return vo;
    }

    public SyncInfoVo(List<String> successInfo, List<String> failInfo) {
        this.successInfo = successInfo;
        this.failInfo = failInfo;
    }

    public SyncInfoVo addSuccessInfo(String info) {
        this.successInfo.add(info);
        return this;
    }

    public SyncInfoVo addFailInfo(String info) {
        this.failInfo.add(info);
        return this;
    }
}
