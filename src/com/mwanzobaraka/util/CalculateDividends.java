package com.mwanzobaraka.util;

import com.mwanzobaraka.dbcfg.ConnectionProvider;
import com.mwanzobaraka.models.Contribution;
import com.mwanzobaraka.models.Dividend;
import com.mwanzobaraka.models.Member;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CalculateDividends {

    public static double getAllIncome() throws SQLException, IOException {
        double totalIncome = 0;
        Connection conn = ConnectionProvider.getConnection();
        PreparedStatement stmt = conn.prepareStatement("SELECT SUM(penaltyAmount) FROM chamabaraka.penalties");
        if (stmt.executeQuery().next()) {
            totalIncome += stmt.getResultSet().getDouble(1);
        }
        stmt = conn.prepareStatement("SELECT SUM(interest) FROM chamabaraka.repayments");
        if (stmt.executeQuery().next()) {
            totalIncome += stmt.getResultSet().getDouble(1);
        }
        stmt = conn.prepareStatement("SELECT SUM(amount_paid) FROM chamabaraka.registration_fees");
        if (stmt.executeQuery().next()) {
            totalIncome += stmt.getResultSet().getDouble(1);
        }
        return totalIncome;
    }

    public static void calculateDividends() throws SQLException, IOException {
        double totalIncome = getAllIncome();
        double totalDividends = totalIncome * 0.6;
        double totalContributions = Contribution.getMembersContributionsTotal();
        for (Member member : Member.getAllMembers()) {
            double memberContribution = Contribution.getMemberContributionsTotal(member.getMemberId());
            double memberDividend = (memberContribution / totalContributions) * totalDividends;
            new Dividend(member.getMemberId(), memberDividend).save();
        }

    }
}
