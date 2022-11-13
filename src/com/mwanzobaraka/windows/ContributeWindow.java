package com.mwanzobaraka.windows;

import com.mwanzobaraka.models.Contribution;
import com.mwanzobaraka.models.Member;
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

public class ContributeWindow extends GenericWindow implements ActionListener {
    ProjectJPanel card;
    ProjectTextField txtMemberID, txtAmount;
    ProjectButton reset, saveClose, search;
    ProjectLabel lblFullName, fullName, lblMID2, MID, error, lblAmt;
    Member member = null;
    public ContributeWindow(LayoutManager layout, Rectangle bounds) {
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

        ProjectLabel header = new ProjectLabel("ADD MEMBER CONTRIBUTION");
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
        error = new ProjectLabel("No member with that ID was found");
        error.setVisible(false);
        error.setForeground(Color.red);
        error.setXYCoordinates(10, 90, 500);
        ArrayList<Component> bottomComponents = new ArrayList<>(Arrays.asList(
                lblFullName, fullName, lblMID2, MID, error
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
    }

    private void clearFields(){
        txtMemberID.setText("");
        txtAmount.setText("");
        fullName.setText("");
        MID.setText("");
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
                assert member != null;
                try {
                    if (member.getGroupID() == null) {
                        new Contribution(member.getMemberId(), new Date(System.currentTimeMillis()), Double.parseDouble(txtAmount.getText()), null, null).save();
                    } else {
                        double amount = Double.parseDouble(txtAmount.getText());
                        double groupContribution = 200.0;
                        double mShare = amount - groupContribution;
                        new Contribution(member.getMemberId(), new Date(System.currentTimeMillis()), mShare, groupContribution, member.getGroupID()).save();
                    }
                    clearFields();
                    toggleComponents(false);
                    error.setVisible(false);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid amount");
                } catch (SQLException | IOException ex) {
                    error.setText("Could not save contribution");
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
                        toggleComponents(true);
                        fullName.setText(member.getFullName());
                        MID.setText(member.getMemberId());
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
