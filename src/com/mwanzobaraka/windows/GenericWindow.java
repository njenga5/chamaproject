package com.mwanzobaraka.windows;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;

public class GenericWindow extends JFrame {
    String grpName = "Mwanzo Baraka SHG";

    public GenericWindow(LayoutManager layout, Rectangle bounds) {
        try (BufferedReader reader = new BufferedReader(new FileReader(Paths.get("").toAbsolutePath()+"/../config/config.properties"))) {
            Properties props = new Properties();
            props.load(reader);
            grpName = props.getProperty("group_name");
            setTitle(grpName);
        } catch (IOException e) {
            setTitle(grpName);
        }
        getContentPane().setBackground(new Color(182,214,198));
        setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource("/res/logo.png"))).getImage());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        if (bounds != null) setBounds(bounds);
        setLayout(layout);
    }
}
