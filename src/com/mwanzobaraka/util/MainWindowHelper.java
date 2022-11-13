package com.mwanzobaraka.util;

import com.mwanzobaraka.models.*;
import com.mwanzobaraka.windows.MainWindow;
import com.mwanzobaraka.windows.RegSingleWindow;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;

public class MainWindowHelper {
    static ProjectButton members, groups, contrib,
            regFees, penalties, loans,
            l_members, l_groups, l_penalties,
            l_loans, l_contrib, l_regFees, repay, l_repay;

    public static ArrayList<ProjectButton> createButtons(){

        members = new ProjectButton("Members");
        members.setEnabled(false);
        members.setXYCoordinates(5, 5, 100);
        groups = new ProjectButton("Groups");
        groups.setXYCoordinates(120, 5, 100);
        penalties = new ProjectButton("Penalties");
        penalties.setXYCoordinates(5, 50, 100);
        loans = new ProjectButton("Loans");
        loans.setXYCoordinates(120, 50, 100);
        contrib = new ProjectButton("Contributions");
        contrib.setXYCoordinates(5, 95, 220);
        regFees = new ProjectButton("Registration Fees");
        regFees.setXYCoordinates(5, 145, 220);
        repay = new ProjectButton("Repayments");
        repay.setXYCoordinates(5, 190, 220);

        return new ArrayList<>(Arrays.asList(
                members, groups, contrib, regFees, penalties, loans, repay
        ));

    }
    private static void showUpperButtons(){
        members.setEnabled(true);
        groups.setEnabled(true);
        contrib.setEnabled(true);
        regFees.setEnabled(true);
        penalties.setEnabled(true);
        loans.setEnabled(true);
        repay.setEnabled(true);
    }
    private static void showLowerButtons(){
        l_members.setEnabled(true);
        l_groups.setEnabled(true);
        l_contrib.setEnabled(true);
        l_regFees.setEnabled(true);
        l_penalties.setEnabled(true);
        l_loans.setEnabled(true);
        l_repay.setEnabled(true);
    }
    public static void onClick(ActionEvent e){
        if (e.getSource() == members) {
            showUpperButtons();
            members.setEnabled(false);
            showMembers(MainWindow.getUTable());
        }
        if (e.getSource() == l_members) {
            showLowerButtons();
            l_members.setEnabled(false);
            showMembers(MainWindow.getLTable());
        }
        if (e.getSource() == groups) {
            showUpperButtons();
            groups.setEnabled(false);
            showGroups(MainWindow.getUTable());
        }
        if (e.getSource() == l_groups) {
            showLowerButtons();
            l_groups.setEnabled(false);
            showGroups(MainWindow.getLTable());
        }
        if (e.getSource() == contrib) {
            showUpperButtons();
            contrib.setEnabled(false);
            showContributions(MainWindow.getUTable());
        }
        if (e.getSource() == l_contrib) {
            showLowerButtons();
            l_contrib.setEnabled(false);
            showContributions(MainWindow.getLTable());
        }
        if (e.getSource() == regFees) {
            showUpperButtons();
            regFees.setEnabled(false);
            showRegistrationFees(MainWindow.getUTable());
        }
        if (e.getSource() == l_regFees) {
            showLowerButtons();
            l_regFees.setEnabled(false);
            showRegistrationFees(MainWindow.getLTable());
        }
        if (e.getSource() == penalties) {
            showUpperButtons();
            penalties.setEnabled(false);
            showPenalties(MainWindow.getUTable());
        }
        if (e.getSource() == l_penalties) {
            showLowerButtons();
            l_penalties.setEnabled(false);
            showPenalties(MainWindow.getLTable());
        }
        if (e.getSource() == loans) {
            showUpperButtons();
            loans.setEnabled(false);
            showLoans(MainWindow.getUTable());
        }
        if (e.getSource() == l_loans) {
            showLowerButtons();
            l_loans.setEnabled(false);
            showLoans(MainWindow.getLTable());
        }
        if (e.getSource() == repay) {
            showUpperButtons();
            repay.setEnabled(false);
            showRepayments(MainWindow.getUTable());
        }
        if (e.getSource() == l_repay) {
            showLowerButtons();
            l_repay.setEnabled(false);
            showRepayments(MainWindow.getLTable());
        }
    }

    public static ArrayList<ProjectButton> createLowerButtons() {
        l_members = new ProjectButton("Members");
        l_members.setXYCoordinates(5, 5, 100);
        l_groups = new ProjectButton("Groups");
        l_groups.setEnabled(false);
        l_groups.setXYCoordinates(120, 5, 100);
        l_penalties = new ProjectButton("Penalties");
        l_penalties.setXYCoordinates(5, 50, 100);
        l_loans = new ProjectButton("Loans");
        l_loans.setXYCoordinates(120, 50, 100);
        l_contrib = new ProjectButton("Contributions");
        l_contrib.setXYCoordinates(5, 95, 220);
        l_regFees = new ProjectButton("Registration Fees");
        l_regFees.setXYCoordinates(5, 145, 220);
        l_repay = new ProjectButton("Repayments");
        l_repay.setXYCoordinates(5, 190, 220);

        return new ArrayList<>(Arrays.asList(
                l_members, l_groups, l_contrib, l_regFees, l_penalties, l_loans, l_repay
        ));
    }
    private static void showMembers(ProjectTable table){
        String[] columns = {"Member ID", "First Name", "Middle Name", "Last Name", "ID No", "Gender", "Date Registered", "Group"};
        DefaultTableModel md = (DefaultTableModel)table.getModel();
        md.setColumnIdentifiers(columns);
        md.setRowCount(0);
        table.getSelectionModel().addListSelectionListener(e -> {
                    if (!e.getValueIsAdjusting()){
                        int row = table.getSelectedRow();
                        if (row != -1){
                            table.clearSelection();
                            new RegSingleWindow(null, new Rectangle(300, 40, 700, 450), true,(String) table.getValueAt(row, 0));
                        }
                    }
                }
        );
        new Thread(()->{
            try{
                for(Member member : Member.getAllMembers()){
                    md.addRow(new Object[]{
                            member.getMemberId(), member.getFName(), member.getMName(),
                            member.getLName(), member.getIDNumber(), member.getGender(),
                            member.getDateRegistered(), member.getGroupID()
                    });
                }
            }catch(SQLException | IOException e){
                JOptionPane.showMessageDialog(null, "Query Error", "Error", JOptionPane.ERROR_MESSAGE);
                //dispose();
                e.printStackTrace();
            }

        }).start();

    }
    private static void showGroups (ProjectTable table){
        DefaultListSelectionModel sm = (DefaultListSelectionModel) table.getSelectionModel();
        if(sm.getListSelectionListeners().length > 0)sm.removeListSelectionListener(sm.getListSelectionListeners()[0]);
        String [] columns = {"Group ID", "Group Name", "Number of Members", "Date Registered"};
        DefaultTableModel md = (DefaultTableModel)table.getModel();
        md.setColumnIdentifiers(columns);
        md.setRowCount(0);
        new Thread(()->{
            try {
                for (Group g: Group.getGroups()){
                    md.addRow(new Object[]{
                            g.getGroupID(), g.getGroupName(), g.getNoOfMembers(), g.getDateRegistered()
                    });
                }
            } catch (SQLException | IOException e) {
                JOptionPane.showMessageDialog(null, "Query Error g", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }).start();


    }
     private static void showContributions(ProjectTable table){
         DefaultListSelectionModel sm = (DefaultListSelectionModel) table.getSelectionModel();
         if(sm.getListSelectionListeners().length > 0)sm.removeListSelectionListener(sm.getListSelectionListeners()[0]);
         String[] columns = {"ContributionID", "Amount", "Group Share", "Date", "Group ID", "MemberID"};
         DefaultTableModel md = (DefaultTableModel)table.getModel();
         md.setColumnIdentifiers(columns);
         md.setRowCount(0);
         new Thread(()->{
             try{
                 for(Contribution contribution : Contribution.getContributions()){
                     md.addRow(new Object[]{
                             contribution.getContributionID(), contribution.getContributionAmount(), contribution.getGroupShare(),
                             contribution.getContributionDate(), contribution.getGroupID(), contribution.getContributorID()
                     });
                 }
             } catch (SQLException | IOException e) {
                 JOptionPane.showMessageDialog(null, "Query Error", "Error", JOptionPane.ERROR_MESSAGE);
             }
         }).start();

    }
     private static void showRegistrationFees(ProjectTable table){
         DefaultListSelectionModel sm = (DefaultListSelectionModel) table.getSelectionModel();
         if(sm.getListSelectionListeners().length > 0)sm.removeListSelectionListener(sm.getListSelectionListeners()[0]);
        String[] columns = {"RegistrationID", "Amount", "Date", "MemberID", "GroupID"};
         DefaultTableModel md = (DefaultTableModel)table.getModel();
         md.setColumnIdentifiers(columns);
         md.setRowCount(0);
         new Thread(()->{
             try {
                 for (RegFee registrationFee : RegFee.getRegFees()){
                     md.addRow(new Object[]{
                             registrationFee.getRegID(), registrationFee.getAmountPaid(), registrationFee.getDatePaid(),
                             registrationFee.getMemberID(), registrationFee.getGroupID()
                     });
                 }
             } catch (SQLException | IOException e) {
                 e.printStackTrace();
                 JOptionPane.showMessageDialog(null, "Query Error", "Error", JOptionPane.ERROR_MESSAGE);
             }
         }).start();
    }
     private static void showPenalties(ProjectTable table){
         DefaultListSelectionModel sm = (DefaultListSelectionModel) table.getSelectionModel();
         if(sm.getListSelectionListeners().length > 0)sm.removeListSelectionListener(sm.getListSelectionListeners()[0]);
         String[] columns = {"PenaltyID", "Amount", "Loan ID"};
         DefaultTableModel md = (DefaultTableModel)table.getModel();
         md.setColumnIdentifiers(columns);
         md.setRowCount(0);
         new Thread(()->{
             try {
                 for (Penalty penalty : Penalty.getPenalties()){
                     md.addRow(new Object[]{
                             penalty.getPenaltyID(), penalty.getPenaltyAmount(), penalty.getLoanID()
                     });
                 }
             } catch (SQLException | IOException e) {
                 JOptionPane.showMessageDialog(null, "Query Error", "Error", JOptionPane.ERROR_MESSAGE);
             }
         }).start();


    }
     private static void showLoans(ProjectTable table){
         DefaultListSelectionModel sm = (DefaultListSelectionModel) table.getSelectionModel();
         if(sm.getListSelectionListeners().length > 0) sm.removeListSelectionListener(sm.getListSelectionListeners()[0]);
        String[] columns = {"LoanID", "Amount", "Interest Rate", "Loan Balance", "Loan Interest", "Loan Status", "Principal", "Issue Date", "Period", "MemberID"};
         DefaultTableModel md = (DefaultTableModel)table.getModel();
         md.setColumnIdentifiers(columns);
         md.setRowCount(0);
         new Thread(()->{
             try {
                 for (Loan loan : Loan.getLoans()){
                     md.addRow(new Object[]{
                             loan.getLoanID(), loan.getLoanAmount(), loan.getInterestRate(),
                             loan.getLoanBalance(), loan.getLoanInterest(), loan.getLoanStatus(),
                             loan.getLoanPrincipal(), loan.getLoanDate(), loan.getLoanPeriod(),
                             loan.getContributorID()
                     });
                 }
             } catch (SQLException | IOException e) {
                 JOptionPane.showMessageDialog(null, "Query Error", "Error", JOptionPane.ERROR_MESSAGE);
                 e.printStackTrace();
             }
         }).start();

    }
    private static void showRepayments(ProjectTable table){
        DefaultListSelectionModel sm = (DefaultListSelectionModel) table.getSelectionModel();
        if(sm.getListSelectionListeners().length > 0)sm.removeListSelectionListener(sm.getListSelectionListeners()[0]);
        String[] columns = {"RepaymentID", "Invoice ID", "Amount", "Date", "Interest"};
        DefaultTableModel md = (DefaultTableModel)table.getModel();
        md.setColumnIdentifiers(columns);
        md.setRowCount(0);
        new Thread(()->{
            try {
                for (Repayment repayment : Repayment.getRepayments()){
                    md.addRow(new Object[]{
                            repayment.getRepaymentID(), repayment.getInvoiceID(), repayment.getRepaymentAmount(),
                            repayment.getPaymentDate(),  repayment.getInterest()
                    });
                }
            } catch (SQLException | IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Query Error", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }).start();
    }

    public static void addInvoices(){
        LocalDate lastDayLastMonth = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue()-1, 10)
                .with(TemporalAdjusters.lastDayOfMonth());
        if(LocalDate.now().isAfter(lastDayLastMonth) && LocalDate.now().isBefore(lastDayLastMonth.plusDays(10))){
            new Thread(()->{
                try {
                    for (Loan loan : Loan.getUnfinishedLoans()){
                        if (Invoice.getInvoice(loan.getLoanID()) == null)
                            new Invoice(loan.getLoanID(), loan.getLoanPrincipal()).save();

                    }
                } catch (SQLException | IOException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Query Error", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }).start();

        }
    }
    public static void addDividends(){
        if (LocalDate.now().isEqual(LocalDate.of(LocalDate.now().getYear(), 12, 20))) {
            try {
                if (!Dividend.dividendsPaid())
                    CalculateDividends.calculateDividends();
            }catch (SQLException | IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error calculating dividends", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    public static void penalise(){
        if (LocalDate.now().isEqual(LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()))) {
            try {
                for (Invoice invoice : Invoice.getUnpaidInvoices()){
                    invoice.setInvoiceStatus("DEFAULT");
                    if (!Penalty.alreadyPenalised(invoice.getLoanID()))
                        new Penalty(invoice.getLoanID(), .1*invoice.getInvoiceAmount()).save();
                }
            }catch (SQLException | IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error calculating penalties", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}
