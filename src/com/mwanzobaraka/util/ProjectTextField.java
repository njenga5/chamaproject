package com.mwanzobaraka.util;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import java.awt.*;

public class ProjectTextField extends JTextField {
    public ProjectTextField() {
        customize();
    }

    public ProjectTextField(String text) {
        super(text);
        customize();
    }

    public ProjectTextField(int columns) {
        super(columns);
        customize();
    }

    public ProjectTextField(String text, int columns) {
        super(text, columns);
        customize();
    }

    public ProjectTextField(Document doc, String text, int columns) {
        super(doc, text, columns);
        customize();
    }

    private void customize(){
        setHorizontalAlignment(JTextField.CENTER);
        setFont(new Font("Roboto", Font.PLAIN, 16));
    }

    public void setXYCoordinates(int x, int y, int width){
        setBounds(x, y, width, 30);
    }


}
