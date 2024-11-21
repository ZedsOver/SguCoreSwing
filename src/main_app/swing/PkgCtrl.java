/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package main_app.swing;

import com.DeltaSKR.IO.interfce.IOSys;
import com.DeltaSKR.IO.interfce.RndAccess;
import com.DeltaSKR.lang.ArrayUtil;
import com.DeltaSKR.lang.PrimitiveList;
import com.sgucore.data.AFS;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.Enumeration;
import javax.swing.DefaultRowSorter;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.RowFilter;
import javax.swing.table.TableColumn;
import main_app.IndexHelper;
import main_app.InterfaceExplorer;
import parsers.Pkg_box;
import com.sgucore.data.ISO_Root;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author armax
 */
public class PkgCtrl extends javax.swing.JPanel {

    public final static Object[] open(File file, String ext)
    {
        try {
            RndAccess a = IOSys.openRandom(file, true);
            try {
                Pkg_box iso;
                if (ext.equals("afs")) {
                    iso = new AFS(null);
                    iso.setIO(a);
                    if (!iso.isValid(-1)) {
                        throw new IOException("Error invalid file");
                    }
                }
                else if (ext.equals("iso")) {
                    iso = new ISO_Root(null);
                    iso.setIO(a);
                    if (!iso.isValid(-1)) {
                        throw new IOException("Error invalid file");
                    }
                }
                else {
                    throw new IOException("unsupported file type");
                }
                IndexHelper.IEntry entry = new IndexHelper.IEntry(1);
                iso.printOffset(entry, iso.getCType(), false, false);
                return new Object[]{iso, entry.get(0)};
            }
            finally {
                a.close();
            }
        }
        catch (Exception ex) {

        }
        return null;
    }
    final InterfaceExplorer ax = new InterfaceExplorer() {
        ActionListener ata = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt)
            {
                goTo((navBar2.CurrentDir.getComponentZOrder((JComponent) evt.getSource()) + 1) / 2);
            }
        };

        @Override
        public int getSelected()
        {
            return jTable1.convertColumnIndexToModel(jTable1.getSelectedRow());
        }

        @Override
        public int[] getSelecteds()
        {
            int[] jc = jTable1.getSelectedRows();
            if (jc != null) {
                for (int c = 0; c < jc.length; c++) {
                    jc[c] = jTable1.convertColumnIndexToModel(jc[c]);
                }
            }
            return fixSelection(jc);
        }

        @Override
        protected void clearSelection()
        {
            jTable1.clearSelection();
        }

        @Override
        protected void updateUI(int p)
        {
            jTable1.getRowSorter().allRowsChanged();
            jTable1.updateUI();
            p = size() > 0 ? (p < 0 ? 0 : (p >= size() ? size() - 1 : p)) : -1;
            if (p != -1) {
                try {
                    p = jTable1.convertRowIndexToView(p);
                    jTable1.setRowSelectionInterval(p, p);
                    jTable1.scrollRectToVisible(jTable1.getCellRect(p, 0, true));
                }
                catch (Exception e) {
                }
            }
        }

        @Override
        protected void updateHistory(PrimitiveList<InterfaceExplorer.PathItem[]> path, int cur)
        {
            navBar2.updatePath(path, cur, ata);
        }
    };
    final DataTable tableref = new DataTable();
    char[] cfilt;
    final RowFilter filter = new RowFilter() {
        @Override
        public boolean include(RowFilter.Entry entry)
        {
            Object a = entry.getValue(0);
            String b = a == null ? null : a instanceof IndexHelper.IEntry
                    ? ((IndexHelper.IEntry) a).desc
                    : ((IndexHelper.VEntry) a).desc;
            if (b == null || b.isEmpty()) {
                return false;
            }
            for (char ac : cfilt) {
                if (b.indexOf(ac) < 0) {
                    return false;
                }
            }
            return true;
        }

    };

    private static class ColumnSetter {

        private TableColumn col;

        public ColumnSetter ini(JTable src, int c)
        {
            this.col = src.getColumnModel().getColumn(c);
            return this;
        }

        public ColumnSetter ini(TableColumn col)
        {
            this.col = col;
            return this;
        }

        public ColumnSetter setModelIndex(int modelIndex)
        {
            col.setModelIndex(modelIndex);
            return this;
        }

        public ColumnSetter setIdentifier(Object identifier)
        {
            col.setIdentifier(identifier);
            return this;
        }

        public ColumnSetter setHeaderValue(Object headerValue)
        {
            col.setHeaderValue(headerValue);
            return this;
        }

        public ColumnSetter setHeaderRenderer(TableCellRenderer headerRenderer)
        {
            col.setHeaderRenderer(headerRenderer);
            return this;
        }

        public ColumnSetter setCellRenderer(TableCellRenderer cellRenderer)
        {
            col.setCellRenderer(cellRenderer);
            return this;
        }

        public ColumnSetter setCellEditor(TableCellEditor cellEditor)
        {
            col.setCellEditor(cellEditor);
            return this;
        }

        public ColumnSetter setWidth(int width)
        {
            col.setWidth(width);
            return this;
        }

        public ColumnSetter setWdithFixed(int width)
        {
            col.setMaxWidth((int) (width * 1.15f));
            col.setMinWidth(width);
            col.setPreferredWidth(width);
            return this;
        }

        public ColumnSetter setPreferredWidth(int preferredWidth)
        {
            col.setPreferredWidth(preferredWidth);
            return this;
        }

        public ColumnSetter setMinWidth(int minWidth)
        {
            col.setMinWidth(minWidth);
            return this;
        }

        public ColumnSetter setMaxWidth(int maxWidth)
        {
            col.setMaxWidth(maxWidth);
            return this;
        }

        public ColumnSetter setResizable(boolean isResizable)
        {
            col.setResizable(isResizable);
            return this;
        }

    }

    /**
     * Creates new form AfsCtrl
     */
    public PkgCtrl()
    {
        initComponents();
        jTable1.setShowGrid(true);
        jTable1.setShowHorizontalLines(true);
        jTable1.setCellSelectionEnabled(false);
        jTable1.setColumnSelectionAllowed(false);
        jTable1.setRowSelectionAllowed(true);

        jTable1.setModel(tableref);
        DataRender rend = new DataRender(this);
        DataEditor editer = new DataEditor();
        for (Enumeration ix = jTable1.getColumnModel().getColumns(); ix.hasMoreElements();) {
            TableColumn cc = (TableColumn) ix.nextElement();
            cc.setCellRenderer(rend);
            cc.setCellEditor(editer);
        }
        DefaultRowSorter ak = (DefaultRowSorter) jTable1.getRowSorter();
        ak.setSortable(0, false);
        ak.setSortable(1, true);
        ak.setComparator(1, new Comparator() {
            @Override
            public int compare(Object o1, Object o2)
            {
                int a = o1 == null ? null : o1 instanceof IndexHelper.IEntry
                        ? ((IndexHelper.IEntry) o1).code
                        : 0;
                int b = o2 == null ? null : o2 instanceof IndexHelper.IEntry
                        ? ((IndexHelper.IEntry) o2).code
                        : 0;
                return ArrayUtil.compare(a, b);
            }
        });
        ak.setSortable(2, true);
        ak.setComparator(2, new Comparator() {
            @Override
            public int compare(Object o1, Object o2)
            {
                String a = o1 == null ? null : o1 instanceof IndexHelper.IEntry
                        ? ((IndexHelper.IEntry) o1).desc
                        : ((IndexHelper.VEntry) o1).desc;
                String b = o2 == null ? null : o2 instanceof IndexHelper.IEntry
                        ? ((IndexHelper.IEntry) o2).desc
                        : ((IndexHelper.VEntry) o2).desc;
                if (a == b) {
                    return 0;
                }
                return (a != null ? a : "").compareTo(b != null ? b : "");
            }
        });
        ak.setSortable(3, true);
        ak.setSortable(4, true);
        ak.setSortable(5, false);
        jTable1.getColumnModel().setColumnSelectionAllowed(false);
        ColumnSetter cs = new ColumnSetter();

        cs.ini(jTable1, 0)
                .setWdithFixed(79)
                .setResizable(false);
        cs.ini(jTable1, 1)
                .setWdithFixed(39)
                .setResizable(false);
        cs.ini(jTable1, 2)
                .setPreferredWidth(180);
        cs.ini(jTable1, 3)
                .setWdithFixed(69)
                .setResizable(false);
        cs.ini(jTable1, 4)
                .setWdithFixed(69)
                .setResizable(false);
        cs.ini(jTable1, 5)
                .setWdithFixed(69)
                .setResizable(false);

        jTable1.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (jTable1.getTableHeader().columnAtPoint(e.getPoint()) == 0) {
                    jTable1.getRowSorter().setSortKeys(null);
                }
            }
        });
        jTable1.setCellSelectionEnabled(true);
        navBar2.GotoBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                ax.goToBack();
            }
        });
        navBar2.GotoNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                ax.goToNext();
            }
        });
        jTable1.setTransferHandler(new TableRowTransferHandler(jTable1));
        this.setTransferHandler(jTable1.getTransferHandler());
//        TableRowSorter a = (TableRowSorter) jTable1.getRowSorter();
//        cfilt = "SA".toCharArray();
//        a.setRowFilter(filter);

    }
    File stc;

    public PkgCtrl(File src, Object[] root, JPopupMenu ma)
    {
        this();
        stc = src;
        ax.setup(true, false, true);
        ax.setRoot((Pkg_box) root[0], (IndexHelper.IEntry) root[1]);
        tableref.dax = ax;
        jTable1.updateUI();
        setComponentPopupMenu(ma);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        navBar2 = new main_app.swing.NavBar();
        jPanel2 = new javax.swing.JPanel();
        jToggleButton1 = new javax.swing.JToggleButton();
        jToggleButton2 = new javax.swing.JToggleButton();
        jToggleButton3 = new javax.swing.JToggleButton();

        setPreferredSize(new java.awt.Dimension(675, 420));
        setLayout(new java.awt.BorderLayout());

        jScrollPane1.setInheritsPopupMenu(true);

        jTable1.setAutoCreateRowSorter(true);
        jTable1.setFont(new java.awt.Font("Monospaced", 0, 11)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {

            }
        ));
        jTable1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jTable1.setDragEnabled(true);
        jTable1.setDropMode(javax.swing.DropMode.ON_OR_INSERT_ROWS);
        jTable1.setInheritsPopupMenu(true);
        jTable1.setRowHeight(30);
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        jTable1.setShowGrid(true);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel1.setLayout(new java.awt.BorderLayout());

        navBar2.setMinimumSize(new java.awt.Dimension(92, 29));
        navBar2.setName(""); // NOI18N
        navBar2.setPreferredSize(new java.awt.Dimension(88, 29));
        jPanel1.add(navBar2, java.awt.BorderLayout.CENTER);

        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.LINE_AXIS));

        jToggleButton1.setSelected(true);
        jToggleButton1.setText("VDIR");
        jToggleButton1.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jToggleButton1.setMargin(new java.awt.Insets(5, 5, 5, 5));
        jToggleButton1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jToggleButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jToggleButton1);

        jToggleButton2.setSelected(true);
        jToggleButton2.setText("HEX");
        jToggleButton2.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jToggleButton2.setMargin(new java.awt.Insets(5, 5, 5, 5));
        jToggleButton2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jToggleButton2ActionPerformed(evt);
            }
        });
        jPanel2.add(jToggleButton2);

        jToggleButton3.setText("REAL");
        jToggleButton3.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jToggleButton3.setMargin(new java.awt.Insets(5, 5, 5, 5));
        jToggleButton3.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jToggleButton3ActionPerformed(evt);
            }
        });
        jPanel2.add(jToggleButton3);

        jPanel1.add(jPanel2, java.awt.BorderLayout.LINE_START);

        add(jPanel1, java.awt.BorderLayout.NORTH);
    }// </editor-fold>//GEN-END:initComponents

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_jTable1MouseClicked
    {//GEN-HEADEREND:event_jTable1MouseClicked
//        if (jTable1.getSelectedRowCount() > 0) {
//            System.out.println("cc " + jTable1.convertRowIndexToModel(jTable1.getSelectedRow()));
//        }
        switch (evt.getClickCount()) {
            case 2:
                if (jTable1.getSelectedRowCount() == 1) {//on open action
                    ax.select(jTable1.convertRowIndexToModel(jTable1.getSelectedRow()));
                }
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jToggleButton1ActionPerformed
    {//GEN-HEADEREND:event_jToggleButton1ActionPerformed
        ax.setUseVdir();
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void jToggleButton2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jToggleButton2ActionPerformed
    {//GEN-HEADEREND:event_jToggleButton2ActionPerformed
        ax.setUsedec();
        jTable1.updateUI();
    }//GEN-LAST:event_jToggleButton2ActionPerformed

    private void jToggleButton3ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jToggleButton3ActionPerformed
    {//GEN-HEADEREND:event_jToggleButton3ActionPerformed
        ax.setUseRealIndex();
        jTable1.updateUI();
    }//GEN-LAST:event_jToggleButton3ActionPerformed

    private final JTable pom = new JTable() {

        @Override
        public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
        {
            Object value = getValueAt(row, column);

            boolean isSelected = false;
            boolean hasFocus = false;

            // Only indicate the selection and focused cell if not printing
            if (!isPaintingForPrint()) {
                isSelected = isRowSelected(row);

                boolean rowIsLead
                        = (selectionModel.getLeadSelectionIndex() == row);
                boolean colIsLead
                        = (columnModel.getSelectionModel().getLeadSelectionIndex() == column);

                hasFocus = (rowIsLead && colIsLead) && isFocusOwner();
            }
            return renderer.getTableCellRendererComponent(this, value,
                    isSelected, hasFocus,
                    row, column);
        }

    };
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    public final javax.swing.JTable jTable1 = pom;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton2;
    private javax.swing.JToggleButton jToggleButton3;
    private main_app.swing.NavBar navBar2;
    // End of variables declaration//GEN-END:variables
}
