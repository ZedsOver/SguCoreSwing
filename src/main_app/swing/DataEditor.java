/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main_app.swing;

import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import main_app.IndexHelper;

/**
 *
 * @author armax
 */
public class DataEditor extends DefaultCellEditor {

    Class[] argTypes = new Class[]{String.class};
    java.lang.reflect.Constructor constructor;
    Object value;

    public DataEditor()
    {
        super(new JTextField());
        getComponent().setName("Table.editor");
    }

    public boolean stopCellEditing()
    {
        String s = (String) super.getCellEditorValue();
        // Here we are dealing with the case where a user
        // has deleted the string value in a cell, possibly
        // after a failed validation. Return null, so that
        // they have the option to replace the value with
        // null or use escape to restore the original.
        // For Strings, return "" for backward compatibility.
        try {
            if ("".equals(s)) {
                if (constructor.getDeclaringClass() == String.class) {
                    value = s;
                }
                super.stopCellEditing();
            }

//            SwingUtilities2.checkAccess(constructor.getModifiers());
            value = constructor.newInstance(new Object[]{s});
        }
        catch (Exception e) {
            ((JComponent) getComponent()).setBorder(new LineBorder(Color.red));
            return false;
        }
        return super.stopCellEditing();
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected,
            int row, int column)
    {
        this.value = null;
        ((JComponent) getComponent()).setBorder(new LineBorder(Color.black));
        try {
            Class type = table.getColumnClass(column);
            // Since our obligation is to produce a value which is
            // assignable for the required type it is OK to use the
            // String constructor for columns which are declared
            // to contain Objects. A String is an Object.
            if (type == Object.class) {
                type = String.class;
            }
//            ReflectUtil.checkPackageAccess(type);
//            SwingUtilities2.checkAccess(type.getModifiers());
            constructor = type.getConstructor(argTypes);
        }
        catch (Exception e) {
            return null;
        }
        if (value == null) {
            return null;
        }
        if (value instanceof IndexHelper.IEntry) {
            IndexHelper.IEntry dat = (IndexHelper.IEntry) value;
            switch (column) {
                case 0://ID
                    value = row + 1;
                    break;
                case 1://TYPE
                    value = DataRender.getSType(dat.code);
                    break;
                case 2://NAME
                    value = dat.desc;
                    break;
                case 3://OFFSET
                    value = 0L;
                    break;
                case 4://SIZE
                    value = 0L;
                    break;
                case 5://MAX SIZE
                    value = 0L;
                    break;
            }
        }
        else {
            IndexHelper.VEntry dat = (IndexHelper.VEntry) value;
            switch (column) {
                case 0://ID
                    value = row + 1;
                    break;
                case 1://TYPE
                    value = "VDIR";
                    break;
                case 2://NAME
                    value = dat.desc;
                    break;
                case 3://OFFSET
                    value = 0L;
                    break;
                case 4://SIZE
                    value = dat.length;
                   return null;
                case 5://MAX SIZE
                    value = 0L;
                    break;
            }
        }

        return super.getTableCellEditorComponent(table, value, isSelected, row, column);
    }

    public Object getCellEditorValue()
    {
        return value;
    }

}
