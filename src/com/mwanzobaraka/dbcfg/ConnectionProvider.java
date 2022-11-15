package com.mwanzobaraka.dbcfg;

import java.io.*;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Properties;

public class ConnectionProvider {
    private static Connection conn;
    public static Connection getConnection() throws IOException, SQLException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\John\\OneDrive\\Desktop\\1.txt"));
        writer.write(Paths.get("").toAbsolutePath().toString());
        writer.close();
        BufferedReader reader = new BufferedReader(new FileReader(Paths.get("").toAbsolutePath()+"/config/config.properties"));
        Properties props = new Properties();
        props.load(reader);
        String url = props.getProperty("db_url");
        String user = props.getProperty("db_user");
        String pass = props.getProperty("db_password");
        if(conn == null || conn.isClosed()) {
            conn = DriverManager.getConnection(url, user, pass);
        }
        return conn;
    }
}
