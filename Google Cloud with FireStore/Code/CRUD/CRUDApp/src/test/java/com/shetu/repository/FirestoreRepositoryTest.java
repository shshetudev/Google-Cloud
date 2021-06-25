package com.shetu.repository;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.shetu.entity.City;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

@MicronautTest

public class FirestoreRepositoryTest {
  private static final Logger LOG = LoggerFactory.getLogger(FireStoreRepositoryImplTest.class);
  private GoogleCredentials googleCredentials;
  private Firestore db;

//  @BeforeEach
//  void setup() throws IOException {
//    String projectId = "river-pointer-308317";
//    String udProjectId = "rakizo-dev";
//    this.googleCredentials = getCredentials();
//    this.db = getFirestoreDB(googleCredentials, udProjectId);
//  }

  @Test
  void testGoogleCredentials() throws IOException {
    GoogleCredentials googleCredentials = getDefaultCredentials();
    System.out.println(googleCredentials);
  }

  @Test
  void getDefaultStorage() throws IOException {
//    Storage storage = StorageOptions.getDefaultInstance().getService();
    GoogleCredentials googleCredentials = getDefaultCredentials();
    Storage storage = StorageOptions.newBuilder().setCredentials(googleCredentials).build().getService();
    LOG.info("storage:[{}]",storage);
  }
  @Test
  void testGetFireStoreService() throws IOException {
    String projectId = "river-pointer-308317";
//    Firestore firestore = getFirestoreDB(getDefaultCredentials(), projectId);
    Firestore firestore = getFirestoreDB();
    Assertions.assertNotNull(firestore);
  }

  private GoogleCredentials getCredentials() throws IOException {
//    String jsonPath = "/home/shetu/Shetu's folder/Official/UD/RK/rakizo-dev-f59197306042.json";
    String jsonPath = "/home/shetu/RK-JSON-file/ud-account-file.json";
    return GoogleCredentials.fromStream(new FileInputStream(jsonPath))
      .createScoped(Collections.singletonList("https://www.googleapis.com/auth/cloud-platform"));
  }

  private GoogleCredentials getCredentialsWithJSON() throws IOException {
    String defaultJsonPath = "/home/shetu/JSON_FILE/shetu_gcp_key.json";
    return GoogleCredentials.fromStream(new FileInputStream(defaultJsonPath))
      .createScoped(Collections.singletonList("https://www.googleapis.com/auth/cloud-platform"));
  }
  private GoogleCredentials getDefaultCredentials() throws IOException {
    return GoogleCredentials.getApplicationDefault();
  }


  private Firestore getFirestoreDB() throws IOException {
    FirestoreOptions firestoreOptions = FirestoreOptions.getDefaultInstance().toBuilder()
      .setCredentials(GoogleCredentials.getApplicationDefault())
      .build();
    Firestore db = firestoreOptions.getService();
    return db;
  }

  private Firestore getFirestoreDB(GoogleCredentials credentials) {
    FirestoreOptions firestoreOptions = FirestoreOptions.getDefaultInstance().toBuilder()
      .setCredentials(credentials)
      .build();
    Firestore db = firestoreOptions.getService();
    return db;
  }

  private Firestore getFirestoreDB(GoogleCredentials credentials, String projectId) {
    FirestoreOptions firestoreOptions = FirestoreOptions.getDefaultInstance().toBuilder()
      .setProjectId(projectId)
      .setCredentials(credentials)
      .build();
    Firestore db = firestoreOptions.getService();
    return db;
  }


//  @AfterEach
//  void cleanup() {
//    this.db = null;
//    this.googleCredentials = null;
//  }

  // --------------------------- Experimental Test cases ----------------------
  @Test
  void testSave() throws ExecutionException, InterruptedException {
    Map<String, Object> docData = new HashMap<>();
    docData.put("name", "Chandpur");
    docData.put("state", "Bangladesh");
    docData.put("country", "Bangladesh");
    docData.put("regions", Arrays.asList("Hazigonj", "Motlob"));
    save(docData);
  }


  private void save(Map<String, Object> docData) throws ExecutionException, InterruptedException {
    // Add a new document asynchronously in collection cities with id: "Dhaka"
    ApiFuture<WriteResult> future = db.collection("cities").document("Dhaka").set(docData);
    // future.get() blocks on response
    System.out.println("Update time : " + future.get().getUpdateTime());
  }

  @Test
  void testFindAll() throws ExecutionException, InterruptedException {
    String collectionId = "cities";
  }

  private void findAllCollections(String collectionId) throws ExecutionException, InterruptedException {
    ApiFuture<QuerySnapshot> query = db.collection(collectionId).get();
    QuerySnapshot querySnapshot = query.get();
    List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
    for (QueryDocumentSnapshot document : documents) {
      System.out.println("Document ID: " + document.getId());
      System.out.println("Name: " + document.getString("name"));
      System.out.println("State: " + document.getString("state"));
      System.out.println("Country: " + document.getString("country"));
      System.out.println("Born: " + document.get("regions"));
    }
  }

  @Test
  void testFindDocumentByCollectionIdAndDocumentId() throws ExecutionException, InterruptedException {
    String collectionId = "cities";
    String documentId = "Dhaka";
    findDocument(collectionId, documentId);
  }

  private void findDocument(String collectionId, String documentId) throws ExecutionException, InterruptedException {
    DocumentReference docRef = db.collection(collectionId).document(documentId);
    // asynchronously retrieve the document
    ApiFuture<DocumentSnapshot> future = docRef.get();
    // future.get() blocks on response
    DocumentSnapshot document = future.get();
    if (document.exists()) {
      System.out.println("Document data: " + document.getData());
    } else {
      System.out.println("No such document");
    }
  }

  @Test
  void testMultipleDocumentsFromACollection() throws ExecutionException, InterruptedException {
    String collectionId = "cities";
    String documentId1 = "Dhaka";
    deleteMultipleDocuments(collectionId, documentId1);
  }

  private void deleteMultipleDocuments(String collectionId, String documentId1) throws ExecutionException, InterruptedException {
    // asynchronously retrieve multiple documents
    ApiFuture<QuerySnapshot> future = db.collection(collectionId).whereEqualTo(documentId1, true).get();
    // future.get() blocks on response
    List<QueryDocumentSnapshot> documents = future.get().getDocuments();
    for (QueryDocumentSnapshot document : documents) {
      // to Objectify the document
      // System.out.println(document.getId() + " => " + document.toObject(City.class));
      // document.getData() provides a Map
      System.out.println(document.getId() + " => " + document.getData());
    }
  }

  @Test
  void testDeleteFields() throws ExecutionException, InterruptedException {
    String collectionId = "cities";
    String documentId = "Dhaka";
    deleteFields(collectionId, documentId);
  }

  private void deleteFields(String collectionId, String documentId) throws ExecutionException, InterruptedException {
    DocumentReference docRef = db.collection(collectionId).document(documentId);
    Map<String, Object> updates = new HashMap<>();
    updates.put("capital", FieldValue.delete());
    // Update and delete the "capital" field in the document
    ApiFuture<WriteResult> writeResult = docRef.update(updates);
    System.out.println("Update time: " + writeResult.get());
  }

  @Test
  void testDeleteDocument() throws ExecutionException, InterruptedException {
    String collectionId = "cities";
    String documentId = "Dhaka";
    deleteDocument(collectionId, documentId);
  }

  private void deleteDocument(String collectionId, String documentId) throws ExecutionException, InterruptedException {
    // asynchronously delete a document
    ApiFuture<WriteResult> writeResult = db.collection(collectionId).document(documentId).delete();
    System.out.println("Update time: " + writeResult.get().getUpdateTime());
  }

  @Test
  void testDeleteCollection() {
    String collectionId = "cities";
    CollectionReference collection = db.collection(collectionId);
    int batchSize = 5;
    deleteCollection(collection, batchSize);
  }

  private void deleteCollection(CollectionReference collection, int batchSize) {
    try {
      // retrieve a small batch of documents to avoid out-of-memory errors
      ApiFuture<QuerySnapshot> future = collection.limit(batchSize).get();
      int deleted = 0;
      // future.get() blocks on document retrieval
      List<QueryDocumentSnapshot> documents = future.get().getDocuments();
      for (QueryDocumentSnapshot document : documents) {
        document.getReference().delete();
        ++deleted;
      }
      if (deleted >= batchSize) {
        // retrieve and delete another batch
        deleteCollection(collection, batchSize);
      }
    } catch (ExecutionException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  //------------------------------------ Tests on Custom Objects------------------------------//
  @Test
  void save() throws ExecutionException, InterruptedException {
    City city = new City("Los Angeles", "CA", "USA", false, 3900000L, Arrays.asList("west_coast", "socal"));
    Object obj = insertIntoDB(city);
    LOG.debug("Saved Object:{}", obj);
  }

  private Object insertIntoDB(Object object) throws ExecutionException, InterruptedException {
    ApiFuture<WriteResult> future = db.collection(object.getClass().getSimpleName().toLowerCase()).document().set(object);
    // block on response if required
    System.out.println("Update time : " + future.get().getUpdateTime());
    return object;
  }


}
