/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main_app.swing;

import java.awt.Cursor;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DragSource;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import javax.activation.ActivationDataFlavor;
import javax.activation.DataHandler;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;

/**
 *
 * @author armax
 */
/**
 * Handles drag & drop row reordering
 */
public class TableRowTransferHandler extends TransferHandler {

    private final DataFlavor localObjectFlavor = new ActivationDataFlavor(int[].class, "application/x-java-Object;class=java.lang.Object", "Int array Row Index");
    private JTable table = null;

    public TableRowTransferHandler(JTable table)
    {
        this.table = table;
    }

    @Override
    protected Transferable createTransferable(JComponent c)
    {
        assert (c == table);
        return new DataHandler(table.getSelectedRows(), localObjectFlavor.getMimeType());
    }

    private byte cum;

    @Override
    public boolean canImport(TransferHandler.TransferSupport info)
    {
        byte cum1 = (byte) (info.isDataFlavorSupported(localObjectFlavor) ? 1
                : info.isDataFlavorSupported(java.awt.datatransfer.DataFlavor.javaFileListFlavor) ? 2
                : info.isDataFlavorSupported(java.awt.datatransfer.DataFlavor.stringFlavor) ? 3 : 0);
        boolean b = info.getComponent() == table && info.isDrop() && cum1 != 0;
        table.setCursor(b ? DragSource.DefaultMoveDrop : DragSource.DefaultMoveNoDrop);
        cum = cum1;
        return b;
    }

    @Override
    public int getSourceActions(JComponent c)
    {
        return TransferHandler.COPY_OR_MOVE;
    }

    @Override
    public boolean importData(TransferHandler.TransferSupport info)
    {
        try {

            JTable target = (JTable) info.getComponent();
            target.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

            JTable.DropLocation dl = (JTable.DropLocation) info.getDropLocation();

            int index = dl.getRow();
            int max = table.getModel().getRowCount();
            if (index < 0 || index > max) {
                index = max;
            }
            File[] files = null;
            switch (cum) {
                case 1: {
                    try {
                        int[] rowFrom = (int[]) info.getTransferable().getTransferData(localObjectFlavor);
                        if (dl.isInsertRow()) {
                            ((DataTable) table.getModel()).move(rowFrom, index);
                        }
                        else {
                            if (!((DataTable) table.getModel()).putIntoVDir(rowFrom, index)) {
                                return false;
                            }
                            target.getRowSorter().allRowsChanged();
                            target.updateUI();
                        }
                        target.getSelectionModel().addSelectionInterval(index, index);
                        return true;
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
                case 2: {
                    System.out.println("Files");
                    // Say we'll take it.
                    // Get a useful list
                    java.util.List fileList = (java.util.List) info.getTransferable().getTransferData(java.awt.datatransfer.DataFlavor.javaFileListFlavor);
                    // Convert list to array
                    files = new java.io.File[fileList.size()];
                    fileList.toArray(files);

                    // Alert listener to drop.
//                filesDropped( files);
                }
                break;
                case 3: {
                    System.out.println("String");
                    // Thanks, Nathan!
                    // BEGIN 2007-09-12 Nathan Blomquist -- Linux (KDE/Gnome) support added.
                    for (DataFlavor flavor : info.getTransferable().getTransferDataFlavors()) {
                        if (flavor.isRepresentationClassReader()) {
                            // Say we'll take it.
                            Reader reader = flavor.getReaderForText(info.getTransferable());
                            try {
                                BufferedReader br = new BufferedReader(reader, 0x1000);
                                files = FileDrop.createFileArray(br, null);
                            }
                            finally {
                                reader.close();
                            }
                            //filesDropped( createFileArray(br, out));
                            break;
                        }
                        else {//further can be use for rename????
//                              System.out.println(info.getTransferable().getTransferData(flavor));
                        }
                    }
                }
                break;
                default:
                    return false;
            }
            if (files == null) {
                return false;
            }
            if (dl.isInsertRow() || ((DataTable) table.getModel()).dax.size() < 1) {
                ((DataTable) table.getModel()).addFiles(files, index);
            }
            else {
                if (!((DataTable) table.getModel()).replaceFiles(files, index)) {
                    return false;
                }
                target.getRowSorter().allRowsChanged();
                target.updateUI();
            }
            target.getSelectionModel().addSelectionInterval(index, index);
        }
        catch (UnsupportedFlavorException ex) {
        }
        catch (IOException ex) {
        }
        return false;
    }

    @Override
    protected void exportDone(JComponent c, Transferable t, int act)
    {
        if ((act == TransferHandler.MOVE) || (act == TransferHandler.NONE)) {
            table.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

}
