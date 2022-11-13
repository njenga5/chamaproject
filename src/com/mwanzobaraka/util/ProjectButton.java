package com.mwanzobaraka.util;

import javax.swing.*;
import java.awt.*;

public class ProjectButton extends JButton {
    private boolean isSecondary;
    public ProjectButton() {
        customize();
    }
    public ProjectButton(boolean isSecondary) {
        this.isSecondary = isSecondary;
        customize();
    }

    public ProjectButton(Icon icon) {
        super(icon);
        customize();
    }

    public ProjectButton(String text, boolean isSecondary) {
        super(text);
        this.isSecondary = isSecondary;
        customize();
    }
    public ProjectButton(String text) {
        super(text);
        customize();
    }

    public ProjectButton(Action a) {
        super(a);
        customize();
    }

    public ProjectButton(String text, Icon icon) {
        super(text, icon);
        customize();
    }

    private void customize(){
        setFocusable(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
//        setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(), BorderFactory.createEtchedBorder()));
        if (isSecondary){
            setBackground(new Color(199, 95, 128, 242));
            setForeground(new Color(175,177,179));

        }else {
            setBackground(new Color(3, 25, 44));
            setForeground(new Color(175, 177, 179));
        }

    }
    public void setXYCoordinates(int x, int y, int width){
        setBounds(x, y, width, 40);
    }
}
