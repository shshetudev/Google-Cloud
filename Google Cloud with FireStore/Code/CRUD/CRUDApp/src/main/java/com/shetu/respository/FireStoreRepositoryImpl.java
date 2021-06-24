package com.shetu.respository;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.WriteResult;

import javax.inject.Inject;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FireStoreRepositoryImpl<T> implements AbstractRepository{
  private Firestore db = getFirestoreDB(getCredentials(),"rakizo-dev");

  //-------------------------------------------------------------
  // Configuration
  private GoogleCredentials getCredentials() {
    String jsonPath = "/home/shetu/Shetu's folder/Official/UD/RK/rakizo-dev-f59197306042.json";
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
  public T insert(Object model) {
    return null;
  }
  private T save(String collectionName, Map<String,Object> docData){
    ApiFuture<WriteResult> future = db.collection("cities").document("Dhaka").set(docData);
    System.out.println("Update time : " + future.get().getUpdateTime());
  }

  @Override
  public T find(String key) {
    return null;
  }

  @Override
  public List<T> findAll() {
    return null;
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
