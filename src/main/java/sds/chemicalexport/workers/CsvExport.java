package sds.chemicalexport.workers;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import sds.chemicalexport.domain.interfaces.IChemicalExport;
import sds.chemicalexport.domain.models.PropertyValue;
import sds.chemicalexport.domain.models.Record;

public class CsvExport implements IChemicalExport {

    @Override
    public void Export(Iterator<Record> records, OutputStream otputStream, List<String> properties, Map<String, String> map) {

        try {

            try (PrintWriter pw = new PrintWriter(otputStream)) {
                StringBuilder sb = new StringBuilder();

                HashMap<String, HashMap<Long, String>> mapProperties = new HashMap<>();

                long numOfRecords = 0;
                
                List<String> colNames = new ArrayList<>();
                colNames.add("Properties.Fields.SMILES");
                mapProperties.put("Properties.Fields.SMILES", new HashMap<>());
                properties.remove("Properties.Fields.SMILES");
                for(int i = 0; i < properties.size(); i++ )
                {
                    mapProperties.put(properties.get(i), new HashMap<>());
                    if(map.containsKey(properties.get(i)))
                    {
                        colNames.add(map.get(properties.get(i)));
                    }
                    else
                    {
                        colNames.add(properties.get(i));
                    }
                }

                while (records.hasNext()) {

                    Record record = records.next();
                    if (record.getProperties().size()!=0 && record.getMol() != "") {
                        numOfRecords++;

                        Object[] smilesList = record.getProperties().stream().filter(property -> property.getName().toLowerCase().contains("smiles")).map(prop -> prop.getValue().toString()).toArray();
                        String chemicalSmiles = "";
                        if (smilesList.length > 1) {
                            chemicalSmiles = record.getProperties().stream().filter(p -> p.getName().equalsIgnoreCase("Properties.Fields.SMILES")).map(prop -> prop.getValue().toString()).findFirst().get();
                        } else {
                            chemicalSmiles = smilesList[0].toString();
                        }

                        mapProperties.get("Properties.Fields.SMILES").put(record.getIndex(), chemicalSmiles);

                        for (PropertyValue property : record.getProperties()) {
                            if (properties.contains(property.getName())) {

                                HashMap<Long, String> temp = mapProperties.get(property.getName());
                                temp.put(record.getIndex(), property.getValue().toString());

                                mapProperties.put(property.getName(), temp);
                            }
                        }
                    }
                }

                for(int i = 0; i < colNames.size(); i++ )
                {
                    sb.append(colNames.get(i) + ',');
                }
                sb.append('\n');
                
                
                for (long i = 0; i <= numOfRecords; i++) {
                    String smiles = mapProperties.get("Properties.Fields.SMILES").get(i);
                    if(smiles == null){
                        continue;
                    }
                    sb.append(smiles);
                    sb.append(',');
                    for (int j = 0; j < properties.size(); j++  ) {
                        if (mapProperties.get(properties.get(j)).containsKey(i)) {
                            sb.append(mapProperties.get(properties.get(j)).get(i));
                        } else {
                            sb.append("");
                        }
                        sb.append(',');
                    }

                    sb.append('\n');
                }

                pw.write(sb.toString());
            }
            otputStream.flush();

        } catch (IOException ex) {
            Logger.getLogger(CsvExport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
