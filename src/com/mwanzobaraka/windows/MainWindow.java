package com.mwanzobaraka.windows;

import com.mwanzobaraka.models.Group;
import com.mwanzobaraka.models.Member;
import com.mwanzobaraka.util.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class MainWindow extends GenericWindow implements ActionListener {
    ProjectJPanel centerPanel,topPanel, rightPanel, statusBar, containerCenter;
    ProjectButton regNewGrp, regNewMember, viewReports, addCont, lend ,repay;
    DefaultTableModel model1, model2;
    static ProjectLabel lHeader, uHeader, membersHeader, groupsHeader;
    static ProjectTable uTable = new ProjectTable();
    static ProjectTable lTable = new ProjectTable();
    TableRowSorter <DefaultTableModel> sorter1, sorter2;
    public MainWindow(LayoutManager layout, Rectangle bounds) {
        super(layout, bounds);
//        GraphicsEnvironment graphics = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        GraphicsDevice dev = graphics.getDefaultScreenDevice();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();
        CreateTables.create();
        centerPanel = new ProjectJPanel(new BorderLayout(), 1);
        topPanel = new ProjectJPanel(new BorderLayout(), 1);
        topPanel.setPreferredSize(new Dimension(0, 100));
        rightPanel = new ProjectJPanel(new BorderLayout(),1);
        rightPanel.setPreferredSize(new Dimension(250, 0));
        statusBar = new ProjectJPanel(1);
        statusBar.setPreferredSize(new Dimension(0, 20));
        model1 = new DefaultTableModel(0, 0);
        model2 = new DefaultTableModel(0, 0);

        populateTopPanel();
        populateCenterPanel();
        populateRightPanel();

        add(centerPanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);
        add(rightPanel, BorderLayout.EAST);
        add(statusBar, BorderLayout.SOUTH);
        addItemsToTable();
        MainWindowHelper.addDividends();
        MainWindowHelper.addInvoices();
        MainWindowHelper.penalise();
        setBounds(0, 0, width, height);
        setVisible(true);
//        dev.setFullScreenWindow(this);


    }

    private void populateTopPanel(){
        ProjectJPanel containerRight = new ProjectJPanel(null, 0);
        containerRight.setPreferredSize(new Dimension(950, 0));
        ProjectJPanel containerLeft = new ProjectJPanel(null, 0);
        ProjectLabel logo = new ProjectLabel(grpName, new ImageIcon("src\\com\\mwanzobaraka\\res\\logo.png"), 0);
        logo.setFont(new Font("Roboto", Font.ITALIC, 30));
        logo.setBackground(null);
        logo.setBounds(0, 0, 400, 100);
        containerLeft.add(logo);

        regNewMember = new ProjectButton("Add New Member");
        regNewMember.addActionListener(this);
        regNewMember.setXYCoordinates(60, 30, 140);
        regNewGrp = new ProjectButton("Add New Group");
        regNewGrp.addActionListener(this);
        regNewGrp.setXYCoordinates(210, 30, 140);
        viewReports = new ProjectButton("View Reports");
        viewReports.setXYCoordinates(810, 30, 135);

        addCont = new ProjectButton("Add Contribution");
        addCont.addActionListener(this);
        addCont.setXYCoordinates(360, 30, 140);
        lend = new ProjectButton("Issue Loan");
        lend.addActionListener(this);
        lend.setXYCoordinates(510, 30, 140);
        repay = new ProjectButton("Repay Loan");
        repay.addActionListener(this);
        repay.setXYCoordinates(660, 30, 140);

        ArrayList<Component> components = new ArrayList<>(Arrays.asList(
           regNewGrp, regNewMember, viewReports, addCont, lend, repay
        ));

        for (Component c: components){
            containerRight.add(c);
        }
        topPanel.add(containerRight, BorderLayout.EAST);
        topPanel.add(containerLeft, BorderLayout.CENTER);
    }
    private void populateCenterPanel(){

        String [] mColumns = {"Member ID", "First Name", "Middle Name", "Last Name", "ID No", "Gender", "Date Registered", "Group"};
        String [] gColumns = {"Group ID", "Group Name", "Number of Members", "Date Registered"};

        model1.setColumnIdentifiers(mColumns);
        uTable.setModel(model1);
        sorter1 = new TableRowSorter<>(model1);
        uTable.setRowSorter(sorter1);

        model2.setColumnIdentifiers(gColumns);
        lTable.setModel(model2);
        sorter2 = new TableRowSorter<>(model2);
        lTable.setRowSorter(sorter2);

        uTable.getSelectionModel().addListSelectionListener(e -> {
                    if (!e.getValueIsAdjusting()){
                        int row = uTable.getSelectedRow();
                        if (row != -1){
                            uTable.clearSelection();
                            new RegSingleWindow(null, new Rectangle(300, 40, 700, 450), true,(String) uTable.getValueAt(row, 0));
                        }
                    }
                }
        );

        JScrollPane sp = new JScrollPane(uTable);
        JScrollPane sp2 = new JScrollPane(lTable);

        membersHeader = new ProjectLabel("Upper Table Overview");
        membersHeader.setFont(new Font("Roboto", Font.PLAIN, 30));
        membersHeader.setXYCoordinates(300, 10, 400);
        ProjectLabel lblSearch1 = new ProjectLabel("Search");
        lblSearch1.setXYCoordinates(790, 10, 100);
        ProjectTextField search = new ProjectTextField();
        search.setXYCoordinates(890, 10, 200);
        search.setToolTipText("Search");

        search.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchMembers(search.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchMembers(search.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchMembers(search.getText());
            }
            public void searchMembers(String str){
                if (str.isEmpty()){
                    sorter1.setRowFilter(null);
            }else {
                    sorter1.setRowFilter(RowFilter.regexFilter("(?i)" + str));
                }
            }
        }
        );

        groupsHeader = new ProjectLabel("Lower Table Overview");
        groupsHeader.setFont(new Font("Roboto", Font.PLAIN, 30));
        groupsHeader.setXYCoordinates(300, 10, 400);
        ProjectLabel lblSearch2 = new ProjectLabel("Search");
        lblSearch2.setXYCoordinates(790, 10, 100);
        ProjectTextField search2 = new ProjectTextField();
        search2.setXYCoordinates(890, 10, 200);
        search2.setToolTipText("Search");

        search2.getDocument().addDocumentListener(new DocumentListener() {
             @Override
             public void insertUpdate(DocumentEvent e) {
                 searchGroups(search2.getText());
             }

             @Override
             public void removeUpdate(DocumentEvent e) {
                 searchGroups(search2.getText());
             }

             @Override
             public void changedUpdate(DocumentEvent e) {
                 searchGroups(search2.getText());
             }
             public void searchGroups(String str){
                 if (str.isEmpty()){
                     sorter2.setRowFilter(null);
                 }else {
                     sorter2.setRowFilter(RowFilter.regexFilter("(?i)" + str));
                 }
             }
         }
        );

        ProjectJPanel containerTop = new ProjectJPanel(null, 1);
        containerTop.setPreferredSize(new Dimension(0, 50));
        ProjectJPanel containerBottom = new ProjectJPanel(new BorderLayout(), 1);
        containerBottom.setPreferredSize(new Dimension(0, 280));
        ProjectJPanel headerHolder = new ProjectJPanel(null, 1);
        headerHolder.setPreferredSize(new Dimension(0, 50));
        headerHolder.add(groupsHeader);
        headerHolder.add(search2);
        headerHolder.add(lblSearch2);

        containerBottom.add(headerHolder, BorderLayout.NORTH);
        containerBottom.add(sp2);
        containerTop.add(membersHeader);
        containerTop.add(search);
        containerTop.add(lblSearch1);
        centerPanel.add(sp);
        centerPanel.add(containerTop, BorderLayout.NORTH);
        centerPanel.add(containerBottom, BorderLayout.SOUTH);
    }

    private void populateRightPanel(){
        ProjectJPanel containerTop = new ProjectJPanel(null, 1);
        containerTop.setPreferredSize(new Dimension(0, 50));
        containerCenter = new ProjectJPanel(null, 1);

        ArrayList<ProjectButton> components =  MainWindowHelper.createButtons();
        for (ProjectButton c: components){
            c.addActionListener(this);
            containerCenter.add(c);
        }

        ProjectJPanel containerBottom = new ProjectJPanel(new BorderLayout(), 1);
        containerBottom.setPreferredSize(new Dimension(0, 280));

        ProjectJPanel containerBottomTop = new ProjectJPanel(null, 1);
        containerBottomTop.setPreferredSize(new Dimension(0, 50));
        ProjectJPanel containerBottomCenter = new ProjectJPanel(null, 1);

        ArrayList<ProjectButton> components2 =  MainWindowHelper.createLowerButtons();
        for (ProjectButton c: components2){
            c.addActionListener(this);
            containerBottomCenter.add(c);
        }


        uHeader = new ProjectLabel("Quick Controls");
        uHeader.setFont(new Font("Roboto", Font.PLAIN, 30));
        uHeader.setXYCoordinates(0, 10, 250);

//        lHeader = new ProjectLabel("Lower Controls");
//        lHeader.setFont(new Font("Roboto", Font.PLAIN, 30));
//        lHeader.setXYCoordinates(0, 10, 250);
//
//        containerBottomTop.add(lHeader);

        containerTop.add(uHeader);
        containerBottom.add(containerBottomTop, BorderLayout.NORTH);
        containerBottom.add(containerBottomCenter, BorderLayout.CENTER);


        rightPanel.add(containerTop, BorderLayout.NORTH);
        rightPanel.add(containerCenter, BorderLayout.CENTER);
        rightPanel.add(containerBottom, BorderLayout.SOUTH);

    }
    private void addItemsToTable(){
        Thread thread1 = new Thread(()->{
            try{
                for(Member member : Member.getAllMembers()){
                    model1.addRow(new Object[]{
                            member.getMemberId(), member.getFName(), member.getMName(),
                            member.getLName(), member.getIDNumber(), member.getGender(),
                            member.getDateRegistered(), member.getGroupID()
                    });
                }
            }catch(SQLException | IOException e){
                JOptionPane.showMessageDialog(this, "Query Error", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }

        });
        Thread thread2 =  new Thread(()->{
            try {
                for (Group g: Group.getGroups()){
                    model2.addRow(new Object[]{
                            g.getGroupID(), g.getGroupName(), g.getNoOfMembers(), g.getDateRegistered()
                    });
                }
            } catch (SQLException | IOException e) {
                JOptionPane.showMessageDialog(this, "Query Error g", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        thread1.start();
        thread2.start();
    }

    public static ProjectTable getUTable(){
        return uTable;
    }

   public static ProjectTable getLTable(){
        return lTable;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        uTable.clearSelection();
        lTable.clearSelection();
        MainWindowHelper.onClick(e);
        if (e.getSource() == regNewMember){
            new RegSingleWindow(null, new Rectangle(300, 40, 700, 450));
        }else if (e.getSource() == regNewGrp){
            new RegGroupWindow(null, new Rectangle(300, 40, 700, 450));
        }
        else if (e.getSource() == addCont) {
            new ContributeWindow(null, new Rectangle(300, 40, 700, 450));
        }else if (e.getSource() == lend) {
            new LendingWindow(null, new Rectangle(300, 40, 700, 450));
        }else if (e.getSource() == repay) {
            new RepaymentWindow(null, new Rectangle(300, 40, 700, 450));
        }
    }
}
