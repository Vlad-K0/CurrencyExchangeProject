package org.example.currencyexchangeproject.ConnectionManager;

import org.example.currencyexchangeproject.util.YamlUtil;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ConnectionPool {
    private static final String PASSWORD_KEY = "/database/password";
    private static final String USERNAME_KEY = "/database/user";
    private static final String URL_KEY = "/database/url";
    private static final String POOL_SiZE_KEY = "/database/pool/size";
    private static final Integer DEFAULT_POOL_SIZE = 10;
    private static BlockingQueue<Connection> proxyPool;
    private static List<Connection> pool;

    static {
        loadDriver();
        initConnectionPool();
    }

    private static void initConnectionPool() {
        String poolSize = YamlUtil.get(POOL_SiZE_KEY);
        int size = poolSize == null ? DEFAULT_POOL_SIZE : Integer.parseInt(poolSize);
        proxyPool = new ArrayBlockingQueue<>(size);
        pool = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            Connection connection = open();
            pool.add(connection);
            Connection proxyConnection = (Connection) Proxy.newProxyInstance(ConnectionPool.class.getClassLoader(), new Class[]{Connection.class},
                    (proxy, method, args) -> method.getName().equals("close") ? proxyPool.add(connection) : method.invoke(connection, args));
            proxyPool.add(proxyConnection);
        }
    }

    private static Connection open() {
        try {
            return DriverManager.getConnection(
                    YamlUtil.get(URL_KEY),
                    YamlUtil.get(USERNAME_KEY),
                    YamlUtil.get(PASSWORD_KEY)
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void loadDriver() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public ConnectionPool() {

    }

    public static Connection getConnection() {
        try {
            return proxyPool.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public static void closePool() {
        for  (Connection connection : pool) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
