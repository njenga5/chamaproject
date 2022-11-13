package com.mwanzobaraka.util;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.Vector;

public class ProjectTable extends JTable {
    public ProjectTable() {
        customise();
    }

    public ProjectTable(TableModel dm) {
        super(dm);
        customise();
    }

    public ProjectTable(TableModel dm, TableColumnModel cm) {
        super(dm, cm);
    }

    public ProjectTable(TableModel dm, TableColumnModel cm, ListSelectionModel sm) {
        super(dm, cm, sm);
    }

    public ProjectTable(int numRows, int numColumns) {
        super(numRows, numColumns);
    }

    public ProjectTable(Vector<? extends Vector> rowData, Vector<?> columnNames) {
        super(rowData, columnNames);
    }

    public ProjectTable(Object[][] rowData, Object[] columnNames) {
        super(rowData, columnNames);
    }

    private void customise(){
        setBackground(new Color(230, 240, 230));
        setAutoCreateRowSorter(true);
        setRowHeight(25);
        setColumnSelectionAllowed(false);
    }
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
