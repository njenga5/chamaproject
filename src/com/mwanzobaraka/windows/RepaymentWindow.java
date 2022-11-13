package com.mwanzobaraka.windows;

import com.mwanzobaraka.models.*;
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

public class RepaymentWindow extends GenericWindow implements ActionListener {
    ProjectJPanel card;
    ProjectTextField txtMemberID, txtAmount;
    ProjectButton reset, saveClose, search;
    ProjectLabel lblFullName, fullName, lblMID2, MID, error,
            lblAmt, lblLoanAMT, loanAmt, lblPrincipalAMT, principalAMT;
    Member member = null;
    Loan loan = null;
    Invoice invoice = null;
    public RepaymentWindow(LayoutManager layout, Rectangle bounds) {
        super(layout, bounds);
        card = new ProjectJPanel(new BorderLayout(),1);
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

        ProjectLabel header = new ProjectLabel("LOAN REPAYMENT");
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
        lblFullName.setXYCoordinates(260, 50);
        fullName = new ProjectLabel();
        fullName.setVisible(false);
        fullName.setXYCoordinates(380, 50, 300);
        lblMID2 = new ProjectLabel("Member ID:");
        lblMID2.setVisible(false);
        lblMID2.setXYCoordinates(10, 50);
        MID = new ProjectLabel();
        MID.setVisible(false);
        MID.setXYCoordinates(130, 50);
        lblLoanAMT = new ProjectLabel("Loan Amount:");
        lblLoanAMT.setVisible(false);
        lblLoanAMT.setXYCoordinates(260, 85);
        loanAmt = new ProjectLabel();
        loanAmt.setXYCoordinates(380, 85, 300);
        loanAmt.setVisible(false);
        lblPrincipalAMT = new ProjectLabel("Principal Amount:");
        lblPrincipalAMT.setVisible(false);
        lblPrincipalAMT.setXYCoordinates(10, 85);
        principalAMT = new ProjectLabel("N/A");
        principalAMT.setXYCoordinates(130, 85);
        principalAMT.setVisible(false);
        error = new ProjectLabel("No member with that ID was found");
        error.setVisible(false);
        error.setForeground(Color.red);
        error.setXYCoordinates(10, 90, 500);
        ArrayList<Component> bottomComponents = new ArrayList<>(Arrays.asList(
                lblFullName, fullName, lblMID2, MID, error, lblLoanAMT, loanAmt, lblPrincipalAMT,
                principalAMT
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

    private void toggleComponents(boolean show) {
        txtAmount.setVisible(show);
        lblFullName.setVisible(show);
        fullName.setVisible(show);
        lblMID2.setVisible(show);
        MID.setVisible(show);
        error.setVisible(!show);
        lblAmt.setVisible(show);
        txtAmount.setVisible(show);
        reset.setEnabled(show);
        saveClose.setEnabled(show);
        lblLoanAMT.setVisible(show);
        loanAmt.setVisible(show);
        lblPrincipalAMT.setVisible(show);
        principalAMT.setVisible(show);
    }

    private void clearFields(){
        txtMemberID.setText("");
        txtAmount.setText("");
        fullName.setText("");
        MID.setText("");
        loanAmt.setText("");
        principalAMT.setText("");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == reset) {
            clearFields();
            toggleComponents(false);
            error.setVisible(false);
        }
        if (e.getSource() == saveClose) {
            if (txtMemberID.getText().equals("") || txtAmount.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Please fill in all fields");
            } else {
                try {
                    double amount = Double.parseDouble(txtAmount.getText());
                    double interest = (1.2 * amount)/100;
                    if (member.getGroupID() != null){
                        interest = amount/100;
                    }
                    if(invoice != null) {
                        if (amount >= invoice.getInvoiceAmount()){
                            Repayment rp = new Repayment(invoice.getInvoiceID(), amount, new Date(System.currentTimeMillis()), interest).save();
                            loan.setLoanBalance(loan.getLoanBalance() - amount);
                            loan.updateLoan();
                            invoice.setInvoiceStatus("PAID");
                            if (rp.getPaymentDate().after(invoice.getDueDate())){
                                new Penalty(loan.getLoanID(), 0.1 * invoice.getInvoiceAmount()).save();
                            }
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "Amount paid is less than the invoice amount");
                            return;
                        }
                    }
                    clearFields();
                    toggleComponents(false);
                    error.setVisible(false);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid amount", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (SQLException | IOException ex) {
                    error.setText("Could not save repayment");
                    error.setVisible(true);
                    ex.printStackTrace();
                }
            }
        }
        if (e.getSource() == search) {
            if (txtMemberID.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Please enter a member ID");
            } else {
                try {
                    member = Member.getMember(txtMemberID.getText());
                    if (member != null) {
                        loan = member.getMemberLoan();
                        if(loan != null) {
                            invoice = Invoice.getCurrentInvoice(loan.getLoanID());
                            fullName.setText(member.getFullName());
                            MID.setText(member.getMemberId());
                            loanAmt.setText("KES " + loan.getLoanBalance());
                            if (invoice != null) principalAMT.setText("KES "+invoice.getInvoiceAmount());
                            toggleComponents(true);
                        }
                        else{
                            JOptionPane.showMessageDialog(this, "Member has no loan", "Error", JOptionPane.ERROR_MESSAGE);
                            toggleComponents(false);
                            error.setVisible(false);
                        }
                    } else {
                        toggleComponents(false);
                    }
                } catch (SQLException | IOException ex) {
                    error.setText("An error occurred");
                    toggleComponents(false);
//                    ex.printStackTrace();
                }

            }
        }
    }
}
