package com.oceanview.util;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;

public final class DbUtil {
    private static DataSource ds;

    private DbUtil() {}

    public static Connection getConnection() throws Exception {
        if (ds == null) {
            InitialContext ctx = new InitialContext();
            ds = (DataSource) ctx.lookup("java:comp/env/jdbc/ocean_view_resort");
        }
        return ds.getConnection();
    }
}
