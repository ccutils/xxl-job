package com.xxl.job.admin.dao.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.xxl.job.admin.core.model.QXxlJobGroup;
import com.xxl.job.admin.core.model.XxlJobGroup;
import com.xxl.job.admin.dao.XxlJobGroupDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import static com.xxl.job.admin.core.model.QXxlJobGroup.xxlJobGroup;

import java.util.List;


@Component
public class XxlJobGroupDaoImpl implements XxlJobGroupDao {

    public XxlJobGroupDaoImpl(@Autowired JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    private final JPAQueryFactory queryFactory;

    @Override
    public List<XxlJobGroup> findAll() {
        return queryFactory.selectFrom(xxlJobGroup).fetch();
    }

    @Override
    public List<XxlJobGroup> findByAddressType(int addressType) {
        return queryFactory.selectFrom(xxlJobGroup)
                .where(xxlJobGroup.addressType.eq(addressType))
                .orderBy(xxlJobGroup.appname.asc(), xxlJobGroup.title.asc(), xxlJobGroup.id.asc()).fetch();
    }

    @Override
    @Transactional
    public int save(XxlJobGroup xxlJobGroupValue) {
        return (int) queryFactory.insert(QXxlJobGroup.xxlJobGroup)
                .columns(
                        xxlJobGroup.appname,
                        xxlJobGroup.title,
                        xxlJobGroup.addressType,
                        xxlJobGroup.addressList,
                        xxlJobGroup.updateTime
                )
                .values(
                        xxlJobGroupValue.getAppname(),
                        xxlJobGroupValue.getTitle(),
                        xxlJobGroupValue.getAddressType(),
                        xxlJobGroupValue.getAddressList(),
                        xxlJobGroupValue.getUpdateTime()
                )
                .execute();

    }

    @Override
    @Transactional
    public int update(XxlJobGroup xxlJobGroupValue) {
        return (int) queryFactory.update(xxlJobGroup)
                .set(xxlJobGroup.appname, xxlJobGroupValue.getAppname())
                .set(xxlJobGroup.title, xxlJobGroupValue.getTitle())
                .set(xxlJobGroup.addressType, xxlJobGroupValue.getAddressType())
                .set(xxlJobGroup.addressList, xxlJobGroupValue.getAddressList())
                .set(xxlJobGroup.updateTime, xxlJobGroupValue.getUpdateTime())
                .where(xxlJobGroup.id.eq(xxlJobGroupValue.getId()))
                .execute();
    }

    @Override
    @Transactional
    public int remove(int id) {
        return (int) queryFactory.delete(xxlJobGroup).where(xxlJobGroup.id.eq(id)).execute();
    }

    @Override
    public XxlJobGroup load(int id) {
        return queryFactory.selectFrom(xxlJobGroup).where(xxlJobGroup.id.eq(id)).fetchOne();
    }

    @Override
    public List<XxlJobGroup> pageList(int offset, int pagesize, String appname, String title) {
        BooleanExpression where = null;
        if (StringUtils.hasText(appname)) {
            where = xxlJobGroup.appname.containsIgnoreCase(appname);
        }
        if (StringUtils.hasText(title)) {
            where = xxlJobGroup.title.containsIgnoreCase(title).and(where);
        }
        return queryFactory.selectFrom(xxlJobGroup).where(where).offset(offset).limit(pagesize).fetch();
    }

    @Override
    public int pageListCount(int offset, int pagesize, String appname, String title) {
        BooleanExpression where = null;
        if (StringUtils.hasText(appname)) {
            where = xxlJobGroup.appname.containsIgnoreCase(appname);
        }
        if (StringUtils.hasText(title)) {
            where = xxlJobGroup.title.containsIgnoreCase(title).and(where);
        }
        Long count = queryFactory.from(xxlJobGroup).select(xxlJobGroup.count()).where(where).fetchOne();
        return count == null ? 0 : count.intValue();
    }
}
