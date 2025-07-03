package com.xxl.job.admin.dao.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.xxl.job.admin.core.model.XxlJobInfo;
import com.xxl.job.admin.dao.XxlJobInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.xxl.job.admin.core.model.QXxlJobInfo.xxlJobInfo;

@Component
public class XxlJobInfoDaoImpl implements XxlJobInfoDao {

    public XxlJobInfoDaoImpl(@Autowired JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    private final JPAQueryFactory queryFactory;


    @Override
    public List<XxlJobInfo> pageList(int offset, int pagesize, int jobGroup, int triggerStatus, String jobDesc, String executorHandler, String author) {
        return queryFactory.selectFrom(xxlJobInfo)
                .where(pageCondition(jobGroup, triggerStatus, jobDesc, executorHandler, author))
                .orderBy(xxlJobInfo.id.desc())
                .offset(offset).limit(pagesize).fetch();
    }

    @Override
    public int pageListCount(int offset, int pagesize, int jobGroup, int triggerStatus, String jobDesc, String executorHandler, String author) {
        Long count = queryFactory.select(xxlJobInfo.count())
                .from(xxlJobInfo)
                .where(pageCondition(jobGroup, triggerStatus, jobDesc, executorHandler, author))
                .fetchOne();
        return count == null ? 0 : count.intValue();
    }

    private BooleanExpression pageCondition(int jobGroup, int triggerStatus, String jobDesc, String executorHandler, String author) {
        BooleanExpression where = null;
        if (jobGroup > 0) {
            where = xxlJobInfo.jobGroup.eq(jobGroup);
        }
        if (triggerStatus > 0) {
            where = xxlJobInfo.triggerStatus.eq(triggerStatus).and(where);
        }
        if (StringUtils.hasText(jobDesc)) {
            where = xxlJobInfo.jobDesc.containsIgnoreCase(jobDesc).and(where);
        }
        if (StringUtils.hasText(executorHandler)) {
            where = xxlJobInfo.executorHandler.containsIgnoreCase(executorHandler).and(where);
        }
        if (StringUtils.hasText(author)) {
            where = xxlJobInfo.author.containsIgnoreCase(author).and(where);
        }

        return where;
    }

    @Transactional
    @Override
    public int save(XxlJobInfo info) {
        return (int) queryFactory.insert(xxlJobInfo)
                .columns(xxlJobInfo.jobGroup, xxlJobInfo.jobDesc,
                        xxlJobInfo.addTime, xxlJobInfo.updateTime,
                        xxlJobInfo.author, xxlJobInfo.alarmEmail, xxlJobInfo.scheduleConf,
                        xxlJobInfo.misfireStrategy, xxlJobInfo.executorRouteStrategy,
                        xxlJobInfo.executorHandler, xxlJobInfo.executorParam,
                        xxlJobInfo.executorBlockStrategy, xxlJobInfo.executorTimeout,
                        xxlJobInfo.executorFailRetryCount,
                        xxlJobInfo.glueType, xxlJobInfo.glueSource,
                        xxlJobInfo.glueRemark, xxlJobInfo.glueUpdatetime,
                        xxlJobInfo.childJobId,
                        xxlJobInfo.triggerStatus, xxlJobInfo.triggerLastTime, xxlJobInfo.triggerNextTime)
                .values(info.getJobGroup(), info.getJobDesc(),
                        info.getAddTime(), info.getUpdateTime(),
                        info.getAuthor(), info.getAlarmEmail(), info.getScheduleConf(),
                        info.getMisfireStrategy(), info.getExecutorRouteStrategy(),
                        info.getExecutorHandler(), info.getExecutorParam(),
                        info.getExecutorBlockStrategy(), info.getExecutorTimeout(),
                        info.getExecutorFailRetryCount(),
                        info.getGlueType(), info.getGlueSource(),
                        info.getGlueRemark(), info.getGlueUpdatetime(),
                        info.getChildJobId(),
                        info.getTriggerStatus(), info.getTriggerLastTime(), info.getTriggerNextTime())
                .execute();
    }

    @Override
    public XxlJobInfo loadById(int id) {
        return queryFactory.selectFrom(xxlJobInfo).where(xxlJobInfo.id.eq(id)).fetchOne();
    }

    @Transactional
    @Override
    public int update(XxlJobInfo xxlJobInfoValue) {
        return (int) queryFactory.update(xxlJobInfo)
                .set(xxlJobInfo.jobGroup, xxlJobInfoValue.getJobGroup())
                .set(xxlJobInfo.jobDesc, xxlJobInfoValue.getJobDesc())
                .set(xxlJobInfo.updateTime, xxlJobInfoValue.getUpdateTime())
                .set(xxlJobInfo.author, xxlJobInfoValue.getAuthor())
                .set(xxlJobInfo.alarmEmail, xxlJobInfoValue.getAlarmEmail())
                .set(xxlJobInfo.scheduleType, xxlJobInfoValue.getScheduleType())
                .set(xxlJobInfo.scheduleConf, xxlJobInfoValue.getScheduleConf())
                .set(xxlJobInfo.misfireStrategy, xxlJobInfoValue.getMisfireStrategy())
                .set(xxlJobInfo.executorRouteStrategy, xxlJobInfoValue.getExecutorRouteStrategy())
                .set(xxlJobInfo.executorHandler, xxlJobInfoValue.getExecutorHandler())
                .set(xxlJobInfo.executorParam, xxlJobInfoValue.getExecutorParam())
                .set(xxlJobInfo.executorBlockStrategy, xxlJobInfoValue.getExecutorBlockStrategy())
                .set(xxlJobInfo.executorTimeout, xxlJobInfoValue.getExecutorTimeout())
                .set(xxlJobInfo.executorFailRetryCount, xxlJobInfoValue.getExecutorFailRetryCount())
                .set(xxlJobInfo.glueType, xxlJobInfoValue.getGlueType())
                .set(xxlJobInfo.glueSource, xxlJobInfoValue.getGlueSource())
                .set(xxlJobInfo.glueRemark, xxlJobInfoValue.getGlueRemark())
                .set(xxlJobInfo.glueUpdatetime, xxlJobInfoValue.getGlueUpdatetime())
                .set(xxlJobInfo.childJobId, xxlJobInfoValue.getChildJobId())
                .set(xxlJobInfo.triggerStatus, xxlJobInfoValue.getTriggerStatus())
                .set(xxlJobInfo.triggerLastTime, xxlJobInfoValue.getTriggerLastTime())
                .set(xxlJobInfo.triggerNextTime, xxlJobInfoValue.getTriggerNextTime())
                .where(xxlJobInfo.id.eq(xxlJobInfoValue.getId()))
                .execute();

    }

    @Transactional
    @Override
    public int delete(long id) {
        return (int) queryFactory.delete(xxlJobInfo).where(xxlJobInfo.id.eq((int) id)).execute();
    }

    @Override
    public List<XxlJobInfo> getJobsByGroup(int jobGroup) {
        return queryFactory.selectFrom(xxlJobInfo).where(xxlJobInfo.jobGroup.eq(jobGroup)).fetch();
    }

    @Override
    public int findAllCount() {
        Long count = queryFactory.select(xxlJobInfo.count()).from(xxlJobInfo).fetchOne();
        return count == null ? 0 : count.intValue();
    }

    @Override
    public List<XxlJobInfo> scheduleJobQuery(long maxNextTime, int pagesize) {
        return queryFactory.select(xxlJobInfo)
                .from(xxlJobInfo)
                .where(xxlJobInfo.triggerStatus.eq(1).and(xxlJobInfo.triggerNextTime.loe(maxNextTime)))
                .orderBy(xxlJobInfo.id.asc())
                .limit(pagesize)
                .fetch();

    }

    @Transactional
    @Override
    public int scheduleUpdate(XxlJobInfo xxlJobInfoValue) {
        JPAUpdateClause update = queryFactory.update(xxlJobInfo)
                .set(xxlJobInfo.triggerLastTime, xxlJobInfoValue.getTriggerLastTime());
        if (xxlJobInfoValue.getTriggerStatus() >= 0) {
            update.set(xxlJobInfo.triggerStatus, xxlJobInfoValue.getTriggerStatus());
        }
        return (int) update.where(xxlJobInfo.id.eq(xxlJobInfoValue.getId())).execute();
    }
}
