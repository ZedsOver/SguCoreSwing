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
import parsers.Pkg_box;

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
        if (dax == null) {
            return null;
        }
        final Object a = dax.get(rowIndex);
        if (columnIndex < 3 || columnIndex > 4 || a == null) {
            return a;
        }
        if (a.getClass() == IndexHelper.IEntry.class) {
            //fix size info
            rowIndex = dax.getRealIndex(rowIndex);
            final Object ofas = dax.getPIEntry().offs;
            if (ofas instanceof long[]) {
                switch (columnIndex) {
                    case 3://offset
                        return ((long[]) ofas)[rowIndex];
                    case 4://size 
                        final long[] ax = (long[]) ofas;
                        return ax[rowIndex + 1] - ax[rowIndex];
                }
            }
            else if (ofas instanceof long[][]) {
                switch (columnIndex) {
                    case 3://offset
                        return ((long[][]) ofas)[rowIndex][0];
                    case 4://size 
                        final long[] ax = ((long[][]) ofas)[rowIndex];
                        return ax[1] - ax[0];
                }
            }
            else {
                return 0L;
            }

        }
        else if (a instanceof IndexHelper.SEntry) {
            switch (columnIndex) {
                case 3://offset
                    if (((IndexHelper.SEntry) a).isrep) {
                        rowIndex = dax.getRealIndex(rowIndex);
                        final Object ofas = dax.getPIEntry().offs;
                        if (ofas instanceof long[]) {
                            return ((long[]) ofas)[rowIndex];
                        }
                        else if (ofas instanceof long[][]) {
                            return ((long[][]) ofas)[rowIndex][0];
                        }
                    }
                    return 0L;
                case 4://size 
                    return ((IndexHelper.SEntry) a).size;
            }
        }
        else {
            switch (columnIndex) {
                case 3://offset
                    return 0L;
                case 4://size 
                    return (long) ((IndexHelper.VEntry) a).length;
            }
        }
        return a;
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
                ve.add(index, oe.length);
            }
            IndexHelper.IEntry ka = new IndexHelper.SEntry(a);
            ka.code = 0x42494e00;
            ka.modifier |= IndexHelper.UNK | BasicFile.TYPE_BIN;
            oe.add(ka);
            dax.needRzCount++;
        }
        super.fireTableDataChanged();
    }

    //only can replace one file???
    boolean replaceFiles(File[] files, int index)
    {
        IndexHelper.VEntry ve = dax.getPVEntry();
        IndexHelper.IEntry oe = dax.getPIEntry();
        duck:
        if (files.length > 0) {
            if (ve == null) {
                break duck;
            }
            Object ax = ve.get(index);
            if (!(ax instanceof IndexHelper.VEntry)) {
                break duck;
            }
            ve = (IndexHelper.VEntry) ax;
            for (File a : files) {
                ve.add(oe.length);
                IndexHelper.IEntry ka = new IndexHelper.SEntry(a);
                ka.code = 0x42494e00;
                ka.modifier |= IndexHelper.UNK | BasicFile.TYPE_BIN;
                oe.add(ka);
                dax.needRzCount++;
            }
            return true;
        }
        if (files.length != 1) {
            return false;
        }
        IndexHelper.IEntry src = (IndexHelper.IEntry) dax.get(index);

        if (src instanceof IndexHelper.SEntry) {
            ((IndexHelper.SEntry) src).source = files[0];
            ((IndexHelper.SEntry) src).size = files[0].length();
            if (((IndexHelper.SEntry) src).isrep) {//retain original name if is a replacing
                IndexHelper.setName(src, ((IndexHelper.IEntry) dax.get(index)).desc);
            }
            else {
                IndexHelper.setName(src, files[0].getName());
            }
            if (((IndexHelper.SEntry) src).needRsz) {
                dax.needRzCount--;
            }
            else {
                dax.replaceCount--;
            }
        }
        else {
            IndexHelper.SEntry ka = new IndexHelper.SEntry(files[0]);
            ka.isrep = true;
            ka.isrc = src;
            ka.code = 0x42494e00;
            ka.modifier |= IndexHelper.UNK | BasicFile.TYPE_BIN;
            src = ka;
        }
        index = dax.getRealIndex(index);
        final boolean fl = dax.useFlatOPath;
        try {
            Pkg_box a = dax.getRoot();
            dax.useFlatOPath = true;
            IndexHelper.IEntry ost = dax.getIEntry();
            final int[] pa = dax.getPath();
            final int sz = dax.level;
            dum:
            for (int i = 0; i < sz; i++) {
                ost = ost.get(pa[i]);
                for (BasicFile b : a.forParsers()) {
                    if (b.isValidCode(ost.code)) {
                        a = (Pkg_box) b;
                        continue dum;
                    }
                }
                throw new RuntimeException();
            }
            a.offsets = dax.getPIEntry().offs;

            ((IndexHelper.SEntry) src).needRsz = files[0].length() > a.genMax(a.sizeOf(index), index);
            if (((IndexHelper.SEntry) src).needRsz) {
                dax.needRzCount++;
            }
            else {
                dax.replaceCount++;
            }
        }
        finally {
            dax.useFlatOPath = fl;
        }
        oe.set(index, src);
        return true;
    }

}
