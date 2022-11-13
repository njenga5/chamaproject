package com.mwanzobaraka.dbcfg;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class ConnectionProvider {
    private static Connection conn;
    public static Connection getConnection() throws IOException, SQLException {
        InputStream stream = ConnectionProvider.class.getResourceAsStream("config.properties");
        Properties props = new Properties();
        props.load(stream);
        String url = props.getProperty("db_url");
        String user = props.getProperty("db_user");
        String pass = props.getProperty("db_password");
        if(conn == null || conn.isClosed()) {
            conn = DriverManager.getConnection(url, user, pass);
        }
        return conn;
    }
}
