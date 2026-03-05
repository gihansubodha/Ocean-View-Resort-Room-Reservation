package com.oceanview.dao;

import com.oceanview.util.DbUtil;

import javax.sql.DataSource;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

public final class DbTestSupport {
    private DbTestSupport() {}

    public static void installDataSource(DataSource dataSource) throws Exception {
        Field field = DbUtil.class.getDeclaredField("ds");
        field.setAccessible(true);
        field.set(null, dataSource);
    }

    public static void clearDataSource() throws Exception {
        installDataSource(null);
    }

    public static DataSource dataSource(Connection connection) {
        return (DataSource) Proxy.newProxyInstance(
                DbTestSupport.class.getClassLoader(),
                new Class[]{DataSource.class},
                (proxy, method, args) -> {
                    String name = method.getName();
                    if ("getConnection".equals(name)) return connection;
                    if ("unwrap".equals(name)) return null;
                    if ("isWrapperFor".equals(name)) return false;
                    return defaultValue(method.getReturnType());
                }
        );
    }

    public static Connection connection(Map<String, StatementStub> prepared, Map<String, StatementStub> callable) {
        return (Connection) Proxy.newProxyInstance(
                DbTestSupport.class.getClassLoader(),
                new Class[]{Connection.class},
                (proxy, method, args) -> {
                    String name = method.getName();
                    if ("prepareStatement".equals(name)) {
                        String sql = (String) args[0];
                        StatementStub stub = prepared.get(sql);
                        if (stub == null) throw new AssertionError("Unexpected prepareStatement SQL: " + sql);
                        stub.sql = sql;
                        return stub.asPreparedStatement();
                    }
                    if ("prepareCall".equals(name)) {
                        String sql = (String) args[0];
                        StatementStub stub = callable.get(sql);
                        if (stub == null) throw new AssertionError("Unexpected prepareCall SQL: " + sql);
                        stub.sql = sql;
                        return stub.asCallableStatement();
                    }
                    if ("close".equals(name)) return null;
                    if ("unwrap".equals(name)) return null;
                    if ("isWrapperFor".equals(name)) return false;
                    return defaultValue(method.getReturnType());
                }
        );
    }

    public static StatementStub statement() {
        return new StatementStub();
    }

    public static ResultSet rows(List<LinkedHashMap<String, Object>> rows) {
        return (ResultSet) Proxy.newProxyInstance(
                DbTestSupport.class.getClassLoader(),
                new Class[]{ResultSet.class},
                new InvocationHandler() {
                    int cursor = -1;
                    boolean wasNull = false;

                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) {
                        String name = method.getName();
                        if ("next".equals(name)) {
                            cursor++;
                            return cursor < rows.size();
                        }
                        if ("wasNull".equals(name)) return wasNull;
                        if ("close".equals(name)) return null;
                        if (cursor < 0 || cursor >= rows.size()) {
                            return defaultValue(method.getReturnType());
                        }
                        LinkedHashMap<String, Object> row = rows.get(cursor);
                        if (name.startsWith("get")) {
                            Object key = args[0];
                            Object value;
                            if (key instanceof Integer) {
                                value = new ArrayList<>(row.values()).get(((Integer) key) - 1);
                            } else {
                                value = row.get(String.valueOf(key));
                            }
                            wasNull = value == null;
                            switch (name) {
                                case "getInt":
                                    return value == null ? 0 : ((Number) value).intValue();
                                case "getDouble":
                                    return value == null ? 0d : ((Number) value).doubleValue();
                                case "getString":
                                    return value == null ? null : String.valueOf(value);
                                case "getBigDecimal":
                                    return (BigDecimal) value;
                                case "getDate":
                                    return (java.sql.Date) value;
                                case "getTimestamp":
                                    return (Timestamp) value;
                                case "getObject":
                                    return value;
                            }
                        }
                        if ("unwrap".equals(name)) return null;
                        if ("isWrapperFor".equals(name)) return false;
                        return defaultValue(method.getReturnType());
                    }
                }
        );
    }

    public static LinkedHashMap<String, Object> row(Object... kv) {
        LinkedHashMap<String, Object> row = new LinkedHashMap<>();
        for (int i = 0; i < kv.length; i += 2) {
            row.put(String.valueOf(kv[i]), kv[i + 1]);
        }
        return row;
    }

    public static Object defaultValue(Class<?> type) {
        if (type == Void.TYPE) return null;
        if (type == Boolean.TYPE) return false;
        if (type == Byte.TYPE) return (byte) 0;
        if (type == Short.TYPE) return (short) 0;
        if (type == Integer.TYPE) return 0;
        if (type == Long.TYPE) return 0L;
        if (type == Float.TYPE) return 0f;
        if (type == Double.TYPE) return 0d;
        if (type == Character.TYPE) return '\0';
        return null;
    }

    public static final class SqlNull {
        public final int sqlType;

        public SqlNull(int sqlType) {
            this.sqlType = sqlType;
        }
    }

    public static class StatementStub {
        public String sql;
        public final Map<Integer, Object> params = new LinkedHashMap<>();
        public ResultSet queryResult = rows(Collections.emptyList());
        public ResultSet generatedKeys = rows(Collections.emptyList());
        public int updateCount = 1;
        public boolean executed;

        public Throwable onExecuteQuery;
        public Throwable onExecuteUpdate;
        public Throwable onExecute;
        public Throwable onGetGeneratedKeys;

        public PreparedStatement asPreparedStatement() {
            return (PreparedStatement) Proxy.newProxyInstance(
                    DbTestSupport.class.getClassLoader(),
                    new Class[]{PreparedStatement.class},
                    this::invoke
            );
        }

        public CallableStatement asCallableStatement() {
            return (CallableStatement) Proxy.newProxyInstance(
                    DbTestSupport.class.getClassLoader(),
                    new Class[]{CallableStatement.class},
                    this::invoke
            );
        }

        private Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String name = method.getName();
            switch (name) {
                case "setInt":
                case "setString":
                case "setDate":
                case "setBigDecimal":
                case "setObject":
                    params.put((Integer) args[0], args[1]);
                    return null;
                case "setNull":
                    params.put((Integer) args[0], new SqlNull((Integer) args[1]));
                    return null;
                case "executeQuery":
                    executed = true;
                    if (onExecuteQuery != null) throw onExecuteQuery;
                    return queryResult;
                case "executeUpdate":
                    executed = true;
                    if (onExecuteUpdate != null) throw onExecuteUpdate;
                    return updateCount;
                case "execute":
                    executed = true;
                    if (onExecute != null) throw onExecute;
                    return true;
                case "getGeneratedKeys":
                    if (onGetGeneratedKeys != null) throw onGetGeneratedKeys;
                    return generatedKeys;
                case "close":
                    return null;
                case "unwrap":
                    return null;
                case "isWrapperFor":
                    return false;
                default:
                    return defaultValue(method.getReturnType());
            }
        }
    }
}