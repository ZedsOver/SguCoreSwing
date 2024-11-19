/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package parsers;

import com.DeltaSKR.lang.ArrayUtil;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

/**
 *
 * @author armax
 */
public class Pkg_Adapter_generator {

    public static String[][] genData(String path) throws IOException
    {
        InputStream in = new FileInputStream(path);
        try {
            return genData(in);
        }
        finally {
            in.close();
        }
    }

    public static String[][] genDataSeq(String[] paths) throws IOException
    {
        Vector<InputStream> vec = new Vector();
        for (String in : paths) {
            vec.add(new FileInputStream(in));
        }
        SequenceInputStream in = new SequenceInputStream(vec.elements());
        try {
            return genData(in);
        }
        finally {
            in.close();
        }
    }

    public static String[][] genData(InputStream in) throws IOException
    {
        ArrayList tempo = new ArrayList();
        ArrayList<String[]> cas = new ArrayList();
        BufferedReader ca = new BufferedReader(new InputStreamReader(in));
        while (true) {
            String lin = ca.readLine();
            if (lin == null) {
                break;
            }
            lin = lin.trim().substring(1);
            if (lin.isEmpty()) {
                break;
            }
            if (tempo.contains(lin)) {
                continue;
            }
            tempo.add(lin);
            cas.add(lin.split(":"));
        }
        return (String[][]) cas.toArray(new String[0][]);
    }

    static final String pas = "./";

    public static void main(String[] args) throws IOException
    {
//        makeAnalizer(
//                pas + "com.neon.sgu/xeno/tumba/pruebaBT3.wbfs.txt",
//                pas + "com.neon.sgu/xeno/tumba/prueba BT2 wii.ISO.txt",
//                pas + "com.neon.sgu/xeno/tumba/DragonBall Z - Budokai Tenkaichi (Europe, Australia) (En,Ja,Fr,De,Es,It) PS2.iso.txt",
//                pas + "com.neon.sgu/xeno/tumba/DragonBall Z - Budokai Tenkaichi 2 ps2.iso.txt",
//                pas + "com.neon.sgu/xeno/tumba/DragonBall Z - Budokai Tenkaichi 3 (USA) (En,Ja).iso.txt",
//                pas + "com.neon.sgu/xeno/tumba/Dragon Ball Z - Tenkaichi Tag Team.iso.txt"
//        );
        makeAnalizer(
                pas + "com.neon.sgu/xeno/tumba/0404 - Dragon Ball Z - Shin Budokai (Japan) (v1.02).iso.txt",
                pas + "com.neon.sgu/xeno/tumba/0420 - Dragon Ball Z - Shin Budokai (Europe) (En,Fr,De,Es,It) (v1.01).iso.txt",
                pas + "com.neon.sgu/xeno/tumba/0950 - Dragon Ball Z - Shin Budokai Another Road (USA).iso.txt",
                pas + "com.neon.sgu/xeno/tumba/1042 - Dragon Ball Z - Shin Budokai 2 (Japan) (v1.02).iso.txt",
                pas + "com.neon.sgu/xeno/tumba/1736 - Dragon Ball Evolution (Japan).iso.txt",
                pas + "com.neon.sgu/xeno/tumba/Dragon Ball Z - Budokai 2-GC.iso.txt",
                pas + "com.neon.sgu/xeno/tumba/Dragon Ball Z - Budokai (Europe) (En,Fr,De,Es,It).iso.txt",
                pas + "com.neon.sgu/xeno/tumba/DragonBall Z - Budokai 2 (Europe) (En,Fr,De,Es,It).iso.txt",
                pas + "com.neon.sgu/xeno/tumba/DragonBall Z - Budokai 3 (USA).iso.txt",
                pas + "com.neon.sgu/xeno/tumba/DragonBall Z - Budokai (USA) (En,Ja).iso.txt",
                pas + "com.neon.sgu/xeno/tumba/DragonBall Z - Infinite World (Europe) (En,Fr,De,Es,It).iso.txt"
        );
//        makeAnalizer(pas + "com.neon.sgu/xeno/tumba/prueba BT2 wii.ISO.txt");
//        makeAnalizer(pas + "com.neon.sgu/xeno/tumba/pruebaBT3.wbfs.txt");
    }
    public static ClassLoader clo;

    public static void makeAnalizer(String... ar) throws IOException
    {
        String[][] doks = genDataSeq(ar);

        Hashtable<String, ArrayList<String>> pa = new Hashtable();
        //generating individual pkgBoxes
        for (String[] tok : doks) {
            for (int c = 0; c < tok.length - 1; c++) {
                String sup = tok[c].substring(3);
                if (pa.containsKey(sup)) {
                    continue;
                }
                pa.put(sup, new ArrayList<String>());
            }
        }
        //adding childs
        for (String[] tok : doks) {
            for (int c = 0; c < tok.length - 1; c++) {
                String sup1 = tok[c].substring(3);
                ArrayList<String> pm = pa.get(sup1);
                if (tok[c + 1].indexOf("$") > 0) {
                    System.out.println("//ERR?: " + tok[c + 1]);
                }
                if (!pm.contains(tok[c + 1]) && !(tok[c + 1].endsWith(".UNK_BIN")
                        || tok[c + 1].indexOf("$") > 0)) {
                    pm.add(tok[c + 1]);
                }
            }
        }
        for (Map.Entry<String, ArrayList<String>> cs : pa.entrySet()) {
            ArrayList<String> pm = cs.getValue();
            ArrayUtil.sort(true, pm, null);
            for (int i = 0; i < pm.size(); i++) {
                pm.set(i, pm.get(i).substring(3));
            }
            for (int i = 1; i < pm.size();) {
                if (!pm.get(i - 1).equals(pm.get(i))) {
                    i++;
                    continue;
                }
                pm.remove(i);
            }
        }
        //generating tree data maker
        for (Map.Entry<String, ArrayList<String>> cs : pa.entrySet()) {
            String cls = cs.getKey();
            if (cs.getValue().isEmpty()) {
                continue;
            }
            System.out.printf("Pkg_Adapter _%s = new Pkg_Adapter(%s.class);\n", cls.substring(cls.lastIndexOf('.') + 1), cls);
        }
        for (Map.Entry<String, ArrayList<String>> cs : pa.entrySet()) {
            String cls = cs.getKey();
            cls = cls.substring(cls.lastIndexOf('.') + 1);
            int ub = cs.getValue().size() - 1;
            if (ub < 0) {
                continue;
            }
            System.out.printf("_%s.setParsers(", cls);
            for (String ks : cs.getValue()) {
                boolean isadp = pa.containsKey(ks) && !pa.get(ks).isEmpty();
                String lls = isadp ? ks.substring(ks.lastIndexOf('.') + 1) : ks;
                System.out.printf(isadp ? "_%s" : "%s.class", lls);
                if (ub-- > 0) {
                    System.out.print(", ");
                }
            }
            System.out.println(");");
        }

    }
}
