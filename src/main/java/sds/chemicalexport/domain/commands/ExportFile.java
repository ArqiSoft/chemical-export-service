package sds.chemicalexport.domain.commands;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.Map;
import sds.messaging.contracts.AbstractContract;

public class ExportFile extends AbstractContract {
    
    private UUID id;
    private UUID blobId;
    private String bucket;
    private String format;
    private Iterable<String> Properties;
    private HashMap<String, String> Map;
    private UUID userId;

    public ExportFile() {
        namespace = "Sds.ChemicalExport.Domain.Commands";
        contractName = ExportFile.class.getSimpleName();
    }

    /**
     * @return the id
     */
    @JsonProperty("Id")
    public UUID getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * @return the userId
     */
    @JsonProperty("UserId")
    public UUID getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    /**
     * @return the bucket
     */
    @JsonProperty("Bucket")
    public String getBucket() {
        return bucket;
    }

    /**
     * @param bucket the bucket to set
     */
    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    /**
     * @return the blobId
     */
    @JsonProperty("BlobId")
    public UUID getBlobId() {
        return blobId;
    }

    /**
     * @param blobId the blobId to set
     */
    public void setBlobId(UUID blobId) {
        this.blobId = blobId;
    }

    @JsonProperty("Format")
    public String getFormat() {
        return format;
    }

    @JsonProperty("Properties")
    public Iterable<String> getProperties() {
        return Properties;
    }
    
    @JsonProperty("Map")
    public HashMap<String, String> getMap() {
        return Map;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void setProperties(Iterable<String> Properties) {
        this.Properties = Properties;
    }

    public void setMap(HashMap<String, String> Map) {
        this.Map = Map;
    }
    
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("%s [id=%s, blobId=%s, bucket=%s, userId=%s, namespace=%s, contractName=%s, correlationId=%s]",
                ExportFile.class.getSimpleName(), id, blobId, bucket, userId, namespace, contractName, getCorrelationId());
    }


}
