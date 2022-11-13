package com.mwanzobaraka.util;

import com.mwanzobaraka.dbcfg.ConnectionProvider;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreateTables {
    static PreparedStatement stmt;
    public static void create(){
//        new Thread(()->{

        try (Connection conn = ConnectionProvider.getConnection()){

            String createSchema = "CREATE SCHEMA IF NOT EXISTS `chamabaraka` DEFAULT CHARACTER SET utf8 ;";
            stmt = conn.prepareStatement(createSchema);
            stmt.executeUpdate();
            stmt.close();

            stmt = conn.prepareStatement("USE chamabaraka;");
            stmt.executeUpdate();
            stmt.close();

            String createGroupsTable =
                    """
                            CREATE TABLE IF NOT EXISTS `mgroups`(
                            `groupID` VARCHAR(5) PRIMARY KEY,\s
                            `groupName` VARCHAR(30) NOT NULL,\s
                            `numOFMembers` INT NOT NULL,\s
                            `dateRegistered` DATE NOT NULL\s
                            );""";
            stmt = conn.prepareStatement(createGroupsTable);
            stmt.executeUpdate();
            stmt.close();

            String createMembersTable =
                    """
                            CREATE TABLE IF NOT EXISTS `members`(
                            `member_id` VARCHAR(6),
                            `first_name` VARCHAR(15) NOT NULL,
                            `middle_name` VARCHAR(15) DEFAULT NULL,
                            `last_name` VARCHAR(15) NOT NULL,
                            `id_number` VARCHAR(8) NOT NULL,
                            `gender` VARCHAR(6) NOT NULL,
                            `mobile_number` VARCHAR(10) NOT NULL,
                            `email` VARCHAR(50) DEFAULT NULL,
                            `date_joined` DATE NOT NULL,
                            `groupID` VARCHAR(5) DEFAULT NULL,
                            PRIMARY KEY (`member_id`),
                            FOREIGN KEY (`groupID`) REFERENCES mgroups (`groupID`));""";
            stmt = conn.prepareStatement(createMembersTable);
            stmt.executeUpdate();
            stmt.close();

            String createRegFeesTable =
                    """
                            CREATE TABLE IF NOT EXISTS `registration_fees`(
                            `regID` VARCHAR(9) NOT NULL,
                            `amount_paid` DECIMAL(10,2) NOT NULL,
                            `date_paid` DATE NOT NULL,
                            `member_id` VARCHAR(6),
                            `group_id` VARCHAR(6),
                            PRIMARY KEY (`regID`),
                            FOREIGN KEY (`member_id`) REFERENCES members (`member_id`),
                            FOREIGN KEY (`group_id`) REFERENCES mgroups (`groupID`)
                            );
                            """;
            stmt = conn.prepareStatement(createRegFeesTable);
            stmt.executeUpdate();
            stmt.close();

            String createContributionsTable =
                    """
                            CREATE TABLE IF NOT EXISTS `contributions`(
                            `contributionID` VARCHAR(10) NOT NULL,
                            `contributionAmount` DECIMAL(10,2) NOT NULL,
                            `groupShare` DECIMAL(10,2),
                            `contributionDate` DATE NOT NULL,
                            `groupID` VARCHAR(6),
                            `contributorID` VARCHAR(6) NOT NULL,
                            PRIMARY KEY (`contributionID`),
                            FOREIGN KEY (`contributorID`) REFERENCES members (`member_id`)
                            );
                            """;
            stmt = conn.prepareStatement(createContributionsTable);
            stmt.executeUpdate();
            stmt.close();

            String createLoansTable =
                    """
                            CREATE TABLE IF NOT EXISTS `loans`(
                            `loanID` VARCHAR(10) NOT NULL,
                            `loanAmount` DECIMAL(10,2) NOT NULL,
                            `interestRate` DECIMAL(10,2) NOT NULL,
                            `loanBalance` DECIMAL(10,2) NOT NULL,
                            `loanInterest` DECIMAL(10,2) NOT NULL,
                            `loanStatus` VARCHAR(10) NOT NULL,
                            `loanDate` DATE NOT NULL,
                            `loanPeriod` VARCHAR(10) NOT NULL,
                            `principal` DECIMAL(10,2) NOT NULL,
                            `contributorID` VARCHAR(6) NOT NULL,
                            PRIMARY KEY (`loanID`),
                            FOREIGN KEY (`contributorID`) REFERENCES members (`member_id`)
                            );
                            """;
            stmt = conn.prepareStatement(createLoansTable);
            stmt.executeUpdate();
            stmt.close();

            String createPenaltyTable =
                    """
                            CREATE TABLE IF NOT EXISTS `penalties`(
                            `penaltyID` VARCHAR(10) NOT NULL,
                            `penaltyAmount` DECIMAL(10,2) NOT NULL,
                            `penaltyDate` DATE NOT NULL,
                            `loanID` VARCHAR(10) NOT NULL,
                            PRIMARY KEY (`penaltyID`),
                            FOREIGN KEY (`loanID`) REFERENCES loans (`loanID`)
                            );
                            """;

            stmt = conn.prepareStatement(createPenaltyTable);
            stmt.executeUpdate();
            stmt.close();

            String createInvoiceTable =
                    """
                            CREATE TABLE IF NOT EXISTS `invoices`(
                            `invoiceID` VARCHAR(10) NOT NULL,
                            `invoiceAmount` DECIMAL(10,2) NOT NULL,
                            `invoiceDate` DATE NOT NULL,
                            `invoiceStatus` VARCHAR(10) NOT NULL,
                            `dueDate` DATE NOT NULL,
                            `loanID` VARCHAR(10) NOT NULL,
                            PRIMARY KEY (`invoiceID`),
                            FOREIGN KEY (`loanID`) REFERENCES loans (`loanID`)
                            );
                            """;
            stmt = conn.prepareStatement(createInvoiceTable);
            stmt.executeUpdate();
            stmt.close();

            String createRepaymentsTable =
                    """
                            CREATE TABLE IF NOT EXISTS `repayments`(
                            `repaymentID` VARCHAR(10) NOT NULL,
                            `invoiceID` VARCHAR(10) NOT NULL,
                            `repaymentAmount` DECIMAL(10,2) NOT NULL,
                            `repaymentDate` DATE NOT NULL,
                            `interest` DECIMAL(10,2) NOT NULL,
                            PRIMARY KEY (`repaymentID`),
                            FOREIGN KEY (`invoiceID`) REFERENCES invoices (`invoiceID`)
                            );
                            """;
            stmt = conn.prepareStatement(createRepaymentsTable);
            stmt.executeUpdate();
            stmt.close();

            String createDividendsTable =
                    """
                            CREATE TABLE IF NOT EXISTS `dividends`(
                            `dividendID` VARCHAR(10) NOT NULL,
                            `dividendAmount` DECIMAL(10,2) NOT NULL,
                            `dividendDate` DATE NOT NULL,
                            `memberID` VARCHAR(6) NOT NULL,
                            PRIMARY KEY (`dividendID`),
                            FOREIGN KEY (`memberID`) REFERENCES members (`member_id`)
                            );
                            """;
            stmt = conn.prepareStatement(createDividendsTable);
            stmt.executeUpdate();
            stmt.close();

        }catch (SQLException | IOException e) {
            e.printStackTrace();
        }
//        }).start();
    }

}

