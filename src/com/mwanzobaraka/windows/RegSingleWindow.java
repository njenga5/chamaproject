package com.mwanzobaraka.windows;

import com.mwanzobaraka.models.Member;
import com.mwanzobaraka.util.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class RegSingleWindow extends GenericWindow implements MouseListener {
    ProjectJPanel card;
    ProjectTextField txtFName, txtMName, txtLName, txtIDNumber, txtMobile, txtEmail;
    JRadioButton maleRadio, femaleRadio;
    ProjectButton reset, saveClose, saveContinue;
    int slewAmt = 20, memberCount = 0;
    ProjectLabel header;
    ButtonGroup bg;
    private String groupID = null, gender;
    JCheckBox subscribed;
    boolean isEditing = false;
    int count = 1;
    String memberID;

    public RegSingleWindow(LayoutManager layout, Rectangle bounds) {
        super(layout, bounds);
        card = new ProjectJPanel(null,1);
        card.setBounds(10, 10, 665, 370+slewAmt);
        header = new ProjectLabel("REGISTER NEW MEMBER");
        header.setFont(new Font("Roboto", Font.BOLD, 25));
        header.setXYCoordinates(170, 10, 300);

        subscribed = new JCheckBox("I confirm that the member has paid the membership fee(KES 2000)");
        subscribed.setBounds(100, 250+slewAmt, 500, 30);
        subscribed.setFocusable(false);

        card.add(subscribed);
        card.add(header);
        add(card);
        setTitle(grpName + " - Member Registration");
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        createComponents();
        setVisible(true);
    }

    public RegSingleWindow(LayoutManager layout, Rectangle bounds, String groupID, int memberCount) {
        super(layout, bounds);
        card = new ProjectJPanel(null,1);
        card.setBounds(10, 10, 665, 370+slewAmt);
        header = new ProjectLabel("Group " + groupID + " Members Registration");
        header.setFont(new Font("Roboto", Font.BOLD, 25));
        header.setXYCoordinates(120, 10, 500);
        card.add(header);
        add(card);
        this.groupID = groupID;
        this.memberCount = memberCount;
        setTitle(grpName + " - Group Members Registration");
        setResizable(false);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        createComponents();
        saveClose.setEnabled(false);
        setVisible(true);
    }

    public RegSingleWindow(LayoutManager layout, Rectangle bounds, boolean isEditing, String memberID) {
        super(layout, bounds);
        card = new ProjectJPanel(null,1);
        card.setBounds(10, 10, 665, 370+slewAmt);
        header = new ProjectLabel("EDIT MEMBER "+ memberID);
        header.setFont(new Font("Roboto", Font.BOLD, 25));
        header.setXYCoordinates(170, 10, 300);

        card.add(header);
        add(card);
        this.isEditing = isEditing;
        this.memberID = memberID;
        setTitle(grpName + " - Edit Member");
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        createComponents();
        populateFields();
        saveClose.setVisible(false);
        saveContinue.setText("Update");
        reset.setXYCoordinates(360, 320, 100);
        setVisible(true);
    }

    protected ArrayList<Component> createButtons (){
        reset = new ProjectButton("Reset", true);
        reset.addMouseListener(this);
        reset.setXYCoordinates(200, 320, 100);
        saveClose = new ProjectButton("Save and Close");
        saveClose.addMouseListener(this);
        saveClose.setXYCoordinates(310, 320, 150);
        saveClose.setBackground(Color.gray);
        saveContinue = new ProjectButton("Save and Continue");
        saveContinue.addMouseListener(this);
        saveContinue.setXYCoordinates(470, 320, 150);

        return new ArrayList<>(Arrays.asList(
                reset, saveClose, saveContinue
        ));
    }
    protected void createComponents(){

        // Labels
        ProjectLabel lblFName = new ProjectLabel("First Name*");
        lblFName.setXYCoordinates(10, 60+slewAmt);
        ProjectLabel lblMName = new ProjectLabel("Middle Name");
        lblMName.setXYCoordinates(340, 60+slewAmt);
        ProjectLabel lblLName = new ProjectLabel("Surname*");
        lblLName.setXYCoordinates(10, 110+slewAmt);
        ProjectLabel lblID = new ProjectLabel("ID Number*");
        lblID.setXYCoordinates(340, 110+slewAmt);
        ProjectLabel lblMobile = new ProjectLabel("Mobile*");
        lblMobile.setXYCoordinates(10, 150+slewAmt);
        ProjectLabel lblEmail = new ProjectLabel("Email");
        lblEmail.setXYCoordinates(340, 150+slewAmt);
        ProjectLabel lblGender = new ProjectLabel("Gender");
        lblGender.setXYCoordinates(200, 200+slewAmt);

        //Text fields
        txtFName = new ProjectTextField();
        txtFName.setXYCoordinates(130, 60+slewAmt, 200);
        txtMName = new ProjectTextField();
        txtMName.setXYCoordinates(460, 60+slewAmt, 200);
        txtLName = new ProjectTextField();
        txtLName.setXYCoordinates(130, 110+slewAmt, 200);
        txtIDNumber = new ProjectTextField();
        txtIDNumber.setDocument(new LimitDocument(8));
        txtIDNumber.setXYCoordinates(460, 110+slewAmt, 200);
        txtMobile = new ProjectTextField();
        txtMobile.setDocument(new LimitDocument(10));
        txtMobile.setXYCoordinates(130, 150+slewAmt, 200);
        txtEmail = new ProjectTextField();
        txtEmail.setXYCoordinates(460, 150+slewAmt, 200);

        // Buttons
        femaleRadio = new JRadioButton("Female");
        femaleRadio.setBounds(290, 200+slewAmt, 70, 30);
        femaleRadio.setFocusable(false);
        maleRadio = new JRadioButton("Male");
        maleRadio.setBounds(370, 200+slewAmt, 70, 30);
        maleRadio.setFocusable(false);
        bg = new ButtonGroup();
        bg.add(femaleRadio);
        bg.add(maleRadio);

        ArrayList<Component> components = new ArrayList<>(Arrays.asList(
                lblFName, txtFName, lblMName, txtMName, lblLName,
                txtLName, lblID, txtIDNumber, txtMobile, lblMobile,
                lblEmail, txtEmail, maleRadio, femaleRadio, lblGender
        ));
        components.addAll(createButtons());
        for(Component c: components){
            card.add(c);
        }

    }

    private boolean sanitizeInput(){
        if(txtFName.getText().isEmpty() || txtLName.getText().isEmpty() || txtIDNumber.getText().isEmpty() || txtMobile.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "Fields marked with(*) are required", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(!txtIDNumber.getText().matches("[0-9]+")){
            JOptionPane.showMessageDialog(this, "ID Number must be a number", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(!txtMobile.getText().matches("[0-9]+") || txtMobile.getText().length() != 10){
            JOptionPane.showMessageDialog(this, "Mobile must be a number", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(!txtEmail.getText().isEmpty() && !txtEmail.getText().matches("^[A-Za-z0-9+_.-]+@(.+)$")){
            JOptionPane.showMessageDialog(this, "Invalid email address", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (maleRadio.isSelected())gender = "Male";
        else if (femaleRadio.isSelected()) {
            gender = "Female";
        }else{
            JOptionPane.showMessageDialog(this,"Please select gender", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (subscribed != null && !subscribed.isSelected()){
            JOptionPane.showMessageDialog(this,"Please confirm that the member has paid the membership fee", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    private Member saveMember(String groupID) throws SQLException, IOException {
        String fName = txtFName.getText();
        String mName = txtMName.getText();
        String lName = txtLName.getText();
        String idNumber = txtIDNumber.getText();
        String mobile = txtMobile.getText();
        String email = txtEmail.getText();

        if (sanitizeInput()) return new Member(fName, mName, lName, idNumber, gender, mobile, email, groupID).save();
        else return null;
    }


    private void clearFields(){
        txtFName.setText("");
        txtMName.setText("");
        txtLName.setText("");
        txtIDNumber.setText("");
        txtMobile.setText("");
        txtEmail.setText("");
        bg.clearSelection();
    }

    //TODO: Add labels to side panel
    private void populateSidePanel(Member member){


    }

    private boolean cleanUp() throws SQLException, IOException {
        Member member = saveMember(groupID);
        if (member != null) {
            clearFields();
            populateSidePanel(member);
            DefaultTableModel md1 = (DefaultTableModel) MainWindow.getUTable().getModel();
            md1.addRow(new Object[]{
                    member.getMemberId(), member.getFName(), member.getMName(),
                    member.getLName(), member.getIDNumber(), member.getGender(),
                    member.getDateRegistered(), member.getGroupID()});
            return true;
        }
        return false;
    }

    private void populateFields(){
        new Thread(()->{
            try {
                Member member = Member.getMember(memberID);
               if (member != null){
                    txtFName.setText(member.getFName());
                    txtMName.setText(member.getMName());
                    txtLName.setText(member.getLName());
                    txtIDNumber.setText(member.getIDNumber());
                    txtMobile.setText(member.getMobile());
                    txtEmail.setText(member.getEmail());
                    if (member.getGender().equals("Male")) maleRadio.setSelected(true);
                    else femaleRadio.setSelected(true);


               }
            } catch (SQLException | IOException e) {
                JOptionPane.showMessageDialog(this, "Error loading member details", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }).start();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
            if (e.getSource() == reset){
                clearFields();
            } else if (e.getSource() == saveContinue) {
                try {
                    if (isEditing && sanitizeInput()){
                        try{
                         Member.updateMember(memberID, txtFName.getText(), txtMName.getText(),
                                 txtLName.getText(), txtIDNumber.getText(),gender, txtMobile.getText(),
                                 txtEmail.getText());
                        }catch (SQLException | IOException ex){
                            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    if (saveContinue.isEnabled()) {
                        if (cleanUp()){
                            if (count == memberCount - 1) {
                                saveContinue.setEnabled(false);
                                saveClose.setEnabled(true);
                            } else {
                                count += 1;
                            }
                        }
                    }
                } catch (SQLException | IOException ex) {
                    JOptionPane.showMessageDialog(this, "An error occurred with the database", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else if (e.getSource() == saveClose) {
                try {
                        if (cleanUp()) {
                            dispose();
                        }

                } catch (SQLException | IOException ex) {
                    JOptionPane.showMessageDialog(this, "An error occurred with the database", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
