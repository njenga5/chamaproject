package com.mwanzobaraka.util;

import javax.swing.*;
import java.awt.*;

public class ProjectJPanel extends JPanel {
    public ProjectJPanel(LayoutManager layout, boolean isDoubleBuffered, int thickness) {
        super(layout, isDoubleBuffered);
        customise(thickness);
    }

    public ProjectJPanel(LayoutManager layout, int thickness) {
        super(layout);
        customise(thickness);
    }

    public ProjectJPanel(boolean isDoubleBuffered, int thickness) {
        super(isDoubleBuffered);
        customise(thickness);
    }

    public ProjectJPanel(int thickness) {
        customise(thickness);
    }

    private void customise(int thickness){
        setBorder(BorderFactory.createLineBorder(new Color(126, 87, 194), thickness, true));
        setBackground(new Color(204, 219, 222));
    }
}
