package com.mwanzobaraka.windows;

import com.mwanzobaraka.models.Group;
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

public class RegGroupWindow extends GenericWindow implements MouseListener {
    ProjectTextField txtGName, spNMembers;
//    JSpinner spNMembers;
    ProjectButton reset, saveClose;
    ProjectJPanel card;
    ProjectLabel header;
    JCheckBox subscribed;
    String groupID;
    int slewAmt = 20, max=1000, min=1;
    public RegGroupWindow(LayoutManager layout, Rectangle bounds) {
        super(layout, bounds);
        setTitle(grpName + " - Group Registration");
        card = new ProjectJPanel(null,1);
        card.setBounds(10, 10, 665, 370+slewAmt);
        header = new ProjectLabel("REGISTER NEW GROUP");
        header.setFont(new Font("Roboto", Font.BOLD, 25));
        header.setXYCoordinates(170, 10, 300);
        groupID = "G" + (int)(Math.random() * (max-min+1)+min);
        card.add(header);
        add(card);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        createComponents();

    }

    protected void createComponents(){
        ProjectLabel groupName = new ProjectLabel("Group Name*");
        groupName.setXYCoordinates(10, 60+slewAmt);
        ProjectLabel memberCount = new ProjectLabel("# of Members*");
        memberCount.setXYCoordinates(390, 60+slewAmt);

        txtGName = new ProjectTextField();
        txtGName.setXYCoordinates(130, 60+slewAmt, 250);
//        spNMembers = new JSpinner(new SpinnerNumberModel(2, 2, 60, 1));
        spNMembers = new ProjectTextField();
        spNMembers.setDocument(new LimitDocument(2));
        spNMembers.setText("2");
        spNMembers.setBounds(510, 60+slewAmt, 100, 30);
        subscribed = new JCheckBox("I confirm that the group has paid the group fee(KES 5000)");
        subscribed.setBounds(100, 250+slewAmt, 500, 30);
        subscribed.setFocusable(false);
        ArrayList<Component> components = new ArrayList<>(Arrays.asList(
                groupName, memberCount, txtGName, spNMembers, subscribed
        ));

        components.addAll(createButtons());

        for (Component c: components){
            if (c instanceof ProjectButton){
                c.addMouseListener(this);
            }
            card.add(c);
        }

    }

    private ArrayList<? extends Component> createButtons() {
        reset = new ProjectButton("Reset", true);
        reset.setXYCoordinates(360, 320, 100);
        saveClose = new ProjectButton("Save");
        saveClose.setXYCoordinates(470, 320, 150);

        return new ArrayList<>(Arrays.asList(
                reset, saveClose
        ));
    }

    private Group saveGroup() throws SQLException, IOException {
        String name = txtGName.getText();
        int members = Integer.parseInt(spNMembers.getText());
        if (name.isEmpty() || members < 2){
            JOptionPane.showMessageDialog(this, "Fields marked with (*) are required");
            return null;
        }
        if (!subscribed.isSelected()){
            JOptionPane.showMessageDialog(this, "Please confirm that the group has paid the group fee");
            return null;
        }
        return new Group(groupID, name, members).save();
    }

    private void clearFields(){
        txtGName.setText("");
        spNMembers.setText("2");
        subscribed.setSelected(false);
    }

    private void cleanUp() throws SQLException, IOException {
        Group group = saveGroup();
        if (group != null){
            clearFields();
            //TODO: Add code to add the group details to the side panel

            DefaultTableModel model = (DefaultTableModel) MainWindow.getLTable().getModel();
            model.addRow(new Object[]{group.getGroupID(), group.getGroupName(), group.getNoOfMembers(), group.getDateRegistered()});
            dispose();
            new RegSingleWindow(null, new Rectangle(300, 40, 700, 450), groupID, Integer.parseInt(spNMembers.getText()));
        }
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == reset){
            clearFields();
        }else if (e.getSource() == saveClose){
            try {
                cleanUp();
            } catch (SQLException | IOException ex) {
                ex.printStackTrace();
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
