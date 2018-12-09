/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sds.chemicalexport.workers;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import sds.chemicalexport.domain.interfaces.IChemicalExport;
import sds.chemicalexport.domain.models.PropertyValue;
import sds.chemicalexport.domain.models.Record;


public class SdfExport implements IChemicalExport {

    private List<String> Properties;
    private Map<String, String> Map;

    @Override
    public void Export(Iterator<Record> records, OutputStream otputStream, List<String> properties, Map<String, String> map) {
        try {
            OutputStreamWriter sw = new OutputStreamWriter(otputStream);

            Properties = properties;
            Map = map;
            
            StringBuilder sb = new StringBuilder();

            while (records.hasNext()) {
                Record record = records.next();

                sw.write(String.format("%n%s", RecordToSdf(record)));
            }
            sw.flush();
        } catch (IOException ex) {
            Logger.getLogger(SdfExport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String RecordToSdf(Record record) {
        StringBuilder sb = new StringBuilder();

        if (record.getMol() == null) {
            return "";
        }
        sb.append(String.format("%n%s", record.getMol()));
        
        for (PropertyValue property : record.getProperties()) {
            if (Properties.contains(property.getName())) {
                if (Map.containsKey(property.getName())) {
                    sb.append(String.format("%n%s", "> <" + Map.get(property.getName()) + ">"));
                } else {
                    sb.append(String.format("%n%s", "> <" + property.getName() + ">"));
                    }
                sb.append(String.format("%n%s", property.getValue()));
                sb.append(String.format("%n", ""));;
            }

        }
        sb.append(String.format("%n%s", "$$$$"));
        return sb.toString();
    }
}
