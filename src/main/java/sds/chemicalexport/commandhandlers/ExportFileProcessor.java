package sds.chemicalexport.commandhandlers;

import com.mongodb.client.MongoDatabase;
import sds.chemicalexport.domain.commands.ExportFile;
import sds.chemicalexport.domain.events.*;
import java.io.IOException;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.npspot.jtransitlight.JTransitLightException;
import com.npspot.jtransitlight.consumer.ReceiverBusControl;
import com.npspot.jtransitlight.publisher.IBusControl;
import com.sds.storage.BlobInfo;
import com.sds.storage.BlobStorage;
import com.sds.storage.Guid;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.io.FilenameUtils;
import sds.messaging.callback.AbstractMessageProcessor;
import sds.chemicalexport.domain.interfaces.IChemicalExport;
import sds.chemicalexport.domain.models.MimeType;
import sds.chemicalexport.domain.models.Record;
import sds.chemicalexport.workers.*;


@Component
public class ExportFileProcessor extends AbstractMessageProcessor<ExportFile> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExportFileProcessor.class);

    ReceiverBusControl receiver;
    IBusControl bus;
    BlobStorage storage;
    MongoDatabase db;

    @Autowired
    public ExportFileProcessor(ReceiverBusControl receiver, IBusControl bus,
            BlobStorage storage, MongoDatabase db) throws JTransitLightException, IOException {
        this.bus = bus;
        this.receiver = receiver;
        this.storage = storage;
        this.db = db;
    }

    public void process(ExportFile message) {
        try {
            BlobInfo blobInfo = storage.getFileInfo(new Guid(message.getBlobId()), message.getBucket());

            if (blobInfo == null) {
                ExportFailed exportFailded = new ExportFailed();
                exportFailded.setId(message.getId());
                exportFailded.setFileId(message.getBlobId());
                exportFailded.setBucket(message.getBucket());
                exportFailded.setFormat(message.getFormat());
                exportFailded.setMessage("Cannot find blob with id " + message.getBlobId() + " in bucket " + message.getBucket() + ".");

                bus.publish(exportFailded);
                return;
            }

            IChemicalExport exporter = null;

            switch (message.getFormat().toLowerCase()) {
                case "sdf":
                    exporter = new SdfExport();
                    break;
                case "csv":
                    exporter = new CsvExport();
                    break;
                case "spl":
                    exporter = new SplExport();
                    break;
                default:
                    ExportFailed exportFailded = new ExportFailed();
                    exportFailded.setId(message.getId());
                    exportFailded.setFileId(message.getBlobId());
                    exportFailded.setBucket(message.getBucket());
                    exportFailded.setFormat(message.getFormat());
                    exportFailded.setMessage("Queried export format is not supported.");

                    bus.publish(exportFailded);

                    return;
            }

            String fileName = FilenameUtils.removeExtension(blobInfo.getFileName()) + "." + message.getFormat().toLowerCase();

            if (exporter != null) {
                Guid blobId = Guid.newGuid();
                try (OutputStream stream = storage.OpenUploadStream(blobId, fileName, MimeType.GetFileMIME(message.getFormat()), message.getBucket(), new HashMap<>())) {
                    OsdrRecordsProvider provider = new OsdrRecordsProvider(db, storage);

                    Iterator<Record> recordsEnumerator = provider.GetRecords(message.getBlobId(), message.getBucket());

                    exporter.Export(recordsEnumerator, stream, IteratorUtils.toList(message.getProperties().iterator()), message.getMap());

                    stream.flush();

                    ExportFinished exportFinished = new ExportFinished();
                    exportFinished.setId(message.getId());
                    exportFinished.setSourceBlobId(message.getBlobId());
                    exportFinished.setFileName(fileName);
                    exportFinished.setExportBlobId(blobId);
                    exportFinished.setFormat(message.getFormat());
                    exportFinished.setExportBucket(message.getBucket());
                    exportFinished.setUserId(message.getUserId());
                    exportFinished.setTimeStamp(getTimestamp());
                    exportFinished.setCorrelationId(UUID.randomUUID());
                    
                    bus.publish(exportFinished);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            ExportFailed exportFailed = new ExportFailed();
            exportFailed.setId(message.getId());
            exportFailed.setMessage("File can not be exported. Details: " + ex.getMessage());
            exportFailed.setCorrelationId(message.getCorrelationId());
            exportFailed.setUserId(message.getUserId());
            exportFailed.setTimeStamp(getTimestamp());

            bus.publish(exportFailed);
        }
    }

    private String getTimestamp() {
        //("yyyy-MM-dd'T'HH:mm:ss'Z'")
        return LocalDateTime.now().toString();
    }

}
