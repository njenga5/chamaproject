package com.mwanzobaraka.models;

import com.mwanzobaraka.dbcfg.ConnectionProvider;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class Contribution {
    private String contributorID;
    private String contributionID;
    private Date contributionDate;
    private Double contributionAmount;
    private Double groupShare;
    private String groupID;

    public Contribution(String contributorID, Date contributionDate, Double contributionAmount, Double groupShare, String groupID) {
        this.contributorID = contributorID;
        this.contributionDate = contributionDate;
        this.contributionAmount = contributionAmount;
        this.groupShare = groupShare;
        this.groupID = groupID;
    }

    public String getContributorID() {
        return contributorID;
    }

    public void setContributorID(String contributorID) {
        this.contributorID = contributorID;
    }

    public String getContributionID() {
        return contributionID;
    }

    public void setContributionID(String contributionID) {
        this.contributionID = contributionID;
    }

    public Date getContributionDate() {
        return contributionDate;
    }

    public void setContributionDate(Date contributionDate) {
        this.contributionDate = contributionDate;
    }

    public Double getContributionAmount() {
        return contributionAmount;
    }

    public void setContributionAmount(Double contributionAmount) {
        this.contributionAmount = contributionAmount;
    }

    public Double getGroupShare() {
        return groupShare;
    }

    public void setGroupShare(Double groupShare) {
        this.groupShare = groupShare;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public void save() throws SQLException, IOException {
        Connection conn = ConnectionProvider.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO chamabaraka.contributions VALUES (?,?,?,?,?,?)"
        );
        int min = 100, max = 999_999;
        setContributionID("CONT" + (int) (Math.random() * (max - min + 1) + min));

        stmt.setString(1, contributionID);
        stmt.setDouble(2, contributionAmount);
        if(groupID == null) {
            stmt.setNull(3, Types.DOUBLE);
            stmt.setNull(5, Types.VARCHAR);
        } else {
            stmt.setDouble(3, groupShare);
            stmt.setString(5, groupID);
        }
        stmt.setDate(4, contributionDate);
        stmt.setString(6, contributorID);
        stmt.executeUpdate();
        stmt.close();
        conn.close();
    }

    public static double getMembersContributionsTotal() throws SQLException, IOException {
        Connection conn = ConnectionProvider.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
                "SELECT SUM(contributionAmount) FROM chamabaraka.contributions"
        );
        ResultSet rs = stmt.executeQuery();
        double total = 0.0;
        if(rs.next()) {
            total = rs.getDouble(1);
        }
        return total;
    }

    public static double getGroupsContributionsTotal() throws SQLException, IOException {
        Connection conn = ConnectionProvider.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
                "SELECT SUM(groupShare) FROM chamabaraka.contributions"
        );
        ResultSet rs = stmt.executeQuery();
        double total = 0.0;
        if(rs.next()) {
            total = rs.getDouble(1);
        }
        return total;
    }

    public static double getMemberContributionsTotal(String memberID) throws SQLException, IOException {
        Connection conn = ConnectionProvider.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
                "SELECT SUM(contributionAmount) FROM chamabaraka.contributions WHERE contributorID = ?"
        );
        stmt.setString(1, memberID);
        ResultSet rs = stmt.executeQuery();
        double total = 0.0;
        if(rs.next()) {
            total = rs.getDouble(1);
        }
        return total;
    }

    public static double getGroupContributionsTotal(String groupID) throws SQLException, IOException {
        Connection conn = ConnectionProvider.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
                "SELECT SUM(groupShare) FROM chamabaraka.contributions WHERE groupID = ?"
        );
        stmt.setString(1, groupID);
        ResultSet rs = stmt.executeQuery();
        double total = 0.0;
        if(rs.next()) {
            total = rs.getDouble(1);
        }
        return total;
    }

    public static ArrayList<Contribution> getContributions() throws SQLException, IOException {
        ArrayList<Contribution> contributions = new ArrayList<>();
            Connection conn = ConnectionProvider.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM chamabaraka.contributions"
            );
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Contribution contribution = new Contribution(
                        rs.getString(6),
                        rs.getDate(4),
                        rs.getDouble(2),
                        rs.getDouble(3),
                        rs.getString(5)
                );
                contribution.setContributionID(rs.getString(1));
                contributions.add(contribution);
            }
            return contributions;

    }

    @Override
    public String toString() {
        return "<Contribution " +
                "contributorID='" + contributorID + '\'' +
                ", contributionAmount='" + contributionAmount + '\'' +
                ", groupShare=" + groupShare +
                '>';
    }
}
