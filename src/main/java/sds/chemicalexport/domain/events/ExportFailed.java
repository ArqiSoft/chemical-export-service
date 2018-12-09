package sds.chemicalexport.domain.events;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import sds.messaging.contracts.AbstractContract;

public class ExportFailed extends AbstractContract {

    private UUID id;
    private String timeStamp;
    private UUID userId;
    private String Message;
    private String Bucket;
    private UUID FileId;
    private String Format;
    
    public ExportFailed() {
        namespace = "Sds.OfficeProcessor.Domain.Events";
        contractName = ExportFailed.class.getSimpleName();
    }

    public void setMessage(String Message) {
        this.Message = Message;
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

    @JsonProperty("Message")
    public String getMessage() {
        return Message;
    }

    public void setBucket(String Bucket) {
        this.Bucket = Bucket;
    }

    public void setFileId(UUID FileId) {
        this.FileId = FileId;
    }

    public void setFormat(String Format) {
        this.Format = Format;
    }

    @JsonProperty("Bucket")
    public String getBucket() {
        return Bucket;
    }
    
    @JsonProperty("FileId")
    public UUID getFileId() {
        return FileId;
    }

    @JsonProperty("Format")
    public String getFormat() {
        return Format;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("%s [id=%s, timeStamp=%s, userId=%s, namespace=%s, contractName=%s, correlationId=%s]",
                ExportFailed.class.getSimpleName(), id, timeStamp, userId, namespace, contractName, getCorrelationId());
    }
    
    

}
