package com.mwanzobaraka.windows;

import com.mwanzobaraka.util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;

public class LoginWindow extends GenericWindow implements MouseListener {
    ProjectJPanel panel;
    ProjectButton close, submit;
    ProjectTextField memberID;
    JPasswordField password;
    public LoginWindow(LayoutManager layout, Rectangle bounds) {
        super(layout, bounds);
        panel = new ProjectJPanel(null, true, 3);
        panel.setBounds(20, 50, 450, 350);
        add(panel);
        createComponents();
        setResizable(false);
        setVisible(true);
    }

    public void createComponents(){
        // Labels
        ProjectLabel logo = new ProjectLabel(new ImageIcon("src\\com\\mwanzobaraka\\res\\logo.png"), 0);
        logo.setBounds(195, 5, 100, 100);
        ProjectLabel header = new ProjectLabel("LOGIN");
        header.setFont(new Font("Roboto", Font.BOLD, 25));
        header.setXYCoordinates(150, 110, 180);
        ProjectLabel lblID = new ProjectLabel("MemberID");
        lblID.setXYCoordinates(5, 170);
        ProjectLabel lblPass = new ProjectLabel("Password");
        lblPass.setXYCoordinates(5, 220);

        // Buttons
        close = new ProjectButton("Exit", true);
        close.setXYCoordinates(180, 270, 100);
        close.addMouseListener(this);
        submit  = new ProjectButton("Login");
        submit.addMouseListener(this);
        submit.setXYCoordinates(300, 270, 100);

        // Text fields
        memberID = new ProjectTextField();
        memberID.setDocument(new LimitDocument(6));
        memberID.setXYCoordinates(120, 170, 300);
        password = new JPasswordField();
        password.setBounds(120, 220, 300, 30);
        password.setHorizontalAlignment(JTextField.CENTER);

        ArrayList <Component> components = new ArrayList<>(Arrays.asList(
                memberID, password, header, lblID, lblPass, close, submit, logo
        ));
        for (Component c: components){
            panel.add(c);
        }
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource() == close) this.dispose();
        else if (e.getSource() == submit) {
            this.dispose();
            new MainWindow(new BorderLayout(3, 3), null);
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
        if (e.getSource() == submit){
            submit.setForeground(new Color(216,217,218));
            submit.setBackground(new Color(22,61,93));
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (e.getSource() == submit){
            submit.setBackground(new Color(1,22,39));
            submit.setForeground(new Color(175, 177, 179));
        }
    }
}
