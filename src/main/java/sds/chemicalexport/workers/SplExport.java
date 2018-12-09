/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sds.chemicalexport.workers;

import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import sds.chemicalexport.domain.interfaces.IChemicalExport;
import sds.chemicalexport.domain.models.Record;

public class SplExport implements IChemicalExport {

    @Override
    public void Export(Iterator<Record> records, OutputStream otputStream, List<String> properties, Map<String, String> map) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
