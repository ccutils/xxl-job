package com.xxl.job.admin.dao;

import com.xxl.job.admin.core.model.XxlJobUser;

import java.util.List;

/**
 * @author xuxueli 2019-05-04 16:44:59
 */

public interface XxlJobUserDao {

    List<XxlJobUser> pageList(int offset,
                              int pagesize,
                              String username,
                              int role);

    int pageListCount(int offset,
                      int pagesize,
                      String username,
                      int role);

    XxlJobUser loadByUserName(String username);

    int save(XxlJobUser xxlJobUser);

    int update(XxlJobUser xxlJobUser);

    int delete(int id);

}
