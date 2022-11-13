package com.mwanzobaraka.models;

import com.mwanzobaraka.dbcfg.ConnectionProvider;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class Loan {

    private String loanID;
    private String contributorID;
    private Double loanAmount;
    private Double interestRate;
    private Double loanBalance;
    private Double loanInterest;
    private String loanStatus;
    private int loanPeriod;
    private Date loanDate;
    private double loanPrincipal;

    public Loan(String contributorID, Double loanAmount, Double interestRate, int loanPeriod, Date loanDate) {
        this.contributorID = contributorID;
        this.loanAmount = loanAmount;
        this.interestRate = interestRate;
        this.loanPeriod = loanPeriod;
        this.loanDate = loanDate;
    }

    public String getLoanID() {
        return loanID;
    }

    public void setLoanID(String loanID) {
        this.loanID = loanID;
    }

    public String getContributorID() {
        return contributorID;
    }

    public void setContributorID(String contributorID) {
        this.contributorID = contributorID;
    }

    public Double getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(Double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public Double getLoanBalance() {
        return loanBalance;
    }

    public void setLoanBalance(Double loanBalance) {
        this.loanBalance = loanBalance;
    }

    public Double getLoanInterest() {
        return loanInterest;
    }

    public void setLoanInterest(Double loanInterest) {
        this.loanInterest = loanInterest;
    }

    public String getLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(String loanStatus) {
        this.loanStatus = loanStatus;
    }

    public int getLoanPeriod() {
        return loanPeriod;
    }

    public void setLoanPeriod(int loanPeriod) {
        this.loanPeriod = loanPeriod;
    }

    public Date getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(Date loanDate) {
        this.loanDate = loanDate;
    }

    public double getLoanPrincipal() {
        return loanPrincipal;
    }

    public void setLoanPrincipal(double loanPrincipal) {
        this.loanPrincipal = loanPrincipal;
    }

    public Loan save() throws SQLException, IOException {
        Connection conn = ConnectionProvider.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO chamabaraka.loans VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        int min = 100, max = 999_999;
        setLoanID("LOAN" + (int) (Math.random() * (max - min + 1) + min));
        setLoanStatus("PENDING");
        setLoanInterest((getLoanAmount() * getInterestRate()*loanPeriod)/100);
        setLoanBalance(getLoanAmount() + getLoanInterest());
        setLoanPrincipal((getLoanAmount()+getLoanInterest())/getLoanPeriod());
        stmt.setString(1, loanID);
        stmt.setDouble(2, loanAmount);
        stmt.setDouble(3, interestRate);
        stmt.setDouble(4, loanBalance);
        stmt.setDouble(5, loanInterest);
        stmt.setString(6, loanStatus);
        stmt.setDate(7, loanDate);
        stmt.setString(8, String.valueOf(loanPeriod));
        stmt.setDouble(9, loanPrincipal);
        stmt.setString(10, contributorID);
        stmt.executeUpdate();
        stmt.close();
        conn.close();
        return this;
    }


    public static Loan getLoan(String loanID) throws SQLException, IOException {
        Connection conn = ConnectionProvider.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM chamabaraka.loans WHERE loanID = ?");
        stmt.setString(1, loanID);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            Loan loan = new Loan(rs.getString("contributorID"),
                    rs.getDouble("loanAmount"),
                    rs.getDouble("interestRate"), Integer.parseInt(rs.getString("loanPeriod")), rs.getDate("loanDate"));
            loan.setLoanID(rs.getString("loanID"));
            loan.setLoanBalance(rs.getDouble("loanBalance"));
            loan.setLoanInterest(rs.getDouble("loanInterest"));
            loan.setLoanStatus(rs.getString("loanStatus"));
            loan.setLoanPrincipal(rs.getDouble("principal"));
            return loan;
        }
        return null;
    }
    public static Loan getLoanByMember(String memberID){
        try {
            Connection conn = ConnectionProvider.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM chamabaraka.loans WHERE contributorID = ? AND loanStatus = 'PENDING'");
            stmt.setString(1, memberID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Loan loan = new Loan(rs.getString("contributorID"),
                        rs.getDouble("loanAmount"),
                        rs.getDouble("interestRate"), Integer.parseInt(rs.getString("loanPeriod")), rs.getDate("loanDate"));
                loan.setLoanID(rs.getString("loanID"));
                loan.setLoanBalance(rs.getDouble("loanBalance"));
                loan.setLoanInterest(rs.getDouble("loanInterest"));
                loan.setLoanStatus(rs.getString("loanStatus"));
                loan.setLoanPrincipal(rs.getDouble("principal"));
                return loan;
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public void updateLoan() {
        try {
            Connection conn = ConnectionProvider.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE chamabaraka.loans SET loanBalance = ?, loanStatus = ? WHERE loanID = ?");
            stmt.setDouble(1, getLoanBalance());
            stmt.setString(2, getLoanStatus());
            stmt.setString(3, getLoanID());
            stmt.executeUpdate();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Loan> getLoans() throws SQLException, IOException {
        Connection conn = ConnectionProvider.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM chamabaraka.loans");
        ResultSet rs = stmt.executeQuery();
        ArrayList<Loan> loans = new ArrayList<>();
        while(rs.next()){
            Loan loan = new Loan(
                    rs.getString("contributorID"), rs.getDouble("loanAmount"),
                    rs.getDouble("interestRate"), Integer.parseInt(rs.getString("loanPeriod")),
                    rs.getDate("loanDate"));
            loan.setLoanID(rs.getString("loanID"));
            loan.setLoanBalance(rs.getDouble("loanBalance"));
            loan.setLoanInterest(rs.getDouble("loanInterest"));
            loan.setLoanStatus(rs.getString("loanStatus"));
            loan.setLoanPrincipal(rs.getDouble("principal"));
            loans.add(loan);
        }
        return loans;
    }
    public static ArrayList<Loan> getUnfinishedLoans() throws SQLException, IOException {
        Connection conn = ConnectionProvider.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM chamabaraka.loans WHERE loanStatus = 'PENDING'"
        );
        ResultSet rs = stmt.executeQuery();
        ArrayList<Loan> loans = new ArrayList<>();
        while(rs.next()){
            Loan loan = new Loan(
                    rs.getString("contributorID"), rs.getDouble("loanAmount"),
                    rs.getDouble("interestRate"), Integer.parseInt(rs.getString("loanPeriod")),
                    rs.getDate("loanDate"));
            loan.setLoanID(rs.getString("loanID"));
            loan.setLoanBalance(rs.getDouble("loanBalance"));
            loan.setLoanInterest(rs.getDouble("loanInterest"));
            loan.setLoanStatus(rs.getString("loanStatus"));
            loan.setLoanPrincipal(rs.getDouble("principal"));
            loans.add(loan);
        }
        return loans;
    }

    @Override
    public String toString() {
        return "<Loan " +
                "loanID='" + loanID + '\'' +
                ", contributorID='" + contributorID + '\'' +
                ", loanAmount=" + loanAmount +
                '>';
    }
}
