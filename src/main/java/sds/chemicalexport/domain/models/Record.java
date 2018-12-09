package sds.chemicalexport.domain.models;

import java.util.List;

public class Record {
    
    public long Index;
    private String Mol;
    private String Smiles;
    private List<PropertyValue> Properties;

    public Record() {
    }

    public Record(long Index, String Mol, List<PropertyValue> Properties) {
        this.Index = Index;
        this.Mol = Mol;
        this.Properties = Properties;
    }

    public long getIndex() {
        return Index;
    }

    public String getMol() {
        return Mol;
    }

    public String getSmiles() {
        return Smiles;
    }

    public List<PropertyValue> getProperties() {
        return Properties;
    }
    
    
}
