/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package main_app.swing;

import com.DeltaSKR.lang.PrimitiveList;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import main_app.InterfaceExplorer;
import static main_app.swing.DataRender.homeimg;

/**
 *
 * @author armax
 */
public class NavBar extends javax.swing.JPanel {

    private static final FocusListener lx = new FocusListener() {
        @Override
        public void focusGained(FocusEvent e)
        {
            Component a = e.getComponent();
            ((JPanel) a.getParent()).scrollRectToVisible(a.getBounds());
        }

        @Override
        public void focusLost(FocusEvent e)
        {
        }
    };

    /**
     * Creates new form NavBar
     */
    public NavBar()
    {
        initComponents();
    }
    private JButton[] dics;
    private JLabel[] sep;
    private final Border pav=new EmptyBorder(5, 5, 5, 5);
    public void updatePath(PrimitiveList<InterfaceExplorer.PathItem[]> path, int cur, ActionListener ata)
    {
        int uls = cur;
        final int cmpCount = (CurrentDir.getComponentCount() + 1) / 2;
        if (sep == null || sep.length < uls
                || sep.length < cmpCount - 1) {
            JLabel[] a = (JLabel[]) sep;
            sep = new JLabel[(sep != null && uls > sep.length)
                    || uls > cmpCount - 1
                            ? uls : cmpCount - 1];
            uls = 0;
            if (a != null) {
                System.arraycopy(a, 0, sep, 0, a.length);
                uls = a.length;
            }
            for (int i = uls; i < sep.length; i++) {
                sep[i] = new JLabel(">");
//                sep[i].getStyle().setMargin(0, 0, 0, 0);
//                sep[i].getStyle().setPadding(0, 0, 0, 0);
            }
        }
        uls = cur + 1;
        if (dics == null || dics.length < uls
                || dics.length < cmpCount) {
            JButton[] a = (JButton[]) dics;
            dics = new JButton[(dics != null && uls > dics.length)
                    || uls > cmpCount
                            ? uls : cmpCount];
            
            uls = 0;
            if (a != null) {
                System.arraycopy(a, 0, dics, 0, a.length);
                uls = a.length;
            }
            for (int i = uls; i < dics.length; i++) {
                dics[i] = i == 0 ? new JButton("W") {
                    @Override
                    public void paint(Graphics g)
                    {
                        super.paint(g);
                        if (homeimg[0] != null) {
                            g.drawImage(homeimg[0],
                                    (int) ((getWidth() - getHeight() + 0.5f) / 2), 0,
                                    getHeight(), getHeight(), null);
                        }
                    }

                    @Override
                    public void setSize(Dimension d)
                    {
                        if (d != null) {
                            d.width = d.height;
                        }
                        super.setSize(d); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
                    }

                    @Override
                    public void setPreferredSize(Dimension preferredSize)
                    {
                        if (preferredSize != null) {
                            preferredSize.width = preferredSize.height;
                        }
                        super.setPreferredSize(preferredSize); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
                    }

                } : new JButton();
                final int uk = 3;
                dics[i].addActionListener(ata);
                dics[i].setBorder(pav);
                dics[i].addFocusListener(lx);
            }
        }
        CurrentDir.removeAll();
        for (int u = 0; u < cur + 1; u++) {
            if (u > 0) {
                dics[u].setText(path.elements[u].name);
            }
            CurrentDir.add(dics[u]);
            if (u < cur) {
                CurrentDir.add(sep[u]);
            }
        }
        CurrentDir.revalidate();
        if (CurrentDir.getComponentCount() > 1) {
            Component ac = CurrentDir.getComponent(cur * 2);
            CurrentDir.scrollRectToVisible(ac.getBounds());
        }
        CurrentDir.updateUI();
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

        setMaximumSize(new java.awt.Dimension(2147483647, 30));
        setLayout(new java.awt.BorderLayout());

        GotoBack.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        GotoBack.setText(" << ");
        GotoBack.setBorder(null);
        GotoBack.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        GotoBack.setMargin(new java.awt.Insets(0, 5, 0, 5));
        GotoBack.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        add(GotoBack, java.awt.BorderLayout.WEST);

        GotoNext.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        GotoNext.setText(" >> ");
        GotoNext.setBorder(null);
        GotoNext.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        GotoNext.setMargin(new java.awt.Insets(0, 5, 0, 5));
        GotoNext.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        add(GotoNext, java.awt.BorderLayout.EAST);

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane1.setAutoscrolls(true);
        jScrollPane1.addMouseWheelListener(new java.awt.event.MouseWheelListener()
        {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt)
            {
                jScrollPane1MouseWheelMoved(evt);
            }
        });

        CurrentDir.setAutoscrolls(true);
        CurrentDir.setFocusCycleRoot(true);
        CurrentDir.setLayout(new javax.swing.BoxLayout(CurrentDir, javax.swing.BoxLayout.LINE_AXIS));
        jScrollPane1.setViewportView(CurrentDir);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jScrollPane1MouseWheelMoved(java.awt.event.MouseWheelEvent evt)//GEN-FIRST:event_jScrollPane1MouseWheelMoved
    {//GEN-HEADEREND:event_jScrollPane1MouseWheelMoved
        // TODO add your handling code here:
        int ax = evt.getWheelRotation();

        if (ax == 0) {
            return;
        }
        for (Component ca : CurrentDir.getComponents()) {
            if (ca.hasFocus()) {
                if (ax > 0) {
                    ca.transferFocus();
                }
                else {
                    ca.transferFocusBackward();
                }
                break;
            }
        }
    }//GEN-LAST:event_jScrollPane1MouseWheelMoved


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public final javax.swing.JPanel CurrentDir = new javax.swing.JPanel();
    public final javax.swing.JButton GotoBack = new javax.swing.JButton();
    public final javax.swing.JButton GotoNext = new javax.swing.JButton();
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
