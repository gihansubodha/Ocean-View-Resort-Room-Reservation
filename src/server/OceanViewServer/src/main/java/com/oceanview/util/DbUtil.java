package com.oceanview.util;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;

public final class DbUtil {

    private static final DbUtil INSTANCE = new DbUtil();

    // Keep this exact field name for tests
    private static DataSource ds;

    private DbUtil() {
    }

    private static DbUtil getInstance() {
        return INSTANCE;
    }

    private DataSource resolveDataSource() {
        if (ds != null) {
            return ds;
        }

        try {
            InitialContext ctx = new InitialContext();
            ds = (DataSource) ctx.lookup("java:comp/env/jdbc/ocean_view_resort");
            return ds;
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize DataSource.", e);
        }
    }

    public static Connection getConnection() throws Exception {
        return getInstance().resolveDataSource().getConnection();
    }
}