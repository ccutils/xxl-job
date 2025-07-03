package com.xxl.job.admin.dao;

import com.xxl.job.admin.core.model.XxlJobLogReport;

import java.util.Date;
import java.util.List;

/**
 * job log
 * @author xuxueli 2019-11-22
 */

public interface XxlJobLogReportDao {

	int save(XxlJobLogReport xxlJobLogReport);

	int update(XxlJobLogReport xxlJobLogReport);

	List<XxlJobLogReport> queryLogReport( Date triggerDayFrom,
												 Date triggerDayTo);

	XxlJobLogReport queryLogReportTotal();

}
