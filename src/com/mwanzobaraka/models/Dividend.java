package com.mwanzobaraka.models;

import com.mwanzobaraka.dbcfg.ConnectionProvider;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Dividend {
    private String memberID;
    private double dividendAmount;
    private Date datePaid;
    private String dividendID;

    public Dividend(String memberID, double dividendAmount) {
        this.memberID = memberID;
        this.dividendAmount = dividendAmount;
    }

    public String getDividendID() {
        return dividendID;
    }

    public void setDividendID(String dividendID) {
        this.dividendID = dividendID;
    }

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public double getDividendAmount() {
        return dividendAmount;
    }

    public void setDividendAmount(double dividendAmount) {
        this.dividendAmount = dividendAmount;
    }

    public Date getDatePaid() {
        return datePaid;
    }

    public void setDatePaid(Date datePaid) {
        this.datePaid = datePaid;
    }

    public void save() throws SQLException, IOException {
        Connection conn = ConnectionProvider.getConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO chamabaraka.dividends VALUES (?, ?, ?, ?)");
        int min = 100, max = 10000;
        setDatePaid(new Date(System.currentTimeMillis()));
        setDividendID("DIV" + (int) (Math.random() * (max - min + 1) + min));
        stmt.setString(1, this.dividendID);
        stmt.setDouble(2, this.dividendAmount);
        stmt.setDate(3, this.datePaid);
        stmt.setString(4, this.memberID);
        stmt.executeUpdate();
        stmt.close();
        conn.close();
    }
    public static Dividend getMemberDividend(String memberID) throws SQLException, IOException {
        Connection conn = ConnectionProvider.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM chamabaraka.dividends WHERE memberID = ?");
        stmt.setString(1, memberID);
        if (stmt.executeQuery().next()) {
            Dividend dividend = new Dividend(stmt.getResultSet().getString(4), stmt.getResultSet().getDouble(2));
            dividend.setDividendID(stmt.getResultSet().getString(1));
            dividend.setDatePaid(stmt.getResultSet().getDate(3));
            return dividend;
        }
        return null;
    }
    public static boolean dividendsPaid() throws SQLException, IOException {
        Connection conn = ConnectionProvider.getConnection();
        String date = new Date(System.currentTimeMillis()).toString();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM chamabaraka.dividends WHERE dividendDate = ?");
        stmt.setString(1, date);
        return stmt.executeQuery().next();
    }
}

