package com.xxl.job.admin.core.conf;

import com.github.pagehelper.page.PageAutoDialect;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.SQLException;


@Configuration
public class MybatisConfig {

    private static Logger logger = LoggerFactory.getLogger(MybatisConfig.class);

    /**
     * 注册我们自定义的 DatabaseIdProvider
     * @return DatabaseIdProvider
     */
    @Bean
    public DatabaseIdProvider databaseIdProvider() {
        return dataSource -> {

                try (Connection conn = dataSource.getConnection()) {
                    String url = conn.getMetaData().getURL().toLowerCase();
                    return PageAutoDialect.fromJdbcUrl(url);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

        };
    }
}
