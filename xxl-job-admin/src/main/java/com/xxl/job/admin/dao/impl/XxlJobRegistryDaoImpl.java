package com.xxl.job.admin.dao.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.xxl.job.admin.core.model.QXxlJobRegistry;
import com.xxl.job.admin.core.model.XxlJobRegistry;
import com.xxl.job.admin.dao.XxlJobRegistryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Component
public class XxlJobRegistryDaoImpl implements XxlJobRegistryDao {


    public XxlJobRegistryDaoImpl(@Autowired JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
        this.jobRegistry = QXxlJobRegistry.xxlJobRegistry;
    }

    private final JPAQueryFactory queryFactory;

    private final QXxlJobRegistry jobRegistry;

    @Override
    public List<Integer> findDead(int timeout, Date nowTime) {
        Date deadTime = new Date(nowTime.getTime() - timeout * 1000L);
        return queryFactory.select(jobRegistry.id).from(jobRegistry).where(jobRegistry.updateTime.lt(deadTime)).fetch();
    }

    @Transactional
    @Override
    public int removeDead(List<Integer> ids) {
        return (int) queryFactory.delete(jobRegistry).where(jobRegistry.id.in(ids)).execute();
    }

    @Override
    public List<XxlJobRegistry> findAll(int timeout, Date nowTime) {
        Date line = new Date(nowTime.getTime() - timeout * 1000L);
        return queryFactory.selectFrom(jobRegistry).where(jobRegistry.updateTime.gt(line)).fetch();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int registrySaveOrUpdate(String registryGroup, String registryKey, String registryValue, Date updateTime) {
        XxlJobRegistry origin = queryFactory.selectFrom(jobRegistry)
                .where(jobRegistry.registryGroup.equalsIgnoreCase(registryGroup)
                        .and(jobRegistry.registryKey.equalsIgnoreCase(registryKey))
                        .and(jobRegistry.registryValue.equalsIgnoreCase(registryValue)))
                .fetchOne();
        if (origin == null) {
            return (int) queryFactory.insert(jobRegistry).columns(jobRegistry.registryGroup,
                            jobRegistry.registryKey,
                            jobRegistry.registryValue,
                            jobRegistry.updateTime
                    )
                    .values(
                            registryGroup, registryKey, registryValue, updateTime
                    ).execute();
        } else {
            return (int) queryFactory.update(jobRegistry)
                    .set(jobRegistry.updateTime, origin.getUpdateTime())
                    .where(jobRegistry.id.eq(origin.getId()))
                    .execute();
        }

    }

    @Transactional
    @Override
    public int registryDelete(String registryGroup, String registryKey, String registryValue) {
        return (int) queryFactory.delete(jobRegistry)
                .where(jobRegistry.registryGroup.equalsIgnoreCase(registryGroup)
                        .and(jobRegistry.registryKey.equalsIgnoreCase(registryKey))
                        .and(jobRegistry.registryValue.equalsIgnoreCase(registryValue)))
                .execute();
    }
}
