package sds.chemicalexport.workers;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.sds.storage.BlobStorage;
import com.sds.storage.Guid;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.bson.*;
import org.bson.BsonDocument;
import org.bson.conversions.Bson;
import sds.chemicalexport.domain.models.PropertyValue;
import sds.chemicalexport.domain.models.Record;

public class OsdrRecordsProvider {

    private final MongoDatabase database;
    private final BlobStorage blobStorage;

    protected MongoCollection<BsonDocument> Files;
    protected MongoCollection<BsonDocument> Records;

    public OsdrRecordsProvider(MongoDatabase database, BlobStorage blobStorage) {
        this.database = database;
        this.blobStorage = blobStorage;
        this.Files = database.getCollection("Files", BsonDocument.class);
        this.Records = database.getCollection("Records", BsonDocument.class);
    }

    public Iterator<Record> GetRecords(UUID blobId, String bucket) {

        Bson fileFilter
                = Filters.and(Filters.eq("Blob._id", new BsonBinary(BsonBinarySubType.UUID_LEGACY, new Guid(blobId).toByteArray())), Filters.eq("Blob.Bucket", bucket));
        BsonDocument fileView = Files.find(fileFilter).first();
        BsonBinary fileId = fileView.get("_id").asBinary();
        Iterator<Record> recordsIterator = Records.find(Filters.eq("FileId", fileId)).map(item -> {
            long index = item.get("Index").asInt64().longValue();
            String mol = GetMolData(fileId, bucket, index);
            List<PropertyValue> properties = GetAllProperties(fileId, index);
            return new Record(index, mol, properties);
        }).iterator();
        return recordsIterator;
    }

    private String GetMolData(BsonBinary fileId, String bucket, long index) {

        Bson filter = Filters.and(Filters.eq("FileId", fileId), Filters.eq("Index", index));

        BsonDocument recordView = Records.find(filter).first();

        if (recordView.containsKey("Blob")) {
            byte[] blobId = recordView.get("Blob").asDocument().get("_id").asBinary().getData();

            InputStream molBlobStream = blobStorage.getFileStream(Guid.fromByteArray(blobId), bucket);

            return new BufferedReader(new InputStreamReader(molBlobStream)).lines().collect(Collectors.joining("\n"));
        }
        return "";
    }

    private List<PropertyValue> GetAllProperties(BsonBinary fileId, long index) {
        Bson filter = Filters.and(Filters.eq("FileId", fileId), Filters.eq("Index", index));

        BsonDocument recordView = Records.find(filter).first();
        List<PropertyValue> chemicalProperties = new ArrayList<>();
        List<PropertyValue> fields = new ArrayList<>();
        if (recordView.containsKey("Properties")) {
            BsonDocument properties = recordView.get("Properties").asDocument();
            if (properties.containsKey("ChemicalProperties")) {
                chemicalProperties = properties.get("ChemicalProperties").asArray().stream().map(property -> {
                    String name = "Properties.ChemicalProperties." + property.asDocument().get("Name").asString().getValue();
                    BsonValue valueBson = property.asDocument().get("Value");
                    Object value = " ";
                    if (valueBson.isNumber()) {
                        value = valueBson.asNumber().doubleValue();
                    }
                    if (valueBson.isString() || valueBson.isSymbol()) {
                        value = valueBson.asString().getValue();
                    }
                    return new PropertyValue(name, value);
                }).collect(Collectors.toList());
            }

            if (properties.containsKey("Fields")) {
                fields = properties.get("Fields").asArray().stream().map(property -> {
                    String name = "Properties.Fields." + property.asDocument().get("Name").asString().getValue();
                    BsonValue valueBson = property.asDocument().get("Value");
                    Object value = " ";
                    if (valueBson.isNumber()) {
                        value = valueBson.asNumber().doubleValue();
                    }
                    if (valueBson.isString() || valueBson.isSymbol()) {
                        value = valueBson.asString().getValue();
                    }

                    return new PropertyValue(name, value);
                }).collect(Collectors.toList());
            }
        }

        return Stream.concat(chemicalProperties.stream(), fields.stream())
                .collect(Collectors.toList());

    }
}
