/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main_app;

import com.DeltaSKR.lang.ArrayUtil;
import com.DeltaSKR.lang.PrimitiveList;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import parsers.BasicFile;
import parsers.Pkg_base;
import parsers.Pkg_box;

/**
 *
 * @author ARMAX
 */
public abstract class InterfaceExplorer implements List {

    private Pkg_box root;
    private final static StringBuffer bfo = new StringBuffer(4);

    public int level = -1;
    public int curpos = -1;
    private final PrimitiveList<PathItem[]> cuPath = PrimitiveList.newFrom(PathItem.class);
    private int[] path = {};

    public void setIEntry(int ind, IndexHelper.IEntry get)
    {
        PathItem it = cuPath.elements[curpos];
        it = it.parentID == curpos ? it : cuPath.elements[it.parentID];
        it.elem.set(ind, get);
    }

    public IndexHelper.IEntry getIEntry()
    {
        return cuPath.elements[0].elem;
    }

    public Pkg_box getRoot()
    {
        return root;
    }

    public Pkg_base getParentObject() throws IOException
    {
        buk:
        if (root != null) {
            boolean fl = useFlatOPath;
            try {
                useFlatOPath = true;
                return root.getFromPath(this.getPath(), level);
            }
            finally {
                useFlatOPath = fl;
            }
        }
        return null;
    }

    public void printDecoders(List<Integer> codecs)
    {
        buk:
        if (root != null) {
            boolean fl = useFlatOPath;
            try {
                useFlatOPath = true;
                IndexHelper.IEntry cc = cuPath.elements[0].elem;
                Pkg_box cx = root;
                dit:
                for (int dim : ArrayUtil.forArray(this.getPath(), 0, level)) {
                    cc = cc.get(dim);
                    int codec = IndexHelper.getCode(cc);
                    System.out.printf("%08X %-5s %-5s %s\n", codec, getSType(codec), cx instanceof Pkg_box, cx);
                    for (BasicFile c : cx.forParsers()) {
                        if (!(c instanceof Pkg_box) || !c.isValidCode(codec)) {
                            continue;
                        }
                        cx = (Pkg_box) c;
                        continue dit;
                    }
                    break buk;
                }
                if (cx instanceof Pkg_box) {
                    int i = 0;
                    while (i < ((Pkg_box) cx).parsersCount()) {
                        BasicFile est = ((Pkg_box) cx).getParser(i++);
                        int[] ic = est.P_CODEC_IDS;
                        if (ic == null) {
                            continue;
                        }
                        for (int c : ic) {
                            codecs.add(c);
//                            BasicFile.CONSOLE.printfln("%08X %-5s %s", c, getSType(c),est);
                        }
                    }
                }
//                System.out.println("main_app.InterfaceExplorer.printDecoders()");
            }
            finally {
                useFlatOPath = fl;
            }
        }
    }

    public int getRealIndex(int selectedIndex)
    {
        if (useVdir) {
            PathItem ee = cuPath.elements[curpos];
            if (ee.vdir != null) {
                Object it = ee.vdir.get(selectedIndex);
                if (it instanceof Integer) {//fix cast exception
                    return (Integer) it;
                }
            }
        }
        return selectedIndex;
    }

    public static class PathItem {

        /**
         * dato dado cuando este item es una carpeta virtual
         */
        public int level;

        public int parentID;
        /**
         * ID de la posicion de la carpeta actual
         */
        public int ID;
        /**
         * carpeta actual
         */
        public IndexHelper.IEntry elem;
        public IndexHelper.VEntry vdir = null;
        public String name;
        private int vID;
    }

    public void reset()
    {
        this.root = null;
        level = -1;
        curpos = -1;
    }

    public void refreshTheme()
    {
    }

    public void setRoot(Pkg_box root, IndexHelper.IEntry et)
    {
        this.root = root;
        cuPath.clear();
        if (root != null) {
            PathItem cos = new PathItem();
            cos.level = 0;
            cos.ID = 0;
            cos.parentID = 0;
            cos.elem = et;
            cos.vdir = IndexHelper.getVDir(et);
            curpos = 0;
            level = 0;
            cuPath.addO(cos);
            updateFmts();
            updateHistory(cuPath, curpos);
            updateUI(-1);
        }
    }

    public abstract int getSelected();

    public abstract int[] getSelecteds();

    protected final int[] fixSelection(int[] selData)
    {
        if (!useFlatOPath) {
            return selData;
        }
        if (curpos < 0 || curpos >= cuPath.elements.length) {
            return null;
        }
        PathItem cur = cuPath.elements[curpos];
        if (!useVdir || cur.vdir == null) {
            return selData;
        }
        int len = selData.length;
        int[] ras = {0, 0};
        for (int i = 0; i < len;) {
            Object ss = cur.vdir.get(selData[i]);
            if (ss instanceof Integer) {
                selData[i] = (Integer) ss;
                i++;
            }
            else {
                int dus = clcFiles((IndexHelper.VEntry) ss);
                ArrayUtil.remove(selData, len, i);
                len--;
                if (dus > 0) {
                    selData = (int[]) ArrayUtil.increaseArraySize(selData, len, dus);
                    ras[0] = len;
                    ras[1] = i;
                    insFiles((IndexHelper.VEntry) ss, selData, ras);
                    len += dus;
                    i = ras[1];
                }
            }
        }
        if (len != selData.length) {
            selData = ArrayUtil.copyOf(selData, len);
        }
        return selData;
    }

    private void insFiles(IndexHelper.VEntry dak, int[] rr, int[] off)
    {
        for (int i = 0; i < dak.length; i++) {
            Object ss = dak.get(i);
            if (ss instanceof Integer) {
                ArrayUtil.insert(rr, off[0]++, off[1]++, (Integer) ss);
            }
            else {
                insFiles((IndexHelper.VEntry) ss, rr, off);
            }
        }
    }

    private int clcFiles(IndexHelper.VEntry dak)
    {
        int sus = 0;
        for (int i = 0; i < dak.length; i++) {
            Object ss = dak.get(i);
            if (ss instanceof Integer) {
                sus++;
            }
            else {
                sus += clcFiles((IndexHelper.VEntry) ss);
            }
        }
        return sus;
    }

    public boolean useFlatOPath = false;

    public int[] getPath()
    {
        if (useFlatOPath) {
            if (level >= path.length) {
                path = new int[level];
            }
            for (int c = 1, i = 0; i < level && c <= curpos; c++) {
                PathItem ptl = cuPath.elements[c];
                if (c == ptl.parentID) {
                    path[i++] = ptl.vID;
                }
            }
            return path;
        }
        if (curpos >= path.length) {
            path = new int[curpos];
        }
        for (int c = 1; c <= curpos; c++) {
            PathItem ptl = cuPath.elements[c];
            path[c - 1] = ptl.ID;
        }
        return path;
    }

    protected abstract void clearSelection();

    protected abstract void updateUI(int p);

    public final void select(int i)
    {
        int ll = curpos;
        open(i);
        if (curpos != ll) {
            clearSelection();
            updateUI(-1);
            updateHistory(cuPath, curpos);
        }
    }

    protected abstract void updateHistory(PrimitiveList<PathItem[]> path, int cur);

    private void open(int i)
    {
        final int vindex = i;
        PathItem path = cuPath.elements[curpos];
        IndexHelper.VEntry vdir = path.vdir;
        drik:
        if (useVdir && vdir != null) {
            if (vdir.isNull(i)) {
                return;//fix null data
            }
            if (vdir.get(i) instanceof Integer) {
                i = (Integer) path.vdir.get(i);
                path = cuPath.elements[path.parentID];
                break drik;
            }
            PathItem et = new PathItem();
            et.level = level;
            et.parentID = path.parentID;
            et.elem = null;
            et.vdir = (IndexHelper.VEntry) vdir.get(i);
            et.ID = vindex;
            et.vID = i;
            et.name = String.format("%" + (usedec ? 'd' : 'X') + " %s", (useRealIndex ? i : vindex) + 1, ((IndexHelper.VEntry) vdir.get(i)).desc);
            curpos++;
            cuPath.setSize(curpos);
            cuPath.addO(et);
            updateFmts();
            return;
        }
        IndexHelper.IEntry cak = path.elem;
        if (cak.get(i) == null) {
            return;
        }
        IndexHelper.IEntry cus = cak.get(i);
        if (cus != null && IndexHelper.isCollapse(cus)) {
            PathItem et = new PathItem();
            et.elem = cus;
            et.vdir = IndexHelper.getVDir(cus);
            et.ID = vindex;
            et.vID = i;
            et.level = ++level;
            et.parentID = ++curpos;
            et.name = IndexHelper.getIfNamed(cus);
            if (et.name == null || et.name.trim().length() < 1) {
                et.name = getSType(IndexHelper.getCode(cus));
            }
            et.name = String.format("%" + (usedec ? 'd' : 'X') + " %s", (useRealIndex ? i : vindex) + 1, et.name);
            cuPath.setSize(curpos);
            cuPath.addO(et);
            updateFmts();
        }

    }

    public boolean goTo(int hist)
    {
        if (root == null || hist < 0 || hist >= cuPath.size()) {
            return false;
        }
        PathItem path = cuPath.elements[hist];
        curpos = hist;
        level = path.level;

        clearSelection();
        updateFmts();
        updateHistory(cuPath, curpos);
        updateUI(curpos + 1 < cuPath.size() ? cuPath.elements[curpos + 1].ID : -1);
        return true;
    }

    public boolean goToBack()
    {
        return goTo(curpos - 1);
    }

    public void goToNext()
    {
        goTo(curpos + 1);
    }

    public String isValidMoveOperation(Object[] datax, int dst, boolean copy)
    {
        final PathItem pe = cuPath.elements[curpos];
        if (!useVdir || pe.vdir == null) {
            return "Error invalid operation on null vdir";
        }
        final IndexHelper.VEntry vap = pe.vdir;
        final int[] sels = (int[]) datax[0];
        final IndexHelper.VEntry path = (IndexHelper.VEntry) datax[1];
        final IndexHelper.VEntry root = datax[2] == null ? path : (IndexHelper.VEntry) datax[2];
        if (cuPath.elements[pe.parentID].vdir != root) {
            return "Error invalid root path, the operation only works with same root";
        }
        if (path == vap) {
            if (copy) {
                return "Error try to duplicate file on same location";
            }
            for (int so : sels) {
                if (so == dst) {
                    return "Error dest location, already on selection";
                }
            }
            return null;
        }
        //testing recursive path <--- vap
        for (int i = curpos - 1; i > -1; i--) {
            if (cuPath.elements[i].vdir == path) {
                IndexHelper.VEntry tomp = cuPath.elements[i + 1].vdir;
                for (int cc : sels) {
                    if (tomp == path.get(cc)) {
                        return "Error recursion dest location, already into selection";
                    }
                }
            }
        }
        return null;
    }

    public void moveTo(Object[] datax, int dst, boolean copy)
    {
        final PathItem pe = cuPath.elements[curpos];
        if (!useVdir || pe.vdir == null) {
            datax[0] = null;
            datax[1] = null;
            datax[2] = null;
            return;
        }
        final IndexHelper.VEntry vap = pe.vdir;
        final int[] sels = (int[]) datax[0];
        final IndexHelper.VEntry path0 = (IndexHelper.VEntry) datax[1];
        if (path0.length - sels.length < 1) {//ignore if selection move contains  same length of
            return;
        }
        for (int u : sels) {//fix inter line error
            if (u == dst) {
                dst++;
            }
        }
        final Object cc = vap.get(dst);//get dentiny object
        final Object[] iti = new Object[sels.length];

        for (int c = sels.length - 1, a = 1; c > -1; c--) {
            Object e = copy ? path0.get(sels[c]) : path0.remove(sels[c]);
            iti[iti.length - a++] = e;
        }
        //prevent possible null pointer error
        dst = cc == null ? path0.length - 1 : vap.indexOf(cc);//recalc destiny location

        vap.growBy(sels.length);//grow internal cap size
        System.arraycopy(vap.subs, dst, vap.subs, dst + sels.length, vap.length - dst);
        System.arraycopy(iti, 0, vap.subs, dst, iti.length);
        vap.length += sels.length;
        clearSelection();
    }

    public void moveTo(Object[] datax, boolean copy)
    {
        if (datax[2] == null) {
            datax[0] = null;
            datax[1] = null;
            return;
        }
        final IndexHelper.VEntry vap = (IndexHelper.VEntry) datax[2];
        final int[] sels = (int[]) datax[0];
        final IndexHelper.VEntry path0 = (IndexHelper.VEntry) datax[1];
        final Object[] iti = new Object[sels.length];
        for (int i = 0; i < sels.length; i++) {//ignore if destiny is on same selection
            if (path0.get(sels[i]) == vap) {
                return;
            }
        }
        for (int c = sels.length - 1, a = 1; c > -1; c--) {
            Object e = copy ? path0.get(sels[c]) : path0.remove(sels[c]);
            iti[iti.length - a++] = e;
        }
        int dst = vap.length;//recalc destiny location
        vap.growBy(sels.length);//grow internal cap size
        System.arraycopy(vap.subs, dst, vap.subs, dst + sels.length, vap.length - dst);
        System.arraycopy(iti, 0, vap.subs, dst, iti.length);
        vap.length += sels.length;
        clearSelection();
    }

    public IndexHelper.IEntry getPIEntry()
    {
        final PathItem pe = cuPath.elements[curpos];
        if (pe.elem != null) {
            return pe.elem;
        }
        return cuPath.elements[pe.parentID].elem;
    }

    public IndexHelper.VEntry getPVEntry()
    {
        final PathItem pe = cuPath.elements[curpos];
        if (pe.elem != null || useVdir) {
            return pe.vdir;
        }
        return cuPath.elements[pe.parentID].vdir;
    }

    public void getVPath(Object[] datax, int i, int vdir)
    {
        final PathItem pe = cuPath.elements[curpos];
        if (useVdir && pe.vdir != null) {
            datax[i] = pe.vdir;
            if (vdir < 0) {
                datax[i + 1] = pe.parentID == curpos ? null : cuPath.elements[pe.parentID].vdir;
                return;
            }
            Object ca = pe.vdir.get(vdir);
            if (ca instanceof IndexHelper.VEntry) {
                datax[i + 1] = ca;
                return;
            }
        }
        datax[i] = null;
        datax[i + 1] = null;
    }

    public void swap(int dest, int[] sels)
    {
        final PathItem pe = cuPath.elements[curpos];
        if (!useVdir || pe.vdir == null) {
            return;
        }
        for (int s : sels) {
            if (dest == s) {
                return;
            }
        }
        final Object cc = pe.vdir.get(dest);
        final Object[] iti = new Object[sels.length];
        for (int c = sels.length - 1, a = 1; c > -1; c--) {
            Object e = pe.vdir.remove(sels[c]);
            iti[iti.length - a++] = e;
        }
        dest = pe.vdir.indexOf(cc);
        System.arraycopy(pe.vdir.subs, dest, pe.vdir.subs, dest + sels.length, pe.vdir.length - dest);
        System.arraycopy(iti, 0, pe.vdir.subs, dest, iti.length);
        pe.vdir.length += sels.length;
        clearSelection();

    }

    public void newGroup(String str, int[] sels)
    {
        PathItem pe = cuPath.elements[curpos];
        IndexHelper.VEntry vpat = pe.vdir;
        if (vpat == null) {//generate vdir instance
            vpat = new IndexHelper.VEntry(pe.elem.length);
            IndexHelper.setVDir(pe.elem, vpat);
            for (int i = 0; i < pe.elem.length; i++) {
                vpat.add(i);
            }
            IndexHelper.setVDir(pe.elem, vpat);//fix empty vdir 
            pe.vdir = vpat;
        }
        if (sels == null || sels.length == 0) {
            IndexHelper.VEntry ee = new IndexHelper.VEntry();
            ee.desc = str;
            vpat.add(ee);
            clearSelection();
            updateUI(vpat.length - 1);
        }
        else {
            IndexHelper.VEntry ee = new IndexHelper.VEntry(sels.length);
            ee.desc = str;
            for (int vo : sels) {
                ee.add(vpat.get(vo));
                vpat.set(vo, null);
            }
            vpat.set(sels[0], ee);
            IndexHelper.trimNulls(vpat);
            clearSelection();
            updateUI(sels[0]);
        }
    }

    public void updateVdir()
    {
        //clean old history path
        curpos = cuPath.elements[curpos].parentID;
        cuPath.setSize(curpos + 1);
        final PathItem pep = cuPath.elements[cuPath.size() - 1];
        pep.vdir = pep.elem.vdir;
        updateHistory(cuPath, curpos);
        updateUI(-1);
    }

    public static final String getSType(int t)
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
    public int size()
    {
        if (root == null || curpos == -1) {
            return 0;
        }
        PathItem et = cuPath.elements[curpos];
        if (useVdir && et.vdir != null) {
            return et.vdir.length;
        }
        return et.elem == null ? 0
                : et.elem.length;
    }

    @Override
    public boolean isEmpty()
    {
        return size() == 0;
    }
    private final Object[] objret = {
        new short[1],//type must be generic
        null//value
    };
    private String vdirfmt = "", rfileFmt;

    public boolean usedec = false;
    private boolean useVdir = true;
    public boolean useRealIndex = false;

    public void setup(boolean hex, boolean real, boolean vdir)
    {
        usedec = !hex;
        useRealIndex = real;
        useVdir = vdir;
    }

    private void updateModePath()
    {
        final int[] lit = {getSelected()};//current selected item used as base
        PathItem et = cuPath.elements[curpos];
        if (!useVdir) {
            //convert relative to abs index
            if (lit[0] > -1 && et.vdir != null) {
                if (lit[0] >= et.vdir.length) {
                    lit[0] = -1;
                }
                else if (!(et.vdir.get(lit[0]) instanceof Integer)) {
                    lit[0] = -1;
                }
                else {
                    lit[0] = (Integer) et.vdir.get(lit[0]);
                }
            }
            if (et.elem == null) {
                et = cuPath.elements[et.parentID];
            }

            //get new history without VDIRS
            int ild = 0;
            for (int i = 0; i < cuPath.size(); i++) {
                PathItem pe = cuPath.elements[i];
                if (pe.elem == null) {//ignore this
                    continue;
                }
                if (i == ild) {//ignore same location
                    ild++;
                    continue;
                }
                pe.ID = pe.vID;//convert to real index
                pe.parentID = ild;//fix bad index error
                cuPath.elements[ild++] = pe;
            }
            int unt = 0;
            for (int i = 0; i < cuPath.size(); i++) {
                if (cuPath.elements[i] == et) {
                    unt = i;
                    break;
                }
            }
            curpos = unt;
            cuPath.setSize(ild);
            clearSelection();
            updateFmts();
            updateHistory(cuPath, curpos);
            updateUI(lit[0] > -1 ? lit[0] : curpos + 1 < cuPath.size() ? cuPath.elements[curpos + 1].ID : -1);
            return;
        }
        //making sub paths and possible history paths
        final int[] bim = new int[3];
        final IndexHelper.DVFor pest = new IndexHelper.DVFor() {
            @Override
            public boolean onValue(ArrayList<IndexHelper.VEntry> scope, int index) throws Exception
            {
//                System.out.println(index);
                if (bim[2] != index) {
                    return true;
                }
                final PathItem next = bim[0] < bim[1] ? cuPath.elements[bim[0]] : null;
                cuPath.setSize(cuPath.size() + scope.size() - 1);
                System.arraycopy(cuPath.elements, bim[0], cuPath.elements, bim[0] + scope.size() - 1, bim[1] - bim[0]);
                final int path = bim[0] - 1;//fix parent index
                for (int ix = 1; ix < scope.size(); ix++) {
                    IndexHelper.VEntry vdir = scope.get(ix - 1);
                    int i = vdir.indexOf(scope.get(ix));
                    PathItem et = new PathItem();
                    et.level = cuPath.elements[path].level;//unmutable level path
                    et.parentID = path;
                    et.elem = null;
                    et.vdir = (IndexHelper.VEntry) vdir.get(i);
                    et.ID = i;
                    et.name = String.format("%X %s", i + 1, ((IndexHelper.VEntry) vdir.get(i)).desc);
                    cuPath.elements[bim[0]++] = et;
                }
                if (next != null) {
                    next.ID = scope.get(scope.size() - 1).pos - 1;//set last for position
                }
                return false;
            }
        };
        for (int i = 0; i < cuPath.size();) {
            PathItem pa = cuPath.elements[i];
            pa.parentID = i;//fix bad index error
            if (pa.vdir == null) {//vdir skip exception 
                i++;
                continue;
            }
            bim[0] = i + 1;
            bim[1] = cuPath.size();
            bim[2] = bim[0] < bim[1] ? cuPath.elements[bim[0]].vID : et == cuPath.elements[bim[1] - 1] && lit[0] > -1 && lit[0] < pa.elem.length ? lit[0] : -1;
            if (bim[2] > -1) {
                try {
                    IndexHelper.forTree(pa.vdir, false, pest);
                    i = bim[0];
                    continue;
                }
                catch (Exception ex) {
                }
            }
            i++;
        }
        bim[0] = 0;
        for (int i = 0; i < cuPath.size(); i++) {
            if (cuPath.elements[i] == et) {
                bim[0] = i;
                break;
            }
        }

        if (et.vdir != null && lit[0] > -1 && lit[0] < et.elem.length) {
            try {
                IndexHelper.forTree(et.vdir, false, new IndexHelper.DVFor() {
                    @Override
                    public boolean onValue(ArrayList<IndexHelper.VEntry> scope, int i) throws Exception
                    {
                        if (lit[0] != i) {
                            return true;
                        }
                        bim[0] += scope.size() - 1;//fix history location file
                        lit[0] = scope.get(scope.size() - 1).pos - 1;//set last for position
                        return false;
                    }
                });
            }
            catch (Exception ex) {
                lit[0] = -1;
            }
        }
        else if (lit[0] > -1 && lit[0] < et.elem.length) {

        }
        else {
            lit[0] = -1;
        }
        curpos = bim[0];
        clearSelection();
        updateFmts();
        updateHistory(cuPath, curpos);
        updateUI(lit[0] > -1 ? lit[0] : curpos + 1 < cuPath.size() ? cuPath.elements[curpos + 1].ID : -1);
    }

    public boolean isUseVdir()
    {
        return !useFlatOPath && useVdir;
    }

    public boolean setUseVdir()
    {
        useVdir = !useVdir;
        updateModePath();
        updateUIHistory();
        return useVdir;
    }

    public boolean setUseRealIndex()
    {
        useRealIndex = !useRealIndex;
        updateFmts();
        updateUIHistory();
        return useRealIndex;
    }

    public boolean setUsedec()
    {
        usedec = !usedec;
        updateFmts();
        updateUIHistory();
        return usedec;
    }

    private void updateUIHistory()
    {
        String clax = "%" + (usedec ? 'd' : 'X') + " %s";
        for (int i = 1; i < cuPath.size(); i++) {
            PathItem pc = cuPath.elements[i];
            pc.name = String.format(clax, (useRealIndex ? pc.vID : pc.ID) + 1, pc.name.substring(pc.name.indexOf(' ') + 1));
        }
        updateHistory(cuPath, curpos);
    }

    public Object getOffsets()
    {
        PathItem path0 = cuPath.elements[curpos];
        //fix small issue for  see last len of  number
        int lut = curpos;
        while (path0.elem == null) {
            path0 = cuPath.elements[--lut];
        }
        return path0.elem.offs;
    }

    private void updateFmts()
    {
        PathItem path = cuPath.elements[curpos];
        //fix small issue for  see last len of  number
        int lis;
        if (useRealIndex || !useVdir) {
            int lut = curpos;
            while (path.elem == null) {
                path = cuPath.elements[--lut];
            }
            lis = path.elem.length;
        }
        else {
            lis = path.vdir != null ? path.vdir.length : path.elem.length;
        }
        byte dlen = (byte) Integer.toHexString(lis).length();
        if (dlen % 2 == 1) {
            dlen++;
        }

        String clax = "%0" + dlen + (usedec ? 'd' : 'X');
        vdirfmt = clax + " %s";
        rfileFmt = clax + " %4s %s";
    }

//    @Override
//    public synchronized Object get(int index)
//    {
//        if (level < 0) {
//            return "<null>";
//        }
//        PathItem et = cuPath.elements[curpos];
//        final int vat = index + 1;
//        if (useVdir && et.vdir != null && !et.vdir.isNull(index)) {
//            if (et.vdir.get(index) instanceof Integer) {
//                index = (Integer) et.vdir.get(index);
//                et = cuPath.elements[et.parentID];
//            }
//            else {
//                ((short[]) objret[0])[0] = (short) (((IndexHelper.VEntry) et.vdir.get(index)).length > 0 ? BasicFile.TYPE_VDIR : BasicFile.TYPE_EVDIR);
//                objret[1] = String.format(vdirfmt, vat, ((IndexHelper.VEntry) et.vdir.get(index)).desc);
//                return objret;
//            }
//        }
//        IndexHelper.IEntry cus;
//        try {
//            cus = et.elem.get(index);
//        }
//        catch (RuntimeException ex) {
//            ((short[]) objret[0])[0] = 0;
//            objret[1] = "<NULL>";
//            return objret;
//        }
////        final String g = IExNameProvider.def.getDetail(IndexHelper.getCode(et.elem), cus, index);
//        final String g = cus.desc;
//        ((short[]) objret[0])[0] = IndexHelper.getModifier(cus);
//        objret[1] = String.format(rfileFmt, useRealIndex ? index + 1 : vat, getSType(IndexHelper.getCode(cus)), (g != null ? g : ""));
//
//        return objret;
//    }
    public Object get(int index)
    {
        PathItem et = cuPath.elements[curpos];
        try {
            if (useVdir && et.vdir != null) {
                if (index < 0 || index >= et.vdir.length) {
                    return null;
                }
                if (et.vdir.get(index) instanceof Integer) {
                    index = (Integer) et.vdir.get(index);
                    et = cuPath.elements[et.parentID];
                }
                else {
                    return et.vdir.get(index);
                }
            }
            return et.elem.get(index);
        }
        catch (Exception ex) {
        }
        return null;
    }

    //<editor-fold defaultstate="collapsed" desc="Dont used METHODS">
    @Override
    public boolean contains(Object o)
    {
        return false;
    }

    @Override
    public Iterator iterator()
    {
        return null;
    }

    @Override
    public Object[] toArray()
    {
        return null;
    }

    @Override
    public Object[] toArray(Object[] a)
    {
        return null;
    }

    @Override
    public boolean add(Object e)
    {
        return false;
    }

    @Override
    public boolean remove(Object o)
    {
        return false;
    }

    @Override
    public boolean containsAll(Collection c)
    {
        return false;
    }

    @Override
    public boolean addAll(Collection c)
    {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection c)
    {
        return false;
    }

    @Override
    public boolean removeAll(Collection c)
    {
        return false;
    }

    @Override
    public boolean retainAll(Collection c)
    {
        return false;
    }

    @Override
    public void clear()
    {

    }

    @Override
    public Object set(int index, Object element)
    {
        return null;
    }

    @Override
    public void add(int index, Object element)
    {

    }

    @Override
    public Object remove(int index)
    {
        return null;
    }

    @Override
    public int indexOf(Object o)
    {
        return -1;
    }

    @Override
    public int lastIndexOf(Object o)
    {
        return -1;
    }

    @Override
    public ListIterator listIterator()
    {
        return null;
    }

    @Override
    public ListIterator listIterator(int index)
    {
        return null;
    }

    @Override
    public List subList(int fromIndex, int toIndex)
    {
        return null;
    }
    //</editor-fold>

}
