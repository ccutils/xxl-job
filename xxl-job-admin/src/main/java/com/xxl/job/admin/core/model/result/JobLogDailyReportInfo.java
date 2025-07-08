package com.xxl.job.admin.core.model.result;

public class JobLogDailyReportInfo {


    private Integer triggerDayCountRunning;
    private Integer triggerDayCount;
    private Integer triggerDayCountSuc;

    public Integer getTriggerDayCountRunning() {
        return triggerDayCountRunning == null ? 0 : triggerDayCountRunning;
    }

    public void setTriggerDayCountRunning(Integer triggerDayCountRunning) {
        this.triggerDayCountRunning = triggerDayCountRunning;
    }

    public Integer getTriggerDayCount() {
        return triggerDayCount == null ? 0 : triggerDayCount;
    }

    public void setTriggerDayCount(Integer triggerDayCount) {
        this.triggerDayCount = triggerDayCount;
    }

    public Integer getTriggerDayCountSuc() {
        return triggerDayCountSuc == null ? 0 : triggerDayCountSuc;
    }

    public void setTriggerDayCountSuc(Integer triggerDayCountSuc) {
        this.triggerDayCountSuc = triggerDayCountSuc;
    }
}
