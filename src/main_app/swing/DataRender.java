/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main_app.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import main_app.IndexHelper;

/**
 *
 * @author armax
 */
public class DataRender extends DefaultTableCellRenderer {

    static final BufferedImage as(String b) throws IOException
    {
        InputStream sa = PkgCtrl.class.getResourceAsStream(b);
        try {
            return ImageIO.read(sa);
        }
        finally {
            sa.close();
        }
    }
    static final BufferedImage[] icons = new BufferedImage[14];
    static final BufferedImage homeimg[] = {null};

    static {
        try {
            icons[0] = as("/assets/icos/error.png");
            icons[1] = as("/assets/icos/binary.png");
            icons[2] = as("/assets/icos/image.png");
            icons[3] = as("/assets/icos/model.png");
            icons[4] = as("/assets/icos/pal.png");
            icons[5] = as("/assets/icos/sound.png");
            icons[6] = as("/assets/icos/text.png");
            icons[7] = as("/assets/icos/dir.png");
            icons[8] = as("/assets/icos/emdir.png");
            icons[9] = as("/assets/icos/zip.png");
            icons[10] = as("/assets/icos/emzip.png");
            icons[11] = as("/assets/icos/vdir.png");
            icons[12] = as("/assets/icos/emvdir.png");
            icons[13] = as("/assets/icos/select.png");
            homeimg[0] = as("/assets/icos/ic-home.png");

        }
        catch (Exception ex) {

        }

    }
    private PkgCtrl parent;
    final Border osk = new LineBorder(Color.MAGENTA, 3);

    public DataRender(PkgCtrl parent)
    {
        this.parent = parent;
    }

    @Override
    protected void paintBorder(Graphics g)
    {
        super.paintBorder(g);
        if (ico > -1) {
            g.drawImage(icons[ico], 0, 0, getHeight(), getHeight(), this);
        }
    }

    byte ico = -1;
    private static final StringBuffer bfo = new StringBuffer(4);

    public synchronized static final String getSType(int t)
    {
        switch (t) {
            case 0x64001027:
                return "TXT0";
            case 0x64000000:
                return "TXT1";
            case 0x6400C05D:
                return "TXT2";
            case 0x89504E47:
                return "PNG";
            case 0x7F454C46:
                return "ELF";
            case 0x42494e00:
                return "BIN";
        }
        char e;
        bfo.delete(0, bfo.length());
        for (int i = 3; i > -1; i--) {
            e = (char) ((t >> (i * 8)) & 255);
            if ((e >= 'a' && e <= 'z')
                    || (e >= 'A' && e <= 'Z')
                    || (e >= '0' && e <= '9')
                    || e == '_') {
                bfo.append(e);
            }
            else {
                bfo.append('?');
            }
        }
        return bfo.toString();
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        ico = -1;
        super.getTableCellRendererComponent(table, null, isSelected, hasFocus, row, column);
        if (value == null) {
            setText("<ERROR>");
            return this;
        }
        setHorizontalAlignment(SwingConstants.LEFT);
        if (value instanceof IndexHelper.IEntry) {
            IndexHelper.IEntry dat = (IndexHelper.IEntry) value;

            switch (column) {
                case 0://ID
                    setHorizontalAlignment(SwingConstants.RIGHT);
                    ico = (byte) (dat.modifier & IndexHelper.TYPEMASK);
                    setText(String.format(parent.ax.usedec ? "%06d" : "%06X",
                            parent.ax.useRealIndex ? parent.ax.getRealIndex(table.convertRowIndexToModel(row))
                                    : table.convertRowIndexToModel(row)
                    ));
                    break;
                case 1://TYPE
                    setText(getSType(dat.code));
                    break;
                case 2://NAME
                    if (value instanceof IndexHelper.SEntry) {
                        setBorder(osk);
                    }
                    setText(dat.desc);
                    break;
                case 3://OFFSET
                {
                    setHorizontalAlignment(SwingConstants.RIGHT);
                    if (value instanceof IndexHelper.SEntry) {
                        setText(String.format("%08X", 0));
                        break;
                    }
                    final Object offs = parent.ax.getOffsets();
                    final int ul = parent.ax.getRealIndex(table.convertRowIndexToModel(row));
                    if (offs instanceof long[]) {
                        setText(String.format("%08X", ((long[]) offs)[ul]));
                    }
                    else {
                        setText(String.format("%08X", ((long[][]) offs)[ul][0]));
                    }
                }
                break;
                case 4://SIZE
                {
                    setHorizontalAlignment(SwingConstants.RIGHT);
                    if (value instanceof IndexHelper.SEntry) {
                        setText(String.format("%08X", ((IndexHelper.SEntry) value).size));
                        break;
                    }
                    final Object offs = parent.ax.getOffsets();
                    final int ul = parent.ax.getRealIndex(table.convertRowIndexToModel(row));
                    if (offs instanceof long[]) {
                        final long[] ax = (long[]) offs;
                        setText(String.format("%08X", ax[ul + 1] - ax[ul]));
                    }
                    else {
                        final long[] ax = ((long[][]) offs)[ul];
                        setText(String.format("%08X", ax[1] - ax[0]));
                    }
                }
                break;
                case 5://MAX SIZE
                    setHorizontalAlignment(SwingConstants.RIGHT);
                    setText(0 + "");
                    break;
            }
        }
        else {
            IndexHelper.VEntry dat = (IndexHelper.VEntry) value;
            switch (column) {
                case 0://ID
                    setHorizontalAlignment(SwingConstants.RIGHT);
                    ico = (byte) (dat.length > 0 ? 11 : 12);
                    setText(String.format(parent.ax.usedec ? "%06d" : "%06X", table.convertRowIndexToModel(row)));
                    break;
                case 1://TYPE
                    setText("VDIR");
                    break;
                case 2://NAME
                    setText(dat.desc);
                    break;
                case 3://OFFSET
                    setHorizontalAlignment(SwingConstants.RIGHT);
                    setText(0 + "");
                    break;
                case 4://SIZE
                    setHorizontalAlignment(SwingConstants.RIGHT);
                    setText(dat.length + "");
                    break;
                case 5://MAX SIZE
                    setHorizontalAlignment(SwingConstants.RIGHT);
                    setText(0 + "");
                    break;
            }
        }

        return this;
    }

}
