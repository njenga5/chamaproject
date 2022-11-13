package com.mwanzobaraka.models;

import com.mwanzobaraka.dbcfg.ConnectionProvider;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class Group {
    private String groupID;
    private String groupName;
    private int noOfMembers;
    private Date dateRegistered;

    public Group(String groupID, String groupName, int noOfMembers) {
        if (groupID == null || groupID.isEmpty() || groupName == null
                || groupName.isEmpty() || noOfMembers < 2) {
            throw new IllegalArgumentException("Some fields are empty");
        }
        this.groupID = groupID;
        this.groupName = groupName;
        this.noOfMembers = noOfMembers;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getNoOfMembers() {
        return noOfMembers;
    }

    public void setNoOfMembers(int noOfMembers) {
        this.noOfMembers = noOfMembers;
    }

    public Date getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(Date dateRegistered) {
        this.dateRegistered = dateRegistered;
    }


    public Group save() throws SQLException, IOException {
        Connection connection = ConnectionProvider.getConnection();
        String sql = "INSERT INTO chamabaraka.mgroups VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        setDateRegistered(new Date(System.currentTimeMillis()));
        stmt.setString(1, groupID);
        stmt.setString(2, groupName);
        stmt.setInt(3, noOfMembers);
        stmt.setDate(4, dateRegistered);
        stmt.executeUpdate();
        stmt.close();
        connection.close();
        new RegFee(null, getGroupID(), getDateRegistered(), 5000.00).save();
        return this;
    }

    public static ArrayList<Group> getGroups() throws SQLException, IOException {
        ArrayList<Group> groups = new ArrayList<>();
        Connection connection = ConnectionProvider.getConnection();
        String sql = "SELECT * FROM chamabaraka.mgroups";
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()){
            Group group = extractGroup(rs);
            groups.add(group);
        }
        return groups;
    }

    public static Group getGroup(String groupID) throws SQLException, IOException {
        Connection connection = ConnectionProvider.getConnection();
        String sql = "SELECT * FROM chamabaraka.mgroups WHERE groupID = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, groupID);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()){
            stmt.close();
            connection.close();
            return extractGroup(rs);
        }
        return null;
    }

    private static Group extractGroup(ResultSet rs) throws SQLException {
        Group group = new Group(
                rs.getString("groupID"),
                rs.getString("groupName"),
                rs.getInt("numOFMembers")
        );
        group.setDateRegistered(rs.getDate("dateRegistered"));
        return group;
    }

    @Override
    public String toString() {
        return "Group<" +
                "groupID='" + groupID + '\'' +
                ", groupName='" + groupName + '\'' +
                '>';
    }
}
