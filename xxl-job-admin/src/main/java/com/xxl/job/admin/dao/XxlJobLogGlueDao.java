package com.xxl.job.admin.dao;

import com.xxl.job.admin.core.model.XxlJobLogGlue;

import java.util.List;

/**
 * job log for glue
 * @author xuxueli 2016-5-19 18:04:56
 */
public interface XxlJobLogGlueDao {

    int save(XxlJobLogGlue xxlJobLogGlue);

    List<XxlJobLogGlue> findByJobId(int jobId);

    int removeOld(int jobId, int limit);

    int deleteByJobId(int jobId);

}
