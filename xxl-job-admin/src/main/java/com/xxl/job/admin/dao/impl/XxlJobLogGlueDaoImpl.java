package com.xxl.job.admin.dao.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.xxl.job.admin.core.model.XxlJobLogGlue;
import com.xxl.job.admin.dao.XxlJobLogGlueDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.xxl.job.admin.core.model.QXxlJobLogGlue.xxlJobLogGlue;


@Component
public class XxlJobLogGlueDaoImpl implements XxlJobLogGlueDao {


    public XxlJobLogGlueDaoImpl(@Autowired JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    private final JPAQueryFactory queryFactory;

    @Transactional
    @Override
    public int save(XxlJobLogGlue xxlJobLogGlueValue) {
        return (int) queryFactory.insert(xxlJobLogGlue)
                .columns(xxlJobLogGlue.jobId,
                        xxlJobLogGlue.glueType,
                        xxlJobLogGlue.glueSource,
                        xxlJobLogGlue.glueRemark,
                        xxlJobLogGlue.addTime,
                        xxlJobLogGlue.updateTime)
                .values(xxlJobLogGlueValue.getJobId(),
                        xxlJobLogGlueValue.getGlueType(),
                        xxlJobLogGlueValue.getGlueSource(),
                        xxlJobLogGlueValue.getGlueRemark(),
                        xxlJobLogGlueValue.getAddTime(),
                        xxlJobLogGlueValue.getUpdateTime())
                .execute();
    }

    @Override
    public List<XxlJobLogGlue> findByJobId(int jobId) {
        return queryFactory.selectFrom(xxlJobLogGlue)
                .where(xxlJobLogGlue.jobId.eq(jobId))
                .orderBy(xxlJobLogGlue.id.desc())
                .fetch();
    }

    @Transactional
    @Override
    public int removeOld(int jobId, int limit) {
        BooleanBuilder where = new BooleanBuilder();
        BooleanBuilder subWhere = new BooleanBuilder();
        subWhere.and(xxlJobLogGlue.jobId.eq(jobId));
        where.and(xxlJobLogGlue.id.notIn(JPAExpressions.select(xxlJobLogGlue.id).from(xxlJobLogGlue).where(subWhere).orderBy(xxlJobLogGlue.updateTime.desc()).limit(limit).offset(0)));
        where.and(xxlJobLogGlue.jobId.eq(jobId));
        return (int) queryFactory.delete(xxlJobLogGlue).where(where).execute();
    }
    @Transactional
    @Override
    public int deleteByJobId(int jobId) {
        return (int) queryFactory.delete(xxlJobLogGlue).where(xxlJobLogGlue.jobId.eq(jobId)).execute();
    }
}
