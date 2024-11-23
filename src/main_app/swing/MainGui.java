/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package main_app.swing;

import com.DeltaSKR.IO.interfce.IOSys;
import com.DeltaSKR.IO.interfce.RndAccess;
import com.DeltaSKR.IO.interfce.WriteSeek;
import com.sgucore.MainApp;
import com.sgucore.data.AFS;
import com.sgucore.data.ISO_Root;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import main_app.IndexHelper;
import parsers.Pkg_box;

/**
 *
 * @author armax
 */
public class MainGui extends javax.swing.JFrame {

    public void OptionAction(ActionEvent e)
    {
        if (jTabbedPane1.getSelectedComponent() == null) {
            return;
        }
        AbstractButton but = (AbstractButton) e.getSource();
        System.out.println((char) but.getMnemonic() + " " + but.getText());
        switch (but.getMnemonic()) {
            case 'N':

                break;
            case 'D':

                break;
            case 'C':

                break;
            case 'I':

                break;
            case 'T': {
                final PkgCtrl cat = (PkgCtrl) jTabbedPane1.getSelectedComponent();
                try {
                    cas.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    if (cas.showSaveDialog(MainGui.this) != JFileChooser.APPROVE_OPTION) {
                        return;
                    }
                }
                finally {
                    cas.setFileSelectionMode(JFileChooser.FILES_ONLY);
                }
                final File op = cas.getSelectedFile();
                new Thread() {
                    @Override
                    public void run()
                    {
                        boolean c = cat.ax.useFlatOPath;
                        RndAccess su = null;
                        try {
                            cat.ax.useFlatOPath = true;
                            int[] cc = cat.ax.getSelecteds();
                            int ss = cat.ax.level;
                            int[] pat = cat.ax.getPath();
                            su = IOSys.openRandom(cat.stc, true);
                            Pkg_box so = cat.ax.getRoot();
                            so.jindex = cat.ax.getIEntry();
                            so.setIO(su);
                            if (!so.isValid(-1)) {
                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override
                                    public void run()
                                    {
                                        StatusMessage.setText("Error cant open file");
                                    }

                                });
                                return;
                            }
                            so.export(op.getPath(), false, cc, pat, ss, true);
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run()
                                {
                                    StatusMessage.setText("Export done!");
                                }

                            });
                        }
                        catch (IOException ex) {
                        }
                        finally {
                            if (su != null) {
                                try {
                                    su.close();
                                }
                                catch (IOException iOException) {
                                }
                            }
                            cat.ax.useFlatOPath = c;
                        }

                    }

                }.start();
            }
            break;
            case 'G': {
                PkgCtrl cat = (PkgCtrl) jTabbedPane1.getSelectedComponent();
                String osk = JOptionPane.showInputDialog("Write a new VDIR name");
                if (osk == null || osk.trim().isEmpty()) {
                    return;
                }
                int[] rows = cat.jTable1.getSelectedRows();
                kas:
                if (rows.length > 0) {
                    //fix out of bounds exception
                    if (rows.length == 1 && (rows[0] < 0 || rows[0] >= cat.jTable1.getRowCount())) {
                        rows = null;
                        break kas;
                    }
                    for (int i = 0; i < rows.length; i++) {
                        rows[i] = cat.jTable1.convertRowIndexToModel(rows[i]);
                    }
                }
                cat.ax.newGroup(osk, cat.ax.size() > 0 ? rows : null);
            }
            break;
            case 'U':

                break;
            case 'M':

                break;
            case 'P':

                break;
        }

    }

    /**
     * Creates new form MainGui
     */
    public MainGui()
    {
        initComponents();
        setLocationRelativeTo(null);
        for (Component a : jTabbedPane1.getComponents()) {
            if (a instanceof PkgCtrl) {
                ((PkgCtrl) a).setComponentPopupMenu(jPopupMenu1);
            }
        }

        final String[] Options = (//
                "N) NEW FOLDER,"
                + "D) IMPORT FOLDER,"
                + "I) IMPORT FILE,"
                + "T) EXPORT FILE,"
                + "-,"
                + "C) COPY,"
                + "P) PASTE,"
                + "E) DELETE,"
                + "-,"
                + "G) VNEW,"
                + "U) VDELETE,"
                + "M) VMOVE,"
                + "K) VCOPY,"
                + "").split(",");
        for (int i = 0; i < Options.length; i++) {
            if ("-".equals(Options[i])) {
                jPopupMenu1.add(new JSeparator(JSeparator.HORIZONTAL));
                jToolBar1.add(new JSeparator(JSeparator.HORIZONTAL));
                continue;
            }
            final Action acc = new AbstractAction(Options[i]) {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    OptionAction(e);
                }
            };
            acc.putValue(Action.MNEMONIC_KEY, (int) Options[i].charAt(0));
            jPopupMenu1.add(new JMenuItem(acc));
            jToolBar1.add(new JButton(acc));
        }

        jToolBar1.setFloatable(false);
        jToolBar1.updateUI();
//        File as = new File("../DBZ_TTT_UTIL/com.neon.sgu/games/DragonBall Z - Budokai Tenkaichi 2 ps2.iso");
//        jTabbedPane1.add(as.getName(), new PkgCtrl(PkgCtrl.open(as), jPopupMenu1));
//        as = new File("../DBZ_TTT_UTIL/com.neon.sgu/games/DragonBall Z - Budokai 3 ps2.iso");
//        jTabbedPane1.add(as.getName(), new PkgCtrl(PkgCtrl.open(as), jPopupMenu1));
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

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jSplitPane1 = new javax.swing.JSplitPane();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jPanel2 = new javax.swing.JPanel();
        jProgressBar1 = new javax.swing.JProgressBar();
        StatusMessage = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setMinimumSize(new java.awt.Dimension(650, 420));
        setSize(new java.awt.Dimension(650, 420));

        jSplitPane1.setOneTouchExpandable(true);

        jTabbedPane1.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        jSplitPane1.setRightComponent(jTabbedPane1);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jToolBar1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jToolBar1.setRollover(true);
        jPanel1.add(jToolBar1, java.awt.BorderLayout.NORTH);

        jSplitPane1.setLeftComponent(jPanel1);

        getContentPane().add(jSplitPane1, java.awt.BorderLayout.CENTER);

        jPanel2.setLayout(new java.awt.BorderLayout());

        jProgressBar1.setMinimumSize(new java.awt.Dimension(10, 20));
        jProgressBar1.setName(""); // NOI18N
        jProgressBar1.setPreferredSize(new java.awt.Dimension(146, 20));
        jPanel2.add(jProgressBar1, java.awt.BorderLayout.WEST);
        jPanel2.add(StatusMessage, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);

        jMenu1.setText("File");

        jMenuItem7.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem7.setMnemonic('a');
        jMenuItem7.setText("Open");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem7);

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem1.setText("New");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem2.setText("Save");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem4.setText("Apply replaces");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    static final JFileChooser cas = new JFileChooser();

    static {
        if (MainApp.prefs.get("curDir", null) != null) {
            cas.setCurrentDirectory(new File(MainApp.prefs.get("curDir", null)));
        }
        cas.setAcceptAllFileFilterUsed(false);
        cas.addChoosableFileFilter(new FileNameExtensionFilter("AFS Aseet File System", "afs"));
        cas.addChoosableFileFilter(new FileNameExtensionFilter("ISO-9660 Raw CD/DVD Image File System", "iso"));
        cas.setMultiSelectionEnabled(false);
        cas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                MainApp.prefs.put("curDir", cas.getCurrentDirectory().getPath());
            }
        });
    }
    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem7ActionPerformed
    {//GEN-HEADEREND:event_jMenuItem7ActionPerformed
        StatusMessage.setText("Open file...");
        cas.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (cas.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            jProgressBar1.setIndeterminate(true);
            new Thread() {
                boolean ax = false;
                Object[] res;
                File as;

                @Override
                public void run()
                {
                    if (!ax) {
                        try {
                            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                            as = cas.getSelectedFile();
                            res = PkgCtrl.open(as, ((FileNameExtensionFilter) cas.getFileFilter()).getExtensions()[0]);
                            ax = true;
                        }
                        catch (Exception e) {
                            StatusMessage.setText("Error: " + e.getMessage());
                            e.printStackTrace();
                            res = null;
                        }
                        SwingUtilities.invokeLater(this);
                        return;
                    }
                    jProgressBar1.setIndeterminate(false);
                    jProgressBar1.setValue(100);
                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    if (res == null) {
                        JOptionPane.showMessageDialog(MainGui.this, "Invalid file type or corrupted", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    jTabbedPane1.add(as.getName(), new PkgCtrl(as, res, jPopupMenu1));
                    jTabbedPane1.setToolTipTextAt(jTabbedPane1.getTabCount() - 1, as.getPath());
                    jTabbedPane1.setSelectedIndex(jTabbedPane1.getTabCount() - 1);
                }

            }.start();
        }
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem1ActionPerformed
    {//GEN-HEADEREND:event_jMenuItem1ActionPerformed
        StatusMessage.setText("Creating new file...");
        IndexHelper.IEntry it = new IndexHelper.IEntry();
        it.code = 0x41465300;
        it.modifier = Pkg_box.TYPE_DIR;
        jTabbedPane1.add("New File " + jTabbedPane1.getTabCount(),
                new PkgCtrl(null, new Object[]{new AFS(null), it}, jPopupMenu1)
        );
        jTabbedPane1.setSelectedIndex(jTabbedPane1.getTabCount()-1);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem2ActionPerformed
    {//GEN-HEADEREND:event_jMenuItem2ActionPerformed
        if (jTabbedPane1.getSelectedComponent() == null) {
            return;
        }
        StatusMessage.setText("Saving file...");
        final int tim = jTabbedPane1.getSelectedIndex();
        final PkgCtrl ca = (PkgCtrl) jTabbedPane1.getSelectedComponent();
        final File[] old = {ca.stc};
        final boolean[] exists = {false};
        if (ca.stc == null) {
            cas.setSelectedFile(new File(cas.getCurrentDirectory(), jTabbedPane1.getTitleAt(tim)));
            if (cas.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                ca.stc = cas.getSelectedFile();
            }
        }
        else if (ca.stc.exists()) {
            switch (JOptionPane.showConfirmDialog(this, "This file already Exists!\nOverwrite it?", "Advice",
                    JOptionPane.YES_NO_CANCEL_OPTION
            )) {
                case JOptionPane.NO_OPTION:
                    ca.stc = new File(ca.stc.getParentFile(), IOSys.setExt(ca.stc.getName(), ((FileNameExtensionFilter) cas.getFileFilter()).getExtensions()[0], true));
                    if (cas.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                        ca.stc = cas.getSelectedFile();
                    }
                    break;
                case JOptionPane.CANCEL_OPTION:
                    return;
                case JOptionPane.YES_OPTION:
                    exists[0] = true;
                    ca.stc = new File(ca.stc.getParentFile(), "__-TMP-__.bin");
                    break;
            }
        }
        jProgressBar1.setIndeterminate(true);
        new Thread() {
            @Override
            public void run()
            {
                try {
                    if (!exists[0]) {
                        ca.stc = new File(ca.stc.getParentFile(), IOSys.setExt(ca.stc.getName(), ((FileNameExtensionFilter) cas.getFileFilter()).getExtensions()[0], true));
                    }
                    IOSys.delete(ca.stc);
                    IndexHelper.IEntry rups = ca.ax.getIEntry();
                    Pkg_box root = ca.ax.getRoot();
                    Pkg_box outa = root instanceof AFS && ((FileNameExtensionFilter) cas.getFileFilter()).getExtensions()[0].equals("afs")
                            ? root : new ISO_Root(null);
                    try {
                        if (old[0] != null && old[0].exists()) {
                            root.setIO(IOSys.openRandom(old[0], true));
                            if (!root.isValid(-1)) {
                                JOptionPane.showMessageDialog(MainGui.this, "ERROR: Cant parse the original data");
                                return;
                            }
                        }
                        try {
                            root.jindex = rups;
                            WriteSeek osta = IOSys.openRandom(ca.stc, false);
                            try {
                                outa.writeCopy(root, osta, true);
                            }
                            finally {
                                osta.close();
                            }
                            if (exists[0] && old != null) {
                                System.out.println("replacing backup");
                                File am = new File(old[0].getParentFile(), IOSys.addExt(old[0].getName(), "bak", true));
                                if (am.exists()) {
                                    IOSys.delete(am);
                                }
                                old[0].renameTo(am);
                                IOSys.delete(old[0]);
                                ca.stc.renameTo(old[0]);
                                ca.stc = old[0];
                            }
                        }
                        finally {
                            if (root.getFi() != null && old[0] != null && old[0].exists()) {
                                root.getFi().close();
                            }
                        }
                        rups.pos = 0;
                        while (true) {
                            IndexHelper.IEntry ast = rups.next();
                            if (ast == null) {
                                break;
                            }
                            if (ast instanceof IndexHelper.SEntry) {
                                rups.set(rups.pos - 1, new IndexHelper.IEntry(ast));
                            }
                        }
                        rups.offs = root.offsets;
                        root.offsets = null;
                        old[0] = ca.stc;
                        ca.ax.setRoot(outa);
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run()
                            {
                                ca.jTable1.updateUI();
                                jTabbedPane1.setTitleAt(tim, ca.stc.getName());
                                jTabbedPane1.setToolTipTextAt(tim, ca.stc.getPath());
                                StatusMessage.setText("Saving done!");
                            }

                        });

                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(MainGui.this, "Error:" + ex.getMessage());
                    }
                    finally {
                        ca.stc = old[0];//revert to original
                    }
                }
                finally {
                    System.out.println("END save opration");
                    jProgressBar1.setIndeterminate(false);
                    jProgressBar1.setValue(100);
                }
            }

        }.start();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem4ActionPerformed
    {//GEN-HEADEREND:event_jMenuItem4ActionPerformed
        if (jTabbedPane1.getSelectedComponent() == null) {
            return;
        }
        StatusMessage.setText("Apply replaces...");
        final int tim = jTabbedPane1.getSelectedIndex();
        final PkgCtrl ca = (PkgCtrl) jTabbedPane1.getSelectedComponent();
        if (ca.ax.replaceCount < 1) {
            StatusMessage.setText("Nothing to apply");
            return;
        }
        final File[] old = {ca.stc};
        jProgressBar1.setIndeterminate(true);
        new Thread() {
            @Override
            public void run()
            {
                try {
                    IndexHelper.IEntry rups = ca.ax.getIEntry();
                    Pkg_box root = ca.ax.getRoot();
                    try {
                        root.setIO(IOSys.openRandom(old[0], false));
                        root.jindex = rups;
                        if (!root.isValid(-1)) {
                            JOptionPane.showMessageDialog(MainGui.this, "ERROR: Cant parse the original data");
                            return;
                        }
                        try {
                            ca.ax.applyReplaceChanges();
                        }
                        finally {
                            if (root.getFi() != null) {
                                root.getFi().close();
                            }
                        }
                        root.offsets = null;
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run()
                            {
                                ca.jTable1.updateUI();
                                jTabbedPane1.setTitleAt(tim, ca.stc.getName());
                                jTabbedPane1.setToolTipTextAt(tim, ca.stc.getPath());
                                StatusMessage.setText("Apply done!");
                            }

                        });
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(MainGui.this, "Error:" + ex.getMessage());
                    }
                }
                finally {
                    System.out.println("END Apply opration");
                    jProgressBar1.setIndeterminate(false);
                    jProgressBar1.setValue(100);
                }
            }

        }.start();
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }
        catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run()
            {
                new MainGui().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel StatusMessage;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
}
