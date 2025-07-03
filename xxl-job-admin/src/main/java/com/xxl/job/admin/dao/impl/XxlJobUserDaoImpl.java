package com.xxl.job.admin.dao.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.xxl.job.admin.core.model.QXxlJobUser;
import com.xxl.job.admin.core.model.XxlJobUser;
import com.xxl.job.admin.dao.XxlJobUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;


@Component
public class XxlJobUserDaoImpl implements XxlJobUserDao {


    public XxlJobUserDaoImpl(@Autowired JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
        this.jobUser = QXxlJobUser.xxlJobUser;
    }

    private final JPAQueryFactory queryFactory;
    private final QXxlJobUser jobUser;


    @Override
    public List<XxlJobUser> pageList(int offset, int pagesize, String username, int role) {
        BooleanBuilder where = new BooleanBuilder();
        if (StringUtils.hasText(username)) {
            where.and(QXxlJobUser.xxlJobUser.username.containsIgnoreCase(username));
        }
        if (role > -1) {
            where.and(jobUser.role.eq(role));
        }
        return queryFactory.selectFrom(jobUser).where(where)
                .orderBy(jobUser.username.asc())
                .offset(offset).limit(pagesize)
                .fetch();
    }

    @Override
    public int pageListCount(int offset, int pagesize, String username, int role) {
        BooleanBuilder where = new BooleanBuilder();
        if (StringUtils.hasText(username)) {
            where.and(QXxlJobUser.xxlJobUser.username.containsIgnoreCase(username));
        }
        if (role > -1) {
            where.and(jobUser.role.eq(role));
        }
        Long count = queryFactory.select(jobUser.count()).from(jobUser)
                .where(where).fetchOne();
        return count == null ? 0 : count.intValue();
    }

    @Override
    public XxlJobUser loadByUserName(String username) {
        return queryFactory.selectFrom(jobUser)
                .where(jobUser.username.equalsIgnoreCase(username))
                .fetchOne();
    }

    @Transactional
    @Override
    public int save(XxlJobUser xxlJobUser) {
        return (int) queryFactory.insert(jobUser)
                .columns(jobUser.username, jobUser.password, jobUser, jobUser.role, jobUser.permission)
                .values(xxlJobUser.getUsername(), xxlJobUser.getPassword(), xxlJobUser.getRole(), xxlJobUser.getPermission())
                .execute();
    }

    @Transactional
    @Override
    public int update(XxlJobUser xxlJobUser) {

        var update = queryFactory.update(jobUser);
        if (StringUtils.hasText(xxlJobUser.getPassword())) {
            update.set(jobUser.password, xxlJobUser.getPassword());
        }
        return (int) update.set(jobUser.role, xxlJobUser.getRole())
                .set(jobUser.permission, xxlJobUser.getPermission())
                .where(jobUser.id.eq(xxlJobUser.getId())).execute();
    }

    @Transactional
    @Override
    public int delete(int id) {
        return (int) queryFactory.delete(jobUser).where(jobUser.id.eq(id)).execute();
    }
}
