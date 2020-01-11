package com.demo.test.abstractfactory;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * @author Jack
 */
public class MySQLDBComponent implements IDBComponent {
    private static final String SQL_QUERY = "SELECT * FROM tbl_student";
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

    private static void getResult(ResultSet resultSet) throws SQLException {
        System.out.println("遍历结果集......");
        while (resultSet.next()) {
            int id = resultSet.getInt("ID");
            String name = resultSet.getString("NAME");
            String password = resultSet.getString("PASSWORD");
            String branch = resultSet.getString("BRANCH");
            String percentage = resultSet.getString("PERCENTAGE");
            String phone = resultSet.getString("PHONE");
            String email = resultSet.getString("EMAIL");
            System.out.println(id
                    + " , " + name
                    + " , " + password
                    + " , " + branch
                    + " , " + percentage
                    + " , " + phone
                    + " , " + email
            );
        }
    }

    private static void releaseResource(Connection connection) {
        releaseResource(null, null, connection);
    }

    private static void releaseResource(ResultSet resultSet, Statement statement, Connection connection) {
        System.out.println("释放资源......");
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException sqlEx) {
            } finally {
                resultSet = null;
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException sqlEx) {
            } finally {
                statement = null;
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception e) {
            } finally {
                connection = null;
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("MySQL: connect.");

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        MySQLDBComponent mySQLDBComponent = new MySQLDBComponent();
        try {
            mySQLDBComponent.getDrive();
            connection = mySQLDBComponent.getConnect();
            statement = mySQLDBComponent.createStatement(connection);
            resultSet = mySQLDBComponent.exeQuery(statement, SQL_QUERY);
            getResult(resultSet);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            releaseResource(resultSet, statement, connection);
        }
    }

    @Override
    public IDrive getDrive() {
        return new MySQLDrive();
    }

    @Override
    public IConnection getConnection() {
        return new MySQLConnection();
    }

    @Override
    public ICommand getCommand() {
        return new MySQLCommand();
    }

    private Connection getConnect() {
        System.out.println("建立连接......");
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException ex1) {
            throw new RuntimeException("无法获取连接,原因:" + ex1.getMessage());
        }
    }

    private Statement createStatement(Connection connection) {
        System.out.println("创建statement对象......");
        try {
            return connection.prepareStatement(SQL_QUERY);
        } catch (SQLException ex1) {
            throw new RuntimeException("无法创建语句对象,原因:" + ex1.getMessage());
        }
    }

    private ResultSet exeQuery(Statement statement, String sql) {
        System.out.println("执行sql......");
        try {
            return statement.executeQuery(sql);
        } catch (SQLException ex1) {
            throw new RuntimeException("无法执行SQL语句,原因:" + ex1.getMessage());
        }
    }

    class MySQLDrive implements IDrive {
        @Override
        public void drive() {
            System.out.println("注册数据库驱动......MySQL: drive.");
            try {
                Class.forName(driver);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("注册数据库驱动出现异常:" + e.getMessage());
            }
        }
    }

    class MySQLConnection implements IConnection {
        @Override
        public Connection connection() {
            Connection connection = null;
            System.out.println("MySQL: connect.");
            try {
                getDrive();
                return getConnect();
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                //releaseResource(connection);  // Pool
            }
            return null;
        }
    }

    class MySQLCommand implements ICommand {
        @Override
        public void command() {
            System.out.println("MySQL: command.");
        }
    }
}


