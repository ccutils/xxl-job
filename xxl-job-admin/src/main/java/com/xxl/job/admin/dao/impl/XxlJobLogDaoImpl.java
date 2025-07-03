package com.xxl.job.admin.dao.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.xxl.job.admin.core.model.QXxlJobLog;
import com.xxl.job.admin.core.model.QXxlJobRegistry;
import com.xxl.job.admin.core.model.XxlJobLog;
import com.xxl.job.admin.dao.XxlJobLogDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.xxl.job.admin.core.model.QXxlJobLog.xxlJobLog;

@Component
public class XxlJobLogDaoImpl implements XxlJobLogDao {


    public XxlJobLogDaoImpl(@Autowired JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    private final JPAQueryFactory queryFactory;


    @Override
    public List<XxlJobLog> pageList(int offset, int pagesize, int jobGroup, int jobId, Date triggerTimeStart, Date triggerTimeEnd, int logStatus) {
        return queryFactory.selectFrom(xxlJobLog)
                .where(pageCondition(jobGroup, jobId, triggerTimeStart, triggerTimeEnd, logStatus))
                .orderBy(xxlJobLog.triggerTime.desc())
                .offset(offset).limit(pagesize).fetch();
    }

    @Override
    public int pageListCount(int offset, int pagesize, int jobGroup, int jobId, Date triggerTimeStart, Date triggerTimeEnd, int logStatus) {
        Long count = queryFactory.from(xxlJobLog)
                .select(xxlJobLog.count())
                .where(pageCondition(jobGroup, jobId, triggerTimeStart, triggerTimeEnd, logStatus))
                .fetchOne();
        return count == null ? 0 : count.intValue();
    }

    private Predicate pageCondition(int jobGroup, int jobId, Date triggerTimeStart, Date triggerTimeEnd, int logStatus) {

        BooleanBuilder where = new BooleanBuilder();

        if (jobId == 0 && jobGroup > 0) {
            where.and(xxlJobLog.jobGroup.eq(jobGroup));
        }
        if (jobId > 0) {
            where.and(xxlJobLog.jobId.eq(jobId));
        }
        if (triggerTimeStart != null) {
            where.and(xxlJobLog.triggerTime.goe(triggerTimeStart));
        }
        if (triggerTimeEnd != null) {
            where.and(xxlJobLog.triggerTime.loe(triggerTimeEnd));
        }
        switch (logStatus) {
            case 1:
                where.and(xxlJobLog.handleCode.eq(200));
                break;
            case 2:
                where.and(xxlJobLog.triggerCode.notIn(0, 200).or(xxlJobLog.handleCode.notIn(0, 200)));
                break;
            case 3:
                where.and(xxlJobLog.triggerCode.eq(200).or(xxlJobLog.handleCode.eq(0)));
        }
        return where;
    }


    @Override
    public XxlJobLog load(long id) {
        return queryFactory.selectFrom(xxlJobLog).where(xxlJobLog.id.eq(id)).fetchOne();
    }

    @Transactional
    @Override
    public long save(XxlJobLog xxlJobLogValue) {
        return (int) queryFactory.insert(xxlJobLog)
                .columns(xxlJobLog.jobGroup,
                        xxlJobLog.jobId,
                        xxlJobLog.triggerTime,
                        xxlJobLog.triggerCode,
                        xxlJobLog.handleCode)
                .values(xxlJobLogValue.getJobGroup(),
                        xxlJobLogValue.getJobId(),
                        xxlJobLogValue.getTriggerTime(),
                        xxlJobLogValue.getTriggerCode(),
                        xxlJobLogValue.getHandleCode()).execute();


    }

    @Transactional
    @Override
    public int updateTriggerInfo(XxlJobLog xxlJobLogValue) {
        //		UPDATE xxl_job_log
        //		SET
        //			`trigger_time`= #{triggerTime},
        //			`trigger_code`= #{triggerCode},
        //			`trigger_msg`= #{triggerMsg},
        //			`executor_address`= #{executorAddress},
        //			`executor_handler`=#{executorHandler},
        //			`executor_param`= #{executorParam},
        //			`executor_sharding_param`= #{executorShardingParam},
        //			`executor_fail_retry_count`= #{executorFailRetryCount}
        //		WHERE `id`= #{id}
        return (int) queryFactory.update(xxlJobLog)
                .set(xxlJobLog.triggerTime, xxlJobLogValue.getTriggerTime())
                .set(xxlJobLog.triggerCode, xxlJobLogValue.getTriggerCode())
                .set(xxlJobLog.triggerMsg, xxlJobLogValue.getTriggerMsg())
                .set(xxlJobLog.executorAddress, xxlJobLogValue.getExecutorAddress())
                .set(xxlJobLog.executorHandler, xxlJobLogValue.getExecutorHandler())
                .set(xxlJobLog.executorParam, xxlJobLogValue.getExecutorParam())
                .set(xxlJobLog.executorShardingParam, xxlJobLogValue.getExecutorShardingParam())
                .set(xxlJobLog.executorFailRetryCount, xxlJobLogValue.getExecutorFailRetryCount())
                .where(xxlJobLog.id.eq(xxlJobLogValue.getId()))
                .execute();
    }

    @Transactional
    @Override
    public int updateHandleInfo(XxlJobLog xxlJobLogValue) {
        return (int) queryFactory.update(xxlJobLog)
                .set(xxlJobLog.handleTime, xxlJobLogValue.getHandleTime())
                .set(xxlJobLog.handleCode, xxlJobLogValue.getHandleCode())
                .set(xxlJobLog.handleMsg, xxlJobLogValue.getHandleMsg())
                .execute();

    }

    @Transactional
    @Override
    public int delete(int jobId) {
        return (int) queryFactory.delete(xxlJobLog).where(xxlJobLog.jobId.eq(jobId)).execute();
    }

    public static class JobLogStatsDTO extends LinkedHashMap<String, Object> {
        public JobLogStatsDTO(Long triggerDayCount, Long triggerDayCountRunning, Long triggerDayCountSuc) {
            super();
            if (triggerDayCount != null) {
                put("triggerDayCount", triggerDayCount);
            }
            if (triggerDayCountRunning != null) {
                put("triggerDayCountRunning", triggerDayCountRunning);
            }
            if (triggerDayCountSuc != null) {
                put("triggerDayCountSuc", triggerDayCountSuc);
            }
        }
    }

    @Override
    public Map<String, Object> findLogReport(Date from, Date to) {

        //		SELECT
        //			COUNT(handle_code) `triggerDayCount`,
        //			SUM(CASE WHEN (trigger_code in (0, 200) and handle_code = 0) then 1 else 0 end) as `triggerDayCountRunning`,
        //			SUM(CASE WHEN handle_code = 200 then 1 else 0 end) as `triggerDayCountSuc`
        //		FROM xxl_job_log
        //		WHERE trigger_time BETWEEN #{from} and #{to}
        return queryFactory.select(Projections.constructor(JobLogStatsDTO.class,
                        //COUNT(handle_code) `triggerDayCount`
                        xxlJobLog.handleCode.count(),
                        //SUM(CASE WHEN (trigger_code in (0, 200) and handle_code = 0) then 1 else 0 end)
                        new CaseBuilder()
                                .when(xxlJobLog.triggerCode.in(0, 200)
                                        .and(xxlJobLog.handleCode.eq(0)))
                                .then(1L) // 使用 1L 保证类型为 Long
                                .otherwise(0L)
                                .sum(),
                        new CaseBuilder()
                                .when(xxlJobLog.handleCode.eq(200))
                                .then(1L)
                                .otherwise(0L)
                                .sum()
                ))
                .from(xxlJobLog)
                .where(xxlJobLog.triggerTime.between(from, to))
                .fetchOne();
    }

    @Override
    public List<Long> findClearLogIds(int jobGroup, int jobId, Date clearBeforeTime, int clearBeforeNum, int pagesize) {
        BooleanBuilder where = new BooleanBuilder();
        if (jobGroup > 0) {
            where.and(xxlJobLog.jobGroup.eq(jobGroup));
        }
        if (jobId > 0) {
            where.and(xxlJobLog.jobId.eq(jobId));
        }
        if (clearBeforeTime != null) {
            where.and(xxlJobLog.triggerTime.loe(clearBeforeTime));
        }
        if (clearBeforeNum > 0) {
            BooleanBuilder subWhere = new BooleanBuilder();
            if (jobGroup > 0) {
                subWhere.and(xxlJobLog.jobGroup.eq(jobGroup));
            }
            if (jobId > 0) {
                subWhere.and(xxlJobLog.jobId.eq(jobId));
            }
            where.and(xxlJobLog.id.notIn(
                    JPAExpressions
                            .select(xxlJobLog.id)
                            .from(xxlJobLog)
                            .where(subWhere)
                            .orderBy(xxlJobLog.triggerTime.desc())
                            .limit(clearBeforeNum))
            );
        }
        return queryFactory.from(xxlJobLog)
                .select(xxlJobLog.id)
                .where(where)
                .orderBy(xxlJobLog.id.asc())
                .limit(pagesize)
                .fetch();

    }

    @Transactional
    @Override
    public int clearLog(List<Long> logIds) {
        return (int) queryFactory.delete(xxlJobLog).where(xxlJobLog.id.in(logIds)).execute();
    }

    @Override
    public List<Long> findFailJobLogIds(int pagesize) {
        BooleanBuilder where = new BooleanBuilder();
        where.andNot(new BooleanBuilder()
                        .and(xxlJobLog.triggerCode.in(0, 200).and(xxlJobLog.handleCode.eq(0)))
                        .or(xxlJobLog.triggerCode.eq(200)))
                .and(xxlJobLog.alarmStatus.eq(0));
        return queryFactory.select(xxlJobLog.id).from(xxlJobLog)
                .where(where).orderBy(xxlJobLog.id.asc())
                .limit(pagesize).fetch();
    }

    @Transactional
    @Override
    public int updateAlarmStatus(long logId, int oldAlarmStatus, int newAlarmStatus) {


        //		UPDATE xxl_job_log
        //		SET
        //			`alarm_status` = #{newAlarmStatus}
        //		WHERE `id`= #{logId} AND `alarm_status` = #{oldAlarmStatus}
        return (int) queryFactory.update(xxlJobLog).set(xxlJobLog.alarmStatus, newAlarmStatus)
                .where(xxlJobLog.id.eq(logId).and(xxlJobLog.alarmStatus.eq(oldAlarmStatus))).execute();

    }

    @Override
    public List<Long> findLostJobIds(Date losedTime) {

        //		SELECT
        //			t.id
        //		FROM
        //			xxl_job_log t
        //			LEFT JOIN xxl_job_registry t2 ON t.executor_address = t2.registry_value
        //		WHERE
        //			t.trigger_code = 200
        //				AND t.handle_code = 0
        //				AND t.trigger_time <![CDATA[ <= ]]> #{losedTime}
        //				AND t2.id IS NULL;

        QXxlJobLog t = QXxlJobLog.xxlJobLog;
        QXxlJobRegistry t2 = QXxlJobRegistry.xxlJobRegistry;

        return queryFactory.select(t.id)
                .from(t)
                .leftJoin(t2).on(t.executorAddress.eq(t2.registryValue))
                .where(t.triggerCode.eq(200).and(t.handleCode.eq(0)).and(t.triggerTime.loe(losedTime)).and(t2.id.isNull()))
                .fetch();

    }
}
