package com.demo.test.abstractfactory;

import java.io.InputStream;
import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * @author Jack
 */
public class SQLServerDBComponent implements IDBComponent {

    private static final int jdbcPoolInitSize;
    private static String driver, url, username, password;

    static {
        InputStream in = JDBCPool.class.getResourceAsStream("/application-dev.properties");
        Properties prop = new Properties();
        try {
            prop.load(in);
            driver = prop.getProperty("spring.datasource.druid.driver-class-name");
            url = prop.getProperty("spring.datasource.druid.url");
            username = prop.getProperty("spring.datasource.druid.username");
            password = prop.getProperty("spring.datasource.druid.password");
            // 数据库连接池的初始化连接数大小
            jdbcPoolInitSize = Integer.parseInt(prop.getProperty("spring.datasource.druid.initial-size"));
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    @Override
    public IDrive getDrive() {
        return new SQLServerDrive();
    }

    @Override
    public IConnection getConnection() {
        return new SQLServerConnection();
    }

    @Override
    public ICommand getCommand() {
        return new SQLServerCommand();
    }

    class SQLServerDrive implements IDrive {
        @Override
        public void drive() {
            System.out.println("注册数据库驱动......SQLServer: drive.");
            try {
                Class.forName(driver);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("注册数据库驱动出现异常:" + e.getMessage());
            }
        }
    }

    class SQLServerConnection implements IConnection {
        @Override
        public Connection connection() {
            System.out.println("SQLServer: connect.");
            return new Connection() {
                @Override
                public Statement createStatement() throws SQLException {
                    return null;
                }

                @Override
                public PreparedStatement prepareStatement(String sql) throws SQLException {
                    return null;
                }

                @Override
                public CallableStatement prepareCall(String sql) throws SQLException {
                    return null;
                }

                @Override
                public String nativeSQL(String sql) throws SQLException {
                    return null;
                }

                @Override
                public boolean getAutoCommit() throws SQLException {
                    return false;
                }

                @Override
                public void setAutoCommit(boolean autoCommit) throws SQLException {

                }

                @Override
                public void commit() throws SQLException {

                }

                @Override
                public void rollback() throws SQLException {

                }

                @Override
                public void close() throws SQLException {

                }

                @Override
                public boolean isClosed() throws SQLException {
                    return false;
                }

                @Override
                public DatabaseMetaData getMetaData() throws SQLException {
                    return null;
                }

                @Override
                public boolean isReadOnly() throws SQLException {
                    return false;
                }

                @Override
                public void setReadOnly(boolean readOnly) throws SQLException {

                }

                @Override
                public String getCatalog() throws SQLException {
                    return null;
                }

                @Override
                public void setCatalog(String catalog) throws SQLException {

                }

                @Override
                public int getTransactionIsolation() throws SQLException {
                    return 0;
                }

                @Override
                public void setTransactionIsolation(int level) throws SQLException {

                }

                @Override
                public SQLWarning getWarnings() throws SQLException {
                    return null;
                }

                @Override
                public void clearWarnings() throws SQLException {

                }

                @Override
                public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
                    return null;
                }

                @Override
                public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
                    return null;
                }

                @Override
                public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
                    return null;
                }

                @Override
                public Map<String, Class<?>> getTypeMap() throws SQLException {
                    return null;
                }

                @Override
                public void setTypeMap(Map<String, Class<?>> map) throws SQLException {

                }

                @Override
                public int getHoldability() throws SQLException {
                    return 0;
                }

                @Override
                public void setHoldability(int holdability) throws SQLException {

                }

                @Override
                public Savepoint setSavepoint() throws SQLException {
                    return null;
                }

                @Override
                public Savepoint setSavepoint(String name) throws SQLException {
                    return null;
                }

                @Override
                public void rollback(Savepoint savepoint) throws SQLException {

                }

                @Override
                public void releaseSavepoint(Savepoint savepoint) throws SQLException {

                }

                @Override
                public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
                    return null;
                }

                @Override
                public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
                    return null;
                }

                @Override
                public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
                    return null;
                }

                @Override
                public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
                    return null;
                }

                @Override
                public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
                    return null;
                }

                @Override
                public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
                    return null;
                }

                @Override
                public Clob createClob() throws SQLException {
                    return null;
                }

                @Override
                public Blob createBlob() throws SQLException {
                    return null;
                }

                @Override
                public NClob createNClob() throws SQLException {
                    return null;
                }

                @Override
                public SQLXML createSQLXML() throws SQLException {
                    return null;
                }

                @Override
                public boolean isValid(int timeout) throws SQLException {
                    return false;
                }

                @Override
                public void setClientInfo(String name, String value) throws SQLClientInfoException {

                }

                @Override
                public String getClientInfo(String name) throws SQLException {
                    return null;
                }

                @Override
                public Properties getClientInfo() throws SQLException {
                    return null;
                }

                @Override
                public void setClientInfo(Properties properties) throws SQLClientInfoException {

                }

                @Override
                public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
                    return null;
                }

                @Override
                public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
                    return null;
                }

                @Override
                public String getSchema() throws SQLException {
                    return null;
                }

                @Override
                public void setSchema(String schema) throws SQLException {

                }

                @Override
                public void abort(Executor executor) throws SQLException {

                }

                @Override
                public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {

                }

                @Override
                public int getNetworkTimeout() throws SQLException {
                    return 0;
                }

                @Override
                public <T> T unwrap(Class<T> iface) throws SQLException {
                    return null;
                }

                @Override
                public boolean isWrapperFor(Class<?> iface) throws SQLException {
                    return false;
                }
            };
        }
    }

    class SQLServerCommand implements ICommand {
        @Override
        public void command() {
            System.out.println("SQLServer: command.");
        }
    }

}
