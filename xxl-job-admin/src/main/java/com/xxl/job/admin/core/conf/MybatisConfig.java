package com.xxl.job.admin.core.conf;

import com.github.pagehelper.page.PageAutoDialect;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;


@Configuration
public class MybatisConfig {


    /**
     * 注册我们自定义的 DatabaseIdProvider
     * @return DatabaseIdProvider
     */
    @Bean
    public DatabaseIdProvider databaseIdProvider() {
        return new DatabaseIdProvider() {


            private ConcurrentHashMap<DataSource, String> databaseIdMap = new ConcurrentHashMap<>();


            @Override
            public String getDatabaseId(DataSource dataSource) throws SQLException {
                if (dataSource == null) {
                    throw new NullPointerException("dataSource cannot be null");
                }
                return databaseIdMap.computeIfAbsent(dataSource, src -> {
                    try (Connection conn = src.getConnection()) {
                        String url = conn.getMetaData().getURL().toLowerCase();
                        return PageAutoDialect.fromJdbcUrl(url);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        };
    }
}
