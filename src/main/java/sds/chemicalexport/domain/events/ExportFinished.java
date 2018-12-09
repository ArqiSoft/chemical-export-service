package sds.chemicalexport.domain.events;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sds.storage.Guid;
import sds.messaging.contracts.AbstractContract;

public class ExportFinished extends AbstractContract {

    private UUID id;
    private UUID sourceBlobId;
    private String exportBucket;
    private Guid exportBlobId;
    private String Format;
    private String fileName;
    private String timeStamp;
    private UUID userId;

    public ExportFinished() {
        namespace = "Sds.ChemicalExport.Domain.Events";
        contractName = ExportFinished.class.getSimpleName();
    }

    /**
     * @return the id
     */
    @JsonProperty("Id")
    public UUID getId() {
        return id;
    }

    /**
     * @return the id of blob
     */
    @JsonProperty("SourceBlobId")
    public UUID getSourceBlobId() {
        return sourceBlobId;
    }

    /**
     * @param id the id to set
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * @return the timeStamp
     */
    @JsonProperty("TimeStamp")
    public String getTimeStamp() {
        return timeStamp;
    }

    /**
     * @param timeStamp the timeStamp to set
     */
    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
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

    public void setSourceBlobId(UUID sourceBlobId) {
        this.sourceBlobId = sourceBlobId;
    }

    public void setExportBucket(String exportBucket) {
        this.exportBucket = exportBucket;
    }

    public void setExportBlobId(Guid exportBlobId) {
        this.exportBlobId = exportBlobId;
    }

    public void setFormat(String Format) {
        this.Format = Format;
    }

    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @JsonProperty("ExportBucket")
    public String getExportBucket() {
        return exportBucket;
    }

    @JsonProperty("ExportBlobId")
    public UUID getExportBlobId() {
        return exportBlobId.getUUID();
    }

    @JsonProperty("Format")
    public String getFormat() {
        return Format;
    }

    @JsonProperty("FileName")
    public String getFileName() {
        return fileName;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("%s [id=%s, timeStamp=%s, userId=%s, namespace=%s, contractName=%s]",
                ExportFinished.class.getSimpleName(), id, timeStamp, userId, namespace, contractName);
    }

}
