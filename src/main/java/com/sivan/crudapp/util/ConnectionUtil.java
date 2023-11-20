package com.sivan.crudapp.util;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public final class ConnectionUtil {
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String URL = "url";
    public static final String POOL_SIZE = "pool.size";
    public static final Integer POOL_SIZE_DEFAULT = 5;
    public static BlockingQueue<Connection> connectionPool;
    public static List<Connection> realConnections;

    static {
        loadDriver();
        initConnectionPool();
    }

    private ConnectionUtil() {
    }

    public static Connection get() {
        try {
            return connectionPool.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static Connection open() {
        try {
            return DriverManager.getConnection(
                    DbPropertiesUtil.get(URL),
                    DbPropertiesUtil.get(USERNAME),
                    DbPropertiesUtil.get(PASSWORD));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void loadDriver() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void initConnectionPool() {
        String poolSize = DbPropertiesUtil.get(POOL_SIZE);
        var size = poolSize == null ? POOL_SIZE_DEFAULT : Integer.parseInt(poolSize);
        connectionPool = new ArrayBlockingQueue<>(size);
        realConnections = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            Connection connection = open();

            var proxyConnection = (Connection) Proxy.newProxyInstance(ConnectionUtil.class.getClassLoader(),
                    new Class[]{Connection.class},
                    (proxy, method, args) ->
                            method.getName().equals("close")
                                    ? connectionPool.add((Connection) proxy)
                                    : method.invoke(connection, args));

            connectionPool.add(proxyConnection);
            realConnections.add(connection);
        }
    }

    public static void closeRealPool() {
        for (Connection connection : realConnections) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
