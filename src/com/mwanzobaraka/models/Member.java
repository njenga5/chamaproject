package com.mwanzobaraka.models;

import com.mwanzobaraka.dbcfg.ConnectionProvider;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;


public class Member {
    private String MemberId;
    private Date dateRegistered;
    private String FName, MName, LName, IDNumber, gender, mobile, email, groupID;

    public Member(String fName, String mName, String lName, String IDNumber, String gender, String mobile, String email, String groupID) {
        this.FName = fName;
        this.MName = mName;
        this.LName = lName;
        this.IDNumber = IDNumber;
        this.gender = gender;
        this.mobile = mobile;
        this.email = email;
        this.groupID = groupID;

    }

    public String getMemberId() {
        return MemberId;
    }


    public String getFName() {
        return FName;
    }

    public void setFName(String FName) {
        this.FName = FName;
    }

    public String getMName() {
        return MName;
    }

    public void setMName(String MName) {
        this.MName = MName;
    }

    public String getLName() {
        return LName;
    }

    public void setLName(String LName) {
        this.LName = LName;
    }

    public String getIDNumber() {
        return IDNumber;
    }

    public void setIDNumber(String IDNumber) {
        this.IDNumber = IDNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public Date getDateRegistered() {
        return dateRegistered;
    }

    public void setMemberId(String memberId) {
        MemberId = memberId;
    }

    public void setDateRegistered(Date dateRegistered) {
        this.dateRegistered = dateRegistered;
    }
    public String getFullName(){
        return FName + " " + MName + " " + LName;
    }

    public Member save() throws SQLException, IOException {
        Connection conn = ConnectionProvider.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO chamabaraka.members VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
            );
            int min = 100000, max = 1000000;

            setMemberId((int) Math.floor(Math.random() * (max - min + 1) + min) + "");
            setDateRegistered(new Date(System.currentTimeMillis()));
            stmt.setString(1, MemberId);
            stmt.setString(2, FName);
            if (MName == null || MName.isEmpty()) {
                stmt.setNull(3, Types.VARCHAR);
            } else {
                stmt.setString(3, MName);
            }
            stmt.setString(4, LName);
            stmt.setString(5, IDNumber);
            stmt.setString(6, gender);
            stmt.setString(7, mobile);
            if (email == null || email.isEmpty()) {
                stmt.setNull(8, Types.VARCHAR);
            } else {
                stmt.setString(8, email);
            }
            stmt.setDate(9, dateRegistered);
            if (groupID == null || groupID.isEmpty()) {
                stmt.setNull(10, Types.VARCHAR);
            } else {
                stmt.setString(10, groupID);
            }
            stmt.executeUpdate();
            new RegFee(getMemberId(), null, getDateRegistered(), 2000.00).save();
            return this;

    }


    private static Member extractMember(ResultSet rs) throws SQLException {
        Member member =  new Member(
                rs.getString("first_name"),
                rs.getString("middle_name"),
                rs.getString("last_name"),
                rs.getString("id_number"),
                rs.getString("gender"),
                rs.getString("mobile_number"),
                rs.getString("email"),
                rs.getString("groupID")
        );
        member.setMemberId(rs.getString("member_id"));
        member.setDateRegistered(rs.getDate("date_joined"));
        return member;
    }

    public Loan getMemberLoan() {
        return Loan.getLoanByMember(getMemberId());
    }

    public static Member getMember(String memberId) throws SQLException, IOException {
        Connection conn = ConnectionProvider.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM chamabaraka.members WHERE member_id = ?"
            );
            stmt.setString(1, memberId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Member member =  extractMember(rs);
                stmt.close();
                conn.close();
                return member;

            }
        return null;
    }

    private static ArrayList<Member> getMembers(ResultSet rs) throws SQLException {
            ArrayList<Member> members = new ArrayList<>();
            while (rs.next()) {
                members.add(extractMember(rs));
            }
            return members;
    }

    public static ArrayList<Member> getAllMembers() throws SQLException, IOException {
        ArrayList<Member> members;
        Connection conn = ConnectionProvider.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM chamabaraka.members"
            );
            ResultSet rs = stmt.executeQuery();
            members = getMembers(rs);
            return members;

    }

    public static ArrayList<Member> getMembersInAGroup(String groupID) throws SQLException, IOException {
        ArrayList<Member> members;
            Connection conn = ConnectionProvider.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM chamabaraka.members WHERE groupID = ?"
            );
            stmt.setString(1, groupID);
            ResultSet rs = stmt.executeQuery();
            members = getMembers(rs);
            return members;

    }

    public static void updateMember(String memberId, String first_name, String middle_name, String last_name, String id_number, String gender, String mobile_number, String email) throws SQLException, IOException {
        Connection conn = ConnectionProvider.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE members SET first_name = ?, middle_name = ?," +
                            " last_name = ?, id_number = ?, gender = ?," +
                            " mobile_number = ?, email = ? WHERE member_id = ?"
            );
            stmt.setString(1, first_name);
            stmt.setString(2, middle_name);
            stmt.setString(3, last_name);
            stmt.setString(4, id_number);
            stmt.setString(5, gender);
            stmt.setString(6, mobile_number);
            stmt.setString(7, email);
            stmt.setString(8, memberId);
            stmt.executeUpdate();
            stmt.close();
            conn.close();
    }

    @Override
    public String toString() {
        return "Member<" +
                "MemberId='" + MemberId + '\'' +
                ", FName='" + FName + '\'' +
                ", MName='" + MName + '\'' +
                ", LName='" + LName + '\'' +
                ", IDNumber='" + IDNumber + '\'' +
                '>';
    }
}
