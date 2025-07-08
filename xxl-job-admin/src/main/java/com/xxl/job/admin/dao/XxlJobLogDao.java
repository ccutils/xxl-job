package com.xxl.job.admin.dao;

import com.xxl.job.admin.core.model.XxlJobLog;
import com.xxl.job.admin.core.model.result.JobLogDailyReportInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * job log
 * @author xuxueli 2016-1-12 18:03:06
 */
@Mapper
public interface XxlJobLogDao {

    List<XxlJobLog> list(@Param("jobGroup") int jobGroup,
                         @Param("jobId") int jobId,
                         @Param("triggerTimeStart") Date triggerTimeStart,
                         @Param("triggerTimeEnd") Date triggerTimeEnd,
                         @Param("logStatus") int logStatus);

    public XxlJobLog load(@Param("id") long id);

    public long save(XxlJobLog xxlJobLog);

    public int updateTriggerInfo(XxlJobLog xxlJobLog);

    public int updateHandleInfo(XxlJobLog xxlJobLog);

    public int delete(@Param("jobId") int jobId);

    public JobLogDailyReportInfo findLogReport(@Param("from") Date from,
                                               @Param("to") Date to);

    public List<Long> findClearLogIds(@Param("jobGroup") int jobGroup,
                                      @Param("jobId") int jobId,
                                      @Param("clearBeforeTime") Date clearBeforeTime,
                                      @Param("clearBeforeNum") int clearBeforeNum);

    public int clearLog(@Param("logIds") List<Long> logIds);

    public List<Long> findFailJobLogIds();

    public int updateAlarmStatus(@Param("logId") long logId,
                                 @Param("oldAlarmStatus") int oldAlarmStatus,
                                 @Param("newAlarmStatus") int newAlarmStatus);

    public List<Long> findLostJobIds(@Param("losedTime") Date losedTime);

}
