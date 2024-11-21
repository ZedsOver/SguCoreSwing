/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sgucore.data;

import com.DeltaSKR.IO.BytePointer;
import com.DeltaSKR.IO.interfce.WriteSeek;
import dLibs.QuickISO.ISO;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;
import main_app.IndexHelper;
import parsers.BasicFile;
import parsers.Pkg_box;
import parsers.pkg.IISO_Root;

/**
 *
 * @author armax
 */
public class ISO_Root extends IISO_Root {

    public ISO_Root(BasicFile parent)
    {
        super(parent, Flags.SVOF, AFS.class);
    }

    @Override
    public void writeCopy(final Pkg_box src, WriteSeek outa, boolean names) throws IOException
    {
        try {
            if (src.jindex == null) {
                throw new NullPointerException("Error invalid jindex data");
            }
            final ISO ctc = new ISO();
            final ISO.quickiso_ctx_t ctx = new ISO.quickiso_ctx_t();
            ctc.quickiso_open(ctx, outa.asOutputStream());
            ctx.Volume = "ISO-9660";
            final boolean[] ver = {false};
            final boolean[] mayus = {false};
            try {
                int psp = -1;
                try {
                    psp = IndexHelper.findIndexFromTree(src.jindex, null, "UMD_DATA.BIN", 0, true);
                }
                catch (Exception e) {
                }
                if (psp > -1) {
                    BasicFile eps = src.readFile(psp);
                    if (eps.length() >= 9) {
                        ctx.System_ID = ctx.Application = "PSP GAME";
                        eps.seek(0);
                        String tax = eps.readStr(9, "windows-1252").trim();
                        ctx.Volume = tax.isEmpty() ? "unknown-umd" : tax;
                    }
                }
                else {
                    psp = -1;
                    try {
                        psp = IndexHelper.findIndexFromTree(src.jindex, null, "SYSTEM.CNF", 0, true);
                    }
                    catch (Exception e) {
                    }
                    if (psp > -1) {
                        ctx.System_ID = ctx.Application = "PLAYSTATION";
                        BasicFile eps = src.readFile(psp);
                        if (eps.length() > 0) {
                            eps.seek(0);
                            BufferedReader trax = new BufferedReader(new InputStreamReader(eps.asInputStream()), 256);
                            String tax = trax.readLine().trim();
                            int a = tax.lastIndexOf('\\') + 1;
                            int b = tax.lastIndexOf(';');
                            tax = tax.substring(a, b < a ? tax.length() : b).trim();
                            ctx.Volume = tax.isEmpty() ? "unknown-ps" : tax;
                            ver[0] = true;
                            mayus[0] = true;
                        }
                    }
                }
            }
            catch (Exception ex) {
                //ignoring error caused by get ID and type
            }
            IndexHelper.DVFor ca = new IndexHelper.DVFor() {
                @Override
                public boolean onValue(List<IndexHelper.VEntry> scope, final int index) throws Exception
                {
                    IndexHelper.IEntry bim = src.jindex.get(index);
                    final String ox = IndexHelper.getPathFromScope(scope, IndexHelper.getName(src.jindex.get(index))) + (ver[0] ? ";1" : "");

                    if (bim instanceof IndexHelper.SEntry) {
                        ctc.quickiso_add_entry(ctx, mayus[0] ? ox.toUpperCase() : ox, ((File) ((IndexHelper.SEntry) bim).source).getPath());

                    }
                    else {
                        ctc.quickiso_add_entry(ctx, new BytePointer((mayus[0] ? ox.toUpperCase() : ox).getBytes("windows-1252"), 0), new ISO.Qms_FileWriter() {

                            @Override
                            public long size()
                            {
                                return src.sizeOf(index);
                            }

                            @Override
                            public boolean isInputStream()
                            {
                                return false;
                            }

                            @Override
                            public InputStream createInput() throws IOException
                            {
                                return null;
                            }

                            @Override
                            public void writeInput(OutputStream out) throws IOException
                            {
                                src.readFile(index).exportRaw(out);

                            }

                        });
                    }
                    return true;
                }
            };
            if (src.jindex.vdir == null) {
                for (int i = 0; i < src.jindex.length; i++) {
                    ca.onValue(Collections.EMPTY_LIST, i);
                }
            }
            else {
                IndexHelper.forTree(src.jindex.vdir, false, ca);
            }
            ctc.quickiso_close(ctx);
        }
        catch (Exception ex) {
            IOException exa = new IOException(ex.getMessage());
            exa.setStackTrace(ex.getStackTrace());
            throw exa;
        }
    }

}
