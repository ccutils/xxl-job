package com.xxl.job.admin.dao;

import com.xxl.job.admin.core.model.XxlJobInfo;

import java.util.List;


/**
 * job info
 * @author xuxueli 2016-1-12 18:03:45
 */
public interface XxlJobInfoDao {

    List<XxlJobInfo> pageList(int offset,
                              int pagesize,
                              int jobGroup,
                              int triggerStatus,
                              String jobDesc,
                              String executorHandler,
                              String author);

    int pageListCount(int offset,
                      int pagesize,
                      int jobGroup,
                      int triggerStatus,
                      String jobDesc,
                      String executorHandler,
                      String author);

    int save(XxlJobInfo info);

    XxlJobInfo loadById(int id);

    int update(XxlJobInfo xxlJobInfo);

    int delete(long id);

    List<XxlJobInfo> getJobsByGroup(int jobGroup);

    int findAllCount();

    List<XxlJobInfo> scheduleJobQuery(long maxNextTime, int pagesize);


    int scheduleUpdate(XxlJobInfo xxlJobInfo);

}
