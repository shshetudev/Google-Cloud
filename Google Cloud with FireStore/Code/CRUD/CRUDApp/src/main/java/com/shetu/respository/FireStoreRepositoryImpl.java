package com.shetu.respository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Singleton
public class FireStoreRepositoryImpl<T> implements AbstractRepository {
  private static final Logger LOG = LoggerFactory.getLogger(FireStoreRepositoryImpl.class);

  ObjectMapper mapper = new ObjectMapper();
  private Firestore db = getFirestoreDB(getCredentials(), "rakizo-dev");

  //-------------------------------------------------------------
  // Configuration
  private GoogleCredentials getCredentials() {
    String jsonPath = "/home/shetu/RK-JSON-file/ud-account-file.json";
//    String jsonPath = "/home/shetu/Shetu's folder/Official/UD/RK/rakizo-dev-f59197306042.json";
    GoogleCredentials googleCredentials;
    try {
      googleCredentials = GoogleCredentials.fromStream(new FileInputStream(jsonPath))
        .createScoped(Collections.singletonList("https://www.googleapis.com/auth/cloud-platform"));
    } catch (IOException e) {
      throw new RuntimeException("Credential exception");
    }
    return googleCredentials;
  }

  private Firestore getFirestoreDB(GoogleCredentials credentials, String projectId) {
    FirestoreOptions firestoreOptions = FirestoreOptions.getDefaultInstance().toBuilder()
      .setProjectId(projectId)
      .setCredentials(credentials)
      .build();
    Firestore db = firestoreOptions.getService();
    return db;
  }

  //-------------------------------------------------------------
  @Override
  public Object insert(Object model) {
    LOG.debug("Saving data for model:[{}] and type:[{}]",model.getClass().getSimpleName(),model.getClass());
    Map<String, Object> map = mapper.convertValue(model, Map.class);
    save(model.getClass().getSimpleName().toLowerCase(), map);
    return model;
  }

  private void save(String collectionName, Map<String, Object> docData) {
    try {
      ApiFuture<WriteResult> future = db.collection(collectionName).document().set(docData);
      LOG.debug("Updated time:[{}]", future.get().getUpdateTime());
    } catch (InterruptedException | ExecutionException exception) {
      throw new RuntimeException("Saving interrupted");
    }

  }

  @Override
  public T find(String key) {
    return null;
  }

  // todo: change here
  @Override
  public List<T> findAll(Object collection) {
    String collectionId = ((Class) collection).getSimpleName().toLowerCase();
    LOG.debug("Fetching all documents of model:[{}]",collectionId);
    List<QueryDocumentSnapshot> documentSnapshots = findAllCollections(collectionId);
    List<?> collect = documentSnapshots.stream().map(docSnap -> docSnap.toObject(collection.getClass())).collect(Collectors.toList());
    LOG.debug("Collection:{}",collect);
    return new ArrayList<>();
//    return (List<T>) findAllCollections(collectionId);
  }

  private List<QueryDocumentSnapshot> findAllCollections(String collectionId) {
    List<QueryDocumentSnapshot> documents;
    try {
      ApiFuture<QuerySnapshot> query = db.collection(collectionId).get();
      QuerySnapshot querySnapshot = query.get();
      documents = querySnapshot.getDocuments();
    }catch (InterruptedException | ExecutionException ie) {
      throw new RuntimeException("Fetching interrupted");
    }
    return documents;
  }

  @Override
  public T update(Object model, Object updateModel) {
    return null;
  }

  @Override
  public T delete(String key) {
    return null;
  }
}
