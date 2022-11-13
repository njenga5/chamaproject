package com.mwanzobaraka.models;

import com.mwanzobaraka.dbcfg.ConnectionProvider;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class Repayment {
    private String repaymentID;
    private String invoiceID;
    private Double repaymentAmount;
    private Double interest;
    private Date paymentDate;
    public Repayment(String invoiceID, Double repaymentAmount, Date paymentDate, double interest) {
        this.invoiceID = invoiceID;
        this.repaymentAmount = repaymentAmount;
        this.paymentDate = paymentDate;
        this.interest = interest;
    }

    public String getRepaymentID() {
        return repaymentID;
    }

    public void setRepaymentID(String repaymentID) {
        this.repaymentID = repaymentID;
    }

    public String getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(String invoiceID) {
        this.invoiceID = invoiceID;
    }

    public Double getRepaymentAmount() {
        return repaymentAmount;
    }

    public void setRepaymentAmount(Double repaymentAmount) {
        this.repaymentAmount = repaymentAmount;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Double getInterest() {
        return interest;
    }

    public void setInterest(Double interest) {
        this.interest = interest;
    }

    public Repayment save() throws SQLException, IOException {
        Connection conn = ConnectionProvider.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO chamabaraka.repayments VALUES(?, ?, ?, ?, ?, ?, ?, ?)");

        int min = 100, max = 100_000;
        setRepaymentID("REPAY" + (int) (Math.random() * (max - min + 1) + min));
        stmt.setString(1, this.repaymentID);
        stmt.setString(2, this.invoiceID);
        stmt.setDouble(3, this.repaymentAmount);
        stmt.setDate(4, this.paymentDate);
        stmt.setDouble(5, this.interest);
        stmt.executeUpdate();
        stmt.close();
        conn.close();
        return this;
        }

    public static ArrayList<Repayment> getRepayments() throws SQLException, IOException {
        Connection conn = ConnectionProvider.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM chamabaraka.repayments");
        ResultSet rs = stmt.executeQuery();
        ArrayList<Repayment> repayments = new ArrayList<>();
        while(rs.next()){
            Repayment repayment = new Repayment(rs.getString("invoiceID"), rs.getDouble("repaymentAmount"),
                    rs.getDate("repaymentDate"), rs.getDouble("interest"));
            repayment.setRepaymentID(rs.getString("repaymentID"));
            repayments.add(repayment);
        }
        return repayments;
    }


    @Override
    public String toString() {
        return "<Repayment " +
                "invoiceID='" + invoiceID + '\'' +
                ", repaymentAmount=" + repaymentAmount +
                '>';
    }
}

