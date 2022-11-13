package com.mwanzobaraka.windows;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GenericWindow extends JFrame {
    String grpName = "Mwanzo Baraka SHG";

    public GenericWindow(LayoutManager layout, Rectangle bounds) {
        try (InputStream stream = getClass().getResourceAsStream("config.properties")) {
            Properties props = new Properties();
            props.load(stream);
            grpName = props.getProperty("group_name");
            setTitle(grpName);
        } catch (IOException e) {
            setTitle(grpName);
        }
        getContentPane().setBackground(new Color(182,214,198));
        setIconImage(new ImageIcon("src\\com\\mwanzobaraka\\res\\logo.png").getImage());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        if (bounds != null) setBounds(bounds);
        setLayout(layout);
    }
}
