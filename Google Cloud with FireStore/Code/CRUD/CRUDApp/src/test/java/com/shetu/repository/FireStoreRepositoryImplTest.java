package com.shetu.repository;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.shetu.entity.User;
import com.shetu.respository.FireStoreRepositoryImpl;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;

@MicronautTest
public class FireStoreRepositoryImplTest {
  private static final Logger LOG = LoggerFactory.getLogger(FireStoreRepositoryImplTest.class);
  private GoogleCredentials googleCredentials;
  private Firestore db;
  @Inject
  private FireStoreRepositoryImpl fireStoreRepository;
  // -----------------------------------------------------------------------------------
  // Configuration
  @BeforeEach
  void setup() throws IOException {
    String projectId = "river-pointer-308317";
    String udProjectId = "rakizo-dev";
    this.googleCredentials = getCredentials();
    this.db = getFirestoreDB(googleCredentials, udProjectId);
  }
  private GoogleCredentials getCredentials() throws IOException {
    String jsonPath = "/home/shetu/Shetu's folder/Official/UD/RK/rakizo-dev-f59197306042.json";
    return GoogleCredentials.fromStream(new FileInputStream(jsonPath))
    .createScoped(Collections.singletonList("https://www.googleapis.com/auth/cloud-platform"));
  }
  private Firestore getFirestoreDB(GoogleCredentials credentials, String projectId) {
    FirestoreOptions firestoreOptions = FirestoreOptions.getDefaultInstance().toBuilder()
    .setProjectId(projectId)
    .setCredentials(credentials)
    .build();
    Firestore db = firestoreOptions.getService();
    return db;
  }
  @AfterEach
  void cleanup() {
    this.db = null;
    this.googleCredentials = null;
  }
  //--------------------------------------------------------------------------------------------------------------

  @Test
  void insert(){
    User user = new User("Shahariar","27");
    user = (User) fireStoreRepository.insert(user);
    LOG.debug("Saved user:{}",user);
  }
  private User getUser(String name, String age){
    return new User(name, age);
  }
}
