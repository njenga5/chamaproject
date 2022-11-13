package com.mwanzobaraka.util;

import javax.swing.*;
import java.awt.*;

public class ProjectLabel extends JLabel {
    public ProjectLabel(){
        customize();
    }
    public ProjectLabel(String text){
        super(text);
        customize();
    }

    public ProjectLabel(Icon image, int horizontalAlignment) {
        super(image, horizontalAlignment);
        customize();
    }

    public ProjectLabel(Icon image) {
        super(image);
        customize();
    }

    public ProjectLabel(String text, Icon icon, int horizontalAlignment) {
        super(text, icon, horizontalAlignment);
        customize();
    }

    public ProjectLabel(String text, int horizontalAlignment) {
        super(text, horizontalAlignment);
        customize();
    }

    private void customize(){
        setBackground(new Color(1,22,39, 10));
        setForeground(new Color(1,22,39));
        setHorizontalAlignment(JLabel.CENTER);
        setOpaque(true);
    }

    public void setXYCoordinates(int x, int y){
        setBounds(x, y, 110, 30);
    }
    public void setXYCoordinates(int x, int y, int width){
        setBounds(x, y, width, 30);
    }

}
