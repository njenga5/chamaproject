package com.mwanzobaraka.models;

import com.mwanzobaraka.dbcfg.ConnectionProvider;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class Penalty {

    private String penaltyID;
    private Double penaltyAmount;
    private Date penaltyDate;
    private String loanID;

    public Penalty(String loanID, Double penaltyAmount) {
        this.loanID = loanID;
        this.penaltyAmount = penaltyAmount;
    }

    public String getPenaltyID() {
        return penaltyID;
    }

    public void setPenaltyID(String penaltyID) {
        this.penaltyID = penaltyID;
    }

    public String getLoanID() {
        return loanID;
    }

    public void setLoanID(String loanID) {
        this.loanID = loanID;
    }

    public Double getPenaltyAmount() {
        return penaltyAmount;
    }

    public void setPenaltyAmount(Double penaltyAmount) {
        this.penaltyAmount = penaltyAmount;
    }

    public Date getPenaltyDate() {
        return penaltyDate;
    }

    public void setPenaltyDate(Date penaltyDate) {
        this.penaltyDate = penaltyDate;
    }

    public void save() throws SQLException, IOException {
        Connection conn = ConnectionProvider.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO chamabaraka.penalties  VALUES (?, ?, ?, ?)"
        );
        int min = 10, max = 10000;
        setPenaltyID("PEN" + (int) (Math.random() * (max - min + 1) + min));
        setPenaltyDate(new Date(System.currentTimeMillis()));
        stmt.setString(1, penaltyID);
        stmt.setDouble(2, penaltyAmount);
        stmt.setDate(3, penaltyDate);
        stmt.setString(4, loanID);
        stmt.executeUpdate();
        stmt.close();
        conn.close();
    }

    public double getTotalPenaltyAmount() throws SQLException, IOException {
        Connection conn = ConnectionProvider.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
                "SELECT SUM(penaltyAmount) FROM chamabaraka.penalties"
        );
         ResultSet rs  = stmt.executeQuery();
         double total  = 0.0;
         if (rs.next()){
             total = rs.getDouble(1);
         }
         return total;
    }
    public static ArrayList<Penalty> getPenalties() throws SQLException, IOException {
        ArrayList<Penalty> penalties = new ArrayList<>();
        Connection conn = ConnectionProvider.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM chamabaraka.penalties"
            );
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Penalty penalty = new Penalty(
                        rs.getString(3),
                        rs.getDouble(2)
                );
                penalty.setPenaltyID(rs.getString(1));
                penalties.add(penalty);
            }
            return penalties;

    }

    public static boolean alreadyPenalised(String loanID) throws SQLException, IOException {
        Connection conn = ConnectionProvider.getConnection();
        String date = new Date(System.currentTimeMillis()).toString();
        PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM chamabaraka.penalties WHERE loanID = ? AND penaltyDate = ?"
        );
        stmt.setString(1, loanID);
        stmt.setString(2, date);
        return stmt.executeQuery().next();
    }

    @Override
    public String toString() {
        return "<Penalty " +
                "loanID='" + loanID + '\'' +
                ", penaltyAmount='" + penaltyAmount + '\'' +
                '>';
    }
}
