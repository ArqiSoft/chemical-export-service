/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sds.chemicalexport.domain.models;

import java.util.HashMap;
import java.util.Map;

public class MimeType {

    private static final Map<String, String> mimes = new HashMap<String, String>();

    static {
        mimes.put(".sdf", "chemical/x-mdl-sdfile");
        mimes.put(".mol", "chemical/x-mdl-molfile");
        mimes.put(".jdx", "chemical/x-jcamp-dx");
        mimes.put(".dx", "chemical/x-jcamp-dx");
        mimes.put(".rdx", "chemical/x-mdl-rxnfile");
        mimes.put(".rdf", "chemical/x-mdl-rdfile");
        mimes.put(".cif", "chemical/x-cif");
        mimes.put(".cdx", "chemical/x-cdx");
    }

    public static String GetFileMIME(String extention) {
        if (mimes.containsKey(extention)) {
            return mimes.get(extention);
        }

        return "application/octet-stream";
    }
}
