package com.mwanzobaraka.models;

import com.mwanzobaraka.dbcfg.ConnectionProvider;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;

public class Invoice {
    private String invoiceID;
    private String loanID;
    private Double invoiceAmount;
    private Date invoiceDate;
    private String invoiceStatus;
    private Date dueDate;

    public Invoice(String loanID, Double invoiceAmount) {
        this.loanID = loanID;
        this.invoiceAmount = invoiceAmount;
    }

    public String getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(String invoiceID) {
        this.invoiceID = invoiceID;
    }

    public String getLoanID() {
        return loanID;
    }

    public void setLoanID(String loanID) {
        this.loanID = loanID;
    }

    public Double getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(Double invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(String invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public void save() throws SQLException, IOException {
        Connection conn = ConnectionProvider.getConnection();
        String sql = "INSERT INTO chamabaraka.invoices VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        int min = 100, max = 100_000;
        setInvoiceID("INV" + (int)Math.floor(Math.random() * (max - min + 1) + min));
        setInvoiceDate(new Date(System.currentTimeMillis()));
        setInvoiceStatus("UNPAID");
        setDueDate(Date.valueOf(LocalDate.now().plusMonths(1).with(TemporalAdjusters.lastDayOfMonth())));
        stmt.setString(1, invoiceID);
        stmt.setDouble(2, invoiceAmount);
        stmt.setDate(3, invoiceDate);
        stmt.setString(4, invoiceStatus);
        stmt.setDate(5, dueDate);
        stmt.setString(6, loanID);
        stmt.executeUpdate();
        stmt.close();
        conn.close();

    }

    public static Invoice getCurrentInvoice(String loanID) throws SQLException, IOException {
        Connection conn = ConnectionProvider.getConnection();
        String date = Date.valueOf(LocalDate.now().with(TemporalAdjusters.lastDayOfMonth())).toString();
        String sql = "SELECT * FROM chamabaraka.invoices WHERE loanID = ? AND dueDate = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, loanID);
        stmt.setString(2, date);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()){
            Invoice invoice = new Invoice(rs.getString("loanID"), rs.getDouble("invoiceAmount"));
            invoice.setInvoiceID(rs.getString("invoiceID"));
            invoice.setInvoiceDate(rs.getDate("invoiceDate"));
            invoice.setInvoiceStatus(rs.getString("invoiceStatus"));
            invoice.setDueDate(rs.getDate("dueDate"));
            return invoice;
        }
        return null;
    }
    public static Invoice getInvoice(String loanID) throws SQLException, IOException {
        Connection conn = ConnectionProvider.getConnection();
        String start = Date.valueOf(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth())).toString();
        String end = Date.valueOf(LocalDate.now().with(TemporalAdjusters.lastDayOfMonth())).toString();
        String sql = "SELECT * FROM chamabaraka.invoices WHERE loanID = ? AND invoiceDate BETWEEN ? AND ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, loanID);
        stmt.setString(2, start);
        stmt.setString(3, end);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()){
            Invoice invoice = new Invoice(rs.getString("loanID"), rs.getDouble("invoiceAmount"));
            invoice.setInvoiceID(rs.getString("invoiceID"));
            invoice.setInvoiceDate(rs.getDate("invoiceDate"));
            invoice.setInvoiceStatus(rs.getString("invoiceStatus"));
            invoice.setDueDate(rs.getDate("dueDate"));
            return invoice;
        }
        return null;
        }

    public static ArrayList<Invoice> getUnpaidInvoices() throws SQLException, IOException {
        Connection conn = ConnectionProvider.getConnection();
        String date = Date.valueOf(LocalDate.now().with(TemporalAdjusters.lastDayOfMonth())).toString();
        String sql = "SELECT * FROM chamabaraka.invoices WHERE invoiceStatus = 'UNPAID' AND dueDate < ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, date);
        ResultSet rs = stmt.executeQuery();
        ArrayList<Invoice> invoices = new ArrayList<>();
        while (rs.next()){
            Invoice invoice = new Invoice(rs.getString("loanID"), rs.getDouble("invoiceAmount"));
            invoice.setInvoiceID(rs.getString("invoiceID"));
            invoice.setInvoiceDate(rs.getDate("invoiceDate"));
            invoice.setInvoiceStatus(rs.getString("invoiceStatus"));
            invoice.setDueDate(rs.getDate("dueDate"));
            invoices.add(invoice);
        }
        return invoices;

    }

    @Override
    public String toString() {
        return "<Invoice " +
                "invoiceID='" + invoiceID + '\'' +
                ", invoiceAmount=" + invoiceAmount +
                ", dueDate=" + dueDate +
                '>';
    }
}
