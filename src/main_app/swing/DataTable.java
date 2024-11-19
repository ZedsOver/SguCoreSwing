/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main_app.swing;

import java.io.File;
import javax.swing.table.AbstractTableModel;
import main_app.IndexHelper;
import main_app.InterfaceExplorer;
import parsers.BasicFile;

/**
 *
 * @author armax
 */
public class DataTable extends AbstractTableModel {

    InterfaceExplorer dax;
    static final String[] columnsIds = new String[]{
        "id", "Type", "Name", "Offset", "Size", "Max size"
    };
    static final Class[] types = new Class[]{
        java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Long.class, java.lang.Long.class, java.lang.Long.class
    };
    static final boolean[] canEdit = new boolean[]{
        false, false, true, false, true, false
    };

    @Override
    public String getColumnName(int column)
    {
        return columnsIds[column];
    }

    public Class getColumnClass(int columnIndex)
    {
        return types[columnIndex];
    }

    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return canEdit[columnIndex];
    }

    @Override
    public int getRowCount()
    {
        return dax == null || dax.size() == 0 ? 1 : dax.size();
    }

    @Override
    public int getColumnCount()
    {
        return 6;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        return dax == null ? null : dax.get(rowIndex);
    }

    @Override
    public void setValueAt(Object aValue, int row, int column)
    {
        Object cc = dax.get(row);
        if (cc instanceof IndexHelper.IEntry) {
            IndexHelper.IEntry dat = (IndexHelper.IEntry) cc;
            switch (column) {
                case 0://ID
                    break;
                case 1://TYPE
                    break;
                case 2://NAME
                    IndexHelper.setName(dat, (String) aValue);
                    break;
                case 3://OFFSET
                    break;
                case 4://SIZE
                    break;
                case 5://MAX SIZE
                    break;
            }
        }
        else {
            IndexHelper.VEntry dat = (IndexHelper.VEntry) cc;
            switch (column) {
                case 0://ID
                    break;
                case 1://TYPE
                    break;
                case 2://NAME
                    dat.desc = (String) aValue;
                    break;
                case 3://OFFSET
                    break;
                case 4://SIZE
                    break;
                case 5://MAX SIZE
                    break;
            }
        }

    }

    public void move(int[] rowFrom, int index)
    {
        Object[] ants = new Object[3];
        ants[0] = rowFrom;
        dax.getVPath(ants, 1, -1);
        dax.moveTo(ants, index, false);
    }

    public boolean putIntoVDir(int[] rowFrom, int index)
    {
        Object[] ants = new Object[3];
        ants[0] = rowFrom;
        dax.getVPath(ants, 1, index);
        if (ants[2] == null) {
            return false;
        }
        dax.moveTo(ants, false);
        return true;
    }

    //can be add many files
    void addFiles(File[] files, int index)
    {
        IndexHelper.IEntry oe = dax.getPIEntry();
        IndexHelper.VEntry ve = dax.getPVEntry();
        for (File a : files) {
            if (ve != null) {
                ve.add(oe.length);
            }
            IndexHelper.IEntry ka = new IndexHelper.SEntry(a);
            ka.code = 0x42494e00;
            ka.modifier |= IndexHelper.UNK | BasicFile.TYPE_BIN;
            oe.add(ka);
        }
        super.fireTableDataChanged();
    }

    //only can replace one file???
    boolean replaceFiles(File[] files, int index)
    {
        IndexHelper.VEntry ve = dax.getPVEntry();
        IndexHelper.IEntry oe = dax.getPIEntry();
        if (files.length > 1) {
            if (ve == null) {
                return false;
            }
            Object ax = ve.get(index);
            if (!(ax instanceof IndexHelper.VEntry)) {
                return false;
            }
            ve = (IndexHelper.VEntry) ax;
            for (File a : files) {
                ve.add(oe.length);
                IndexHelper.IEntry ka = new IndexHelper.SEntry(a);
                ka.code = 0x42494e00;
                ka.modifier |= IndexHelper.UNK | BasicFile.TYPE_BIN;
                oe.add(ka);
            }
            return true;
        }
        if (files.length != 1) {
            return false;
        }
        IndexHelper.IEntry ka = new IndexHelper.SEntry(files[0]);
        ka.code = 0x42494e00;
        ka.modifier |= IndexHelper.UNK | BasicFile.TYPE_BIN;
        oe.set(index, ka);
        return true;
    }

}
