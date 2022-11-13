package com.mwanzobaraka.windows;

import com.mwanzobaraka.models.Contribution;
import com.mwanzobaraka.models.Loan;
import com.mwanzobaraka.models.Member;
import com.mwanzobaraka.models.Repayment;
import com.mwanzobaraka.util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class LendingWindow extends GenericWindow implements ActionListener {
    ProjectJPanel card;
    ProjectButton reset, saveClose, search;
    ProjectLabel lblFullName, fullName, lblQualifiedLoan, qualifiedLoan, error, lblAmt;
    ProjectTextField txtMemberID, txtAmount;
    Member member = null;
    double contributions = 0.0;
    double qualifiedFor = 0.0;
    public LendingWindow(LayoutManager layout, Rectangle bounds) {
        super(layout, bounds);
        card = new ProjectJPanel(new BorderLayout(), 1);
        card.setBounds(10, 10, 665, 390);
        add(card);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        createComponents();
        setVisible(true);
    }

    private ArrayList<? extends Component> createButtons() {
        reset = new ProjectButton("Reset", true);
        reset.setEnabled(false);
        reset.setXYCoordinates(360, 140, 100);
        saveClose = new ProjectButton("Save");
        saveClose.setEnabled(false);
        saveClose.setXYCoordinates(470, 140, 150);


        return new ArrayList<>(Arrays.asList(
                reset, saveClose
        ));
    }
    private void createComponents() {
        ProjectJPanel bottomPanel = new ProjectJPanel(null, 1);
        bottomPanel.setPreferredSize(new Dimension(0, 200));
        ProjectJPanel centerPanel = new ProjectJPanel(null, 1);

        ProjectLabel header = new ProjectLabel("MEMBER LOAN");
        header.setFont(new Font("Roboto", Font.BOLD, 25));
        header.setXYCoordinates(170, 10, 400);

        ProjectLabel lblMID = new ProjectLabel("Member ID");
        lblMID.setXYCoordinates(10, 80);
        lblAmt = new ProjectLabel("Amount");
        lblAmt.setVisible(false);
        lblAmt.setXYCoordinates(340, 80);

        txtMemberID = new ProjectTextField();
        txtMemberID.setDocument(new LimitDocument(6));
        txtMemberID.setXYCoordinates(130, 80, 100);
        txtAmount = new ProjectTextField();
        txtAmount.setVisible(false);
        txtAmount.setXYCoordinates(460, 80, 200);

        search = new ProjectButton("Search");
        search.addActionListener(this);
        search.setBounds(235, 80, 100, 30);

        ArrayList<Component> centerComponents = new ArrayList<>(Arrays.asList(
                header, lblMID, lblAmt, txtMemberID,
                txtAmount, search
        ));
        for (Component c : centerComponents) {
            centerPanel.add(c);
        }

        lblFullName = new ProjectLabel("Full Name:");
        lblFullName.setVisible(false);
        lblFullName.setXYCoordinates(10, 50);
        fullName = new ProjectLabel();
        fullName.setVisible(false);
        fullName.setXYCoordinates(130, 50, 200);

        lblQualifiedLoan = new ProjectLabel("Qualifies For:");
        lblQualifiedLoan.setVisible(false);
        lblQualifiedLoan.setXYCoordinates(340, 50);
        qualifiedLoan = new ProjectLabel();
        qualifiedLoan.setVisible(false);
        qualifiedLoan.setXYCoordinates(460, 50, 200);

        error = new ProjectLabel("No member with that ID was found");
        error.setVisible(false);
        error.setForeground(Color.red);
        error.setXYCoordinates(10, 90, 500);

        ArrayList<Component> bottomComponents = new ArrayList<>(Arrays.asList(
                lblFullName, fullName, lblQualifiedLoan, qualifiedLoan, error
        ));
        bottomComponents.addAll(createButtons());
        for (Component c : bottomComponents) {
            if (c instanceof ProjectButton) {
                ((ProjectButton) c).addActionListener(this);
            }
            bottomPanel.add(c);
        }

        card.add(bottomPanel, BorderLayout.SOUTH);
        card.add(centerPanel, BorderLayout.CENTER);
    }

    private void toggleComponents(boolean toggle) {
        lblFullName.setVisible(toggle);
        fullName.setVisible(toggle);
        lblQualifiedLoan.setVisible(toggle);
        qualifiedLoan.setVisible(toggle);
        lblAmt.setVisible(toggle);
        txtAmount.setVisible(toggle);
        reset.setEnabled(toggle);
        saveClose.setEnabled(toggle);
        error.setVisible(!toggle);
    }

    private void resetComponents() {
        txtMemberID.setText("");
        txtAmount.setText("");
        fullName.setText("");
        qualifiedLoan.setText("");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == search) {
            if (txtMemberID.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Please enter a member ID");
            } else {
                try {
                    member = Member.getMember(txtMemberID.getText());
                    if (member != null) {
                        if (member.getMemberLoan() != null) {
                            JOptionPane.showMessageDialog(null, "Member already has a loan", "Error", JOptionPane.ERROR_MESSAGE);
                            resetComponents();
                            toggleComponents(false);
                            error.setVisible(false);
                        } else {
                            toggleComponents(true);
                            contributions = Contribution.getMemberContributionsTotal(member.getMemberId());
                            fullName.setText(member.getFullName());
                            if (member.getGroupID() == null){
                                qualifiedFor = contributions*3;
                                qualifiedLoan.setText("Ksh. " + qualifiedFor);
                            } else {
                                qualifiedFor = contributions*4;
                                qualifiedLoan.setText("Ksh. " + qualifiedFor);
                            }
                        }

                    } else {
                        toggleComponents(false);
                    }
                } catch (SQLException | IOException ex) {
                    error.setText("An error occurred");
                    toggleComponents(false);
                    ex.printStackTrace();
                }
            }
        } else if (e.getSource() == reset) {
            resetComponents();
            toggleComponents(false);
            error.setVisible(false);
        } else if (e.getSource() == saveClose) {
            if (txtAmount.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Please enter an amount");
            } else {
                try {
                    double amount = Double.parseDouble(txtAmount.getText());
                    if (amount > qualifiedFor) {
                        JOptionPane.showMessageDialog(null, "Amount exceeds qualified loan");
                    } else {
                        double interestRate = 1.2;
                        int loanPeriod = 3*12;
                        if (member.getGroupID() != null) {
                            interestRate = 1.0;
                            loanPeriod = 4*12;
                        }
                        Loan loan = new Loan(member.getMemberId(), amount, interestRate, loanPeriod, new Date(System.currentTimeMillis()));
                        loan.save();
//                        new Repayment(loan.getInvoiceID(), amount, new Date(System.currentTimeMillis())).save();
                        resetComponents();
                        toggleComponents(false);
                        error.setVisible(false);
                    }
                } catch (NumberFormatException ex) {
                    error.setText( "Please enter a valid amount");
                    error.setVisible(true);
                } catch (SQLException | IOException ex) {
                    error.setText( "An error occurred");
                    error.setVisible(true);
                    ex.printStackTrace();
                }
            }
        }
    }
}
