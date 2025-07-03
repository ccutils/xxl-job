package com.xxl.job.admin.dao.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.xxl.job.admin.core.model.QXxlJobLogReport;
import com.xxl.job.admin.core.model.XxlJobLogReport;
import com.xxl.job.admin.dao.XxlJobLogReportDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Component
public class XxlJobLogReportDaoImpl implements XxlJobLogReportDao {

    public XxlJobLogReportDaoImpl(@Autowired JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
        jobLogReport = QXxlJobLogReport.xxlJobLogReport;
    }

    private final QXxlJobLogReport jobLogReport;
    private final JPAQueryFactory queryFactory;


    @Override
    @Transactional
    public int save(XxlJobLogReport xxlJobLogReport) {
        return (int) queryFactory.insert(jobLogReport)
                .columns(jobLogReport.triggerDay,
                        jobLogReport.runningCount,
                        jobLogReport.sucCount,
                        jobLogReport.failCount)
                .values(xxlJobLogReport.getTriggerDay(),
                        xxlJobLogReport.getRunningCount(),
                        xxlJobLogReport.getSucCount(),
                        xxlJobLogReport.getFailCount())
                .execute();

    }

    @Override
    @Transactional
    public int update(XxlJobLogReport xxlJobLogReport) {
        return (int) queryFactory.update(jobLogReport)
                .set(jobLogReport.runningCount, xxlJobLogReport.getRunningCount())
                .set(jobLogReport.sucCount, xxlJobLogReport.getSucCount())
                .set(jobLogReport.failCount, xxlJobLogReport.getFailCount())
                .where(jobLogReport.triggerDay.eq(xxlJobLogReport.getTriggerDay()))
                .execute();
    }

    @Override
    public List<XxlJobLogReport> queryLogReport(Date triggerDayFrom, Date triggerDayTo) {
        return queryFactory.selectFrom(jobLogReport)
                .where(jobLogReport.triggerDay.between(triggerDayFrom, triggerDayTo))
                .orderBy(jobLogReport.triggerDay.asc())
                .fetch();
    }

    @Override
    public XxlJobLogReport queryLogReportTotal() {
        return queryFactory.select(Projections.constructor(XxlJobLogReport.class,
                        jobLogReport.runningCount.sum(),
                        jobLogReport.sucCount.sum(),
                        jobLogReport.failCount.sum()))
                .from(jobLogReport)
                .fetchOne();
    }
}
