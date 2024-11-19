/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsers;

import com.DeltaSKR.IO.interfce.IOSeekUtil;
import com.DeltaSKR.IO.interfce.RndAccFile;
import com.DeltaSKR.lang.PrimitiveList;
import java.io.IOException;
import main_app.IndexHelper;
import parsers.ANY.ELF;

/**
 *
 * @author ARMAX
 */
public class UNK_PCK extends Pkg_box {

    public static int ALIGN;

    public UNK_PCK(int[] codecs, BasicFile parent)
    {
        super(null, codecs, parent, Flags.DUNK | Flags.SVOF);
    }

    public UNK_PCK(int[] codecs, BasicFile parent, Object... parsers)
    {
        super(null, codecs, parent, Flags.DUNK | Flags.SVOF, parsers);
    }

    @Override
    protected boolean isValid() throws IOException
    {
        if (jindex != null && getAbsParent() instanceof Pkg_box) {
            Pkg_box aa = (Pkg_box) getAbsParent();
            offsets = IndexHelper.getEmbedOffset(aa.jindex.get(aa.currentIndex()));
            count = jindex.length;
            return true;
        }
        if (ALIGN == 0) {
            return false;
        }
        if (length() < ALIGN) {
            return false;
        }
        long hs = 0;
        this.offsets = null;
        long ts;
        PrimitiveList<long[][]> pis = PrimitiveList.newFrom(long[].class);
//        if (!resizeOffets(this, 2, 0, 0)) {
//            return false;
//        }
        initializeParsers();//exception need for use parsers
//        offsets = (long[][]) this.offsets;

        count = 0;
        super.countFounds = 0;
        boolean ac;
        while (hs < length()) {
            seek(hs);
            ts = ALIGN;
            ac = false;
            for (int j = 0; j < parsers.length; j++) {
                final BasicFile pr = getParser(j);
                if (pr.isValid(this, hs, length(), -1)) {
                    ts = IOSeekUtil.align(pr.length(), ALIGN);
                    long[] aa = {hs, Math.min(hs + ts, length())};
                    pis.addO(aa);
                    count++;
                    ac = true;
                    break;
                }
            }
            if (!ac) {
                super.unk.isValid(this, hs, length(), -1);
                if (super.unk.getCType() == 0x7F454C46) {
                    super.unk.seek(0);
                    ts = IOSeekUtil.align(ELF.get_elf_size((RndAccFile) super.unk.fi), ALIGN);
                    long[] aa = {hs, Math.min(hs + ts, length())};
                    pis.addO(aa);
                    count++;
                }
            }
            hs += ts;
            countFounds = count;
            updateProgress();
        }
        if (count == 0) {
            endFile();
            return false;
        }
        this.offsets = pis.toArray(false);
        return true;
    }

}
