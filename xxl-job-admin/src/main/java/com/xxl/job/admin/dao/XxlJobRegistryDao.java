package com.xxl.job.admin.dao;

import com.xxl.job.admin.core.model.XxlJobRegistry;


import java.util.Date;
import java.util.List;

/**
 * Created by xuxueli on 16/9/30.
 */
public interface XxlJobRegistryDao {

    List<Integer> findDead( int timeout,
                                   Date nowTime);

    int removeDead( List<Integer> ids);

    List<XxlJobRegistry> findAll( int timeout,
                                         Date nowTime);

    int registrySaveOrUpdate( String registryGroup,
                             String registryKey,
                             String registryValue,
                             Date updateTime);


    int registryDelete( String registryGroup,
                           String registryKey,
                           String registryValue);

}
