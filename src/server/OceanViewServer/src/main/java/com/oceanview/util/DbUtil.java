package com.oceanview.util;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;

public final class DbUtil {

    private static final DbUtil INSTANCE = new DbUtil();
    private final DataSource dataSource;

    private DbUtil() {
        try {
            InitialContext ctx = new InitialContext();
            this.dataSource = (DataSource) ctx.lookup("java:comp/env/jdbc/ocean_view_resort");
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize DataSource.", e);
        }
    }

    public static DbUtil getInstance() {
        return INSTANCE;
    }

    public static Connection getConnection() throws Exception {
        return getInstance().dataSource.getConnection();
    }
}