package com.cmu.heinz.sensoremulator;

import com.google.gson.Gson;
import com.microsoft.azure.documentdb.ConnectionPolicy;
import com.microsoft.azure.documentdb.ConsistencyLevel;
import com.microsoft.azure.documentdb.Database;
import com.microsoft.azure.documentdb.Document;
import com.microsoft.azure.documentdb.DocumentClient;
import com.microsoft.azure.documentdb.DocumentClientException;
import com.microsoft.azure.documentdb.DocumentCollection;
import java.util.List;

public class DocumentDb {

    // Replace with your DocumentDB end point and master key.
    private String END_POINT = "{Endpoint Here}";
    private String MASTER_KEY = "{Key Here}"

    private DocumentClient documentClient = new DocumentClient(END_POINT, MASTER_KEY,
            ConnectionPolicy.GetDefault(), ConsistencyLevel.Session);
    private Gson gson = new Gson();

    private String DATABASE_ID = "{Database Name}";

    private Database myDatabase;
    private DocumentCollection documentCollection;

    public DocumentDb(String DATABASE_ID, String END_POINT, String MASTER_KEY) throws DocumentClientException {
        this.DATABASE_ID = DATABASE_ID;
        this.END_POINT = END_POINT;
        this.MASTER_KEY = MASTER_KEY;
        myDatabase = getDatabase(documentClient);
    }

    public SensorDataPoint addSensorReading(String COLLECTION_ID, SensorDataPoint sdp) {

        documentCollection = getDocumentCollection(COLLECTION_ID);

        Document doc = new Document(sdp.serialize());
        try {
            // Persist the document using the DocumentClient.
            doc = documentClient.createDocument(
                    documentCollection.getSelfLink(), doc, null,
                    false).getResource();
            return gson.fromJson(doc.toString(), SensorDataPoint.class);
        } catch (DocumentClientException e) {
            System.out.println(e.toString());
            return null;
        }
    }

    private Database getDatabase(DocumentClient documentClient) {
        if (myDatabase == null) {
            List<Database> databaseList = documentClient
                    .queryDatabases(
                            "SELECT * FROM root r WHERE r.id='" + DATABASE_ID
                            + "'", null).getQueryIterable().toList();
            if (databaseList.size() > 0) {
                // Cache the database object so we won't have to query for it
                // later to retrieve the selfLink.
                myDatabase = databaseList.get(0);
            } else {
                // Create the database if it doesn't exist.
                try {
                    Database databaseDefinition = new Database();
                    databaseDefinition.setId(DATABASE_ID);

                    myDatabase = documentClient.createDatabase(
                            databaseDefinition, null).getResource();
                } catch (DocumentClientException e) {
                    // TODO: Something has gone terribly wrong - the app wasn't
                    // able to query or create the collection.
                    // Verify your connection, endpoint, and key.
                    System.out.println(e.toString());
                }
            }
        }
        return myDatabase;
    }

    private DocumentCollection getDocumentCollection(String COLLECTION_ID) {
        // Get the collection if it exists.
        List<DocumentCollection> collectionList = documentClient
                .queryCollections(
                        myDatabase.getSelfLink(),
                        "SELECT * FROM root r WHERE r.id='" + COLLECTION_ID
                        + "'", null).getQueryIterable().toList();

        if (collectionList.size() > 0) {
            // Cache the collection object so we won't have to query for it
            // later to retrieve the selfLink.
            return collectionList.get(0);
        } else {
            // Create the collection if it doesn't exist.
            try {
                DocumentCollection collectionDefinition = new DocumentCollection();
                collectionDefinition.setId(COLLECTION_ID);

                return documentClient.createCollection(
                        myDatabase.getSelfLink(),
                        collectionDefinition, null).getResource();
            } catch (DocumentClientException e) {
                System.out.println("Error creating or fetching collection.");
                return null;
            }
        }

    }

}
