package com.xxl.job.admin.dao;

import com.xxl.job.admin.core.model.XxlJobGroup;

import java.util.List;

/**
 * Created by xuxueli on 16/9/30.
 */
public interface XxlJobGroupDao {

    List<XxlJobGroup> findAll();

    List<XxlJobGroup> findByAddressType(int addressType);

    int save(XxlJobGroup xxlJobGroup);


    int update(XxlJobGroup xxlJobGroup);

    int remove(int id);

    XxlJobGroup load(int id);

    List<XxlJobGroup> pageList(int offset,
                               int pagesize,
                               String appname,
                               String title);

    int pageListCount(int offset,
                      int pagesize,
                      String appname,
                      String title);

}
