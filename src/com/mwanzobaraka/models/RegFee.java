package com.mwanzobaraka.models;

import com.mwanzobaraka.dbcfg.ConnectionProvider;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegFee {

    private String memberID;
    private String groupID;
    private Date datePaid;
    private Double amountPaid;
    private String regID;

    //TODO: Make amount dynamic i.e can be changed by the user
    public RegFee(String memberID, String groupID, Date datePaid, Double amountPaid) {
        this.memberID = memberID;
        this.groupID = groupID;
        this.datePaid = datePaid;
        this.amountPaid = amountPaid;
    }

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public Date getDatePaid() {
        return datePaid;
    }

    public void setDatePaid(Date datePaid) {
        this.datePaid = datePaid;
    }

    public Double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(Double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public String getRegID() {
        return regID;
    }

    public void setRegID(String regID) {
        this.regID = regID;
    }


    public void save() throws SQLException, IOException {
        Connection conn = ConnectionProvider.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO chamabaraka.registration_fees VALUES (?, ?, ?, ?, ?)"
        );
        int min = 100_000, max = 999_999;
        int regId_int = (int) Math.floor(Math.random() * (max - min + 1) + min);

        setRegID("REG" + regId_int);
        stmt.setString(1, regID);
        stmt.setDouble(2, amountPaid);
        stmt.setDate(3, datePaid);
        if(groupID == null) {
            stmt.setString(4, memberID);
            stmt.setNull(5, Types.VARCHAR);
        }else {
            stmt.setNull(4, Types.VARCHAR);
            stmt.setString(5, groupID);
        }
        stmt.executeUpdate();
        conn.close();
        stmt.close();
    }

    public static ArrayList<RegFee> getRegFees() throws SQLException, IOException {
        Connection conn = ConnectionProvider.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM chamabaraka.registration_fees"
        );
        ResultSet rs = stmt.executeQuery();
        ArrayList<RegFee> regFees = new ArrayList<>();
        while(rs.next()) {
            RegFee regFee = new RegFee(
                    rs.getString("member_id"),
                    rs.getString("group_id"),
                    rs.getDate("date_paid"),
                    rs.getDouble("amount_paid")
            );
            regFee.setRegID(rs.getString("regID"));
            regFees.add(regFee);
        }
        return regFees;
    }

    @Override
    public String toString() {
        return "RegFees<" +
                "memberID='" + memberID + '\'' +
                ", amountPaid='" + amountPaid + '\'' +
                '>';
    }
}
