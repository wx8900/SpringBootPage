package com.demo.test.abstractfactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.LinkedList;
import java.util.Properties;
import java.util.logging.Logger;

/**
 *  原生的jdbc连接池，加载jdbc.properties文件
 * 由于要频繁读写List集合，所以这里使用LinkedList集合来存放数据库链接比较合适
 */
public class JDBCPool implements DataSource {

    private static JDBCPool jdbcPool = new JDBCPool();
    private static LinkedList<Connection> listConnections = new LinkedList<>();
    private static Connection conn;

    /**
     * 在静态代码块中加载src/jdbc.properties数据库配置文件
     */
    static {
        InputStream in = JDBCPool.class.getResourceAsStream("/application-dev.properties");
        Properties prop = new Properties();
        try {
            prop.load(in);
            String driver = prop.getProperty("spring.datasource.druid.driver-class-name");
            String url = prop.getProperty("spring.datasource.druid.url");
            String username = prop.getProperty("spring.datasource.druid.username");
            String password = prop.getProperty("spring.datasource.druid.password");
            // 数据库连接池的初始化连接数大小
            int jdbcPoolInitSize = Integer.parseInt(prop.getProperty("spring.datasource.druid.initial-size"));
            // 加载数据库驱动
            //Class.forName(driver);
            IDBComponent idbComponent = new MySQLDBComponent();
            for (int i = 0; i < jdbcPoolInitSize; i++) {
                //conn = DriverManager.getConnection(url, username, password);
                IDBComponent.IConnection connection = idbComponent.getConnection();
                conn = connection.connection();
                System.out.println("获取到了连接" + conn);
                //将获取到的数据库连接加入到listConnections集合中，listConnections集合此时就是一个存放了数据库连接的连接池
                listConnections.add(conn);
            }
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private JDBCPool() {
    }

    public static JDBCPool getJdbcPoolInstance() {
        if (jdbcPool == null) {
            jdbcPool = new JDBCPool();
        }
        return jdbcPool;
    }

    public static void main(String[] args) {
        System.out.println("The size of connections are : " + listConnections.size());
    }

    /**
     * 获取数据库连接
     */
    @Override
    public Connection getConnection() throws SQLException {
        // 如果数据库连接池中的连接对象的个数大于0
        if (listConnections.size() > 0) {
            // 从listConnections集合中获取一个数据库连接
            System.out.println("listConnections数据库连接池大小是：" + listConnections.size());
            // 返回Connection对象的代理对象
            return (Connection) Proxy.newProxyInstance(JDBCPool.class.getClassLoader(), conn.getClass().getInterfaces(),
                    new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            if (!method.getName().equals("close")) {
                                System.out.println("获取到连接池连接对象......" + conn);
                                return method.invoke(conn, args);
                            } else {
                                //如果调用的是Connection对象的close方法，就把conn还给数据库连接池
                                listConnections.add(conn);
                                //System.out.println(conn+ "，还给数据库连接池了");
                                //System.out.println("listConnections数据库连接池大小为"+ listConnections.size());
                                return null;
                            }
                        }
                    });
        } else {
            throw new RuntimeException("对不起，数据库忙");
        }
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

}
