# What is FireStore?

* Firestore is a NoSQL, document-oriented database.
* Unlike a SQL database, there are no tables or rows.
* Instead,we store data in documents, which are organized into collecitons.
* Each `document` contains a set of key-value pairs.
* __Firestore is optimized for storing large collections of small documents.__
* All documents must be stored in collections.
* Documents can contain `subcollections` and nested objects, both of which can include primitive fields like strings or complex objects like lists.
* Collections and documents are created implicitly in Firestore. we simple assign data to a document within a collection.

## What is Document?

* In Firestore, the __unit of storeage__ is the document.
* A document is a lightweight record that contains fields, which map to values.
* Each document is __identified by a name__.
* A document representing a user `alovelace` might look like this:

>__`alovelace`__
>`first : "Ada"`
>`last : "Lovelace"`
>`born : 1815`

* __Firestore supports a variety of data types for values: boolean, number, string, geo point, binary blob, and timestamp.__
* Complex, nested objects in a document are called __maps__.
* __We can also use arrays or nested objects, called maps, to structure data within a document.__
* For example, we can structure the user's name from the example above with a map, like this:

>__`alovelace`__
>`name :`
>&nbsp;`first : "Ada"`
>&nbsp;`last : "Lovelace"`
>`born : 1815`

* We may notice that documents look a lot like JSON. In fact they basically are.
* There are some differences, __for example, documents support extra data types and are limited in size to 1 MB.__
* But in general, we can treat documents as lightweight JSON records.

### What are Collections?

* Documents live in collections, which are simply containers for documents.
* For example, we could have a `users` collection to contain our various users, each represented by a document:

>__`users`__
>_`alovelace`_
>`first : "Ada"`
>`last : "Lovelace"`
>`born : 1815`
>_`aturing`_
>`first : "Alan"`
>`last : "Turing"`
>`born : 1912`

* Firestore is __schemaless__, so we have complete freedom over what fields we put in each document and what data types we store in those fields.
* Documents within the same collection can all contain different fields or store different types of data in those fields.
* __However, it's a good idea to use the same fields and data types across multiple documents, so that we can query the documents more easily.__
* A collection contains documents and nothing else.
* __It can not directly contain raw fields with values, and it can not contain other collections.__
* The names of documents within a collection are __unique__.
* We can provide __our own keys, such as user IDs,__ or we can let Firestore create random IDS for us automatically.
* __We do not need to `create` or `delete` collections.__
* After we create the first document in a collection, the collection exists.
* __If we delete all the documents in a collection, it no longer exists.__

### What are References?

* Every document in a Firestor is uniquely identified by it's location wihin the database.
* The previous example showed a document `alovelace` within the collection `users`.
* To refer to this location in our code, we can create a reference to it.

>`DocumentReference alovelaceDocumentRef = db.collection("users").document("alovelace");`

* We can also create references to `collections`:

>`CollectionReference usersCollectionRef = db.collection("users");`

* __For convenience, we can also create references by specifiying the path to a document or collection as a string, with path components spererated by slash(`/`).__

>`DocumentReference alovelaceDocumentRef = db.document("users/alovelace");`

### What is Hierarchical Data?

* To understand how hierarchical data structures work in Firestore, we consider an example chat app with messages and chat rooms.
* We can create a collection called `rooms` to store different chat rooms:

>__`rooms`__
>&nbsp;_`roomA`_
>&nbsp;`name : "my chat room"`
>&nbsp;_`roomB`_
`...`

* Now that we have chat rooms, we can decide how to store our messages.
* We might not want to store them in the chat
room's document.
* Documents in Firestore should be lightweigh, and a chat room could contain a large number of messages.
* However, we can create additional collections within our chat room's document, as subcollections.

### What are Subcollections?

* The best way to store messages in this scenario is by using subcollections.
* A subcollection is a collection associated with a specific document.
* We can create a subcollection called `messages` from every room document in our `rooms` collection:

>__`rooms`__
>&nbsp;__`roomA`__
>&nbsp;`name: "my chat room"`
>&nbsp;&nbsp;__`messages`__
>&nbsp;&nbsp;&nbsp;&nbsp;`message1`
>&nbsp;&nbsp;&nbsp;&nbsp;`from: "alex"`
>&nbsp;&nbsp;&nbsp;&nbsp;`msg: "Hello World!"`
>&nbsp;&nbsp;&nbsp;&nbsp;`message2`
>&nbsp;&nbsp;&nbsp;&nbsp;`from: "martin"`
>&nbsp;&nbsp;&nbsp;&nbsp;`msg: "Good morning!"`
>&nbsp;&nbsp;&nbsp;&nbsp;`...`
>&nbsp;__`roomB`__
>&nbsp;__`...`__

* In this example, we would create a reference to a message in the subcollection with following code:

>`DocumentReference messageRef = db.collection("rooms").document("roomA")`
>`.collection("messages").document("message1");`

* __We can notice the alternating pattern of collections and documents.__
* Our collections and documents must always follow this pattern.
* __We can not reference a collection in a collection or a document in a document.__
* __Subcollections allow us to structure data hierarchically, making data easier to access.__
* To get all messages in `roomA`, we can create a collection reference to the subcollection `messages` and interact with it like we would any other collection reference.
* Documents in subcollections can contain subcollections as well, allowing us to furthur nest data.
* __We can nest data up to 100 levels deep.__

### Deleting a document does not delete its subcollections

* __When we delete a document that has subcollections, those subcollections are not deleted.__
* For example, there may be a document located at `col1/doc/subcol1/subdoc`even though `col1/doc` no longer exists.
* __If we want to delete documents in subcollections when deleting a parent document, we must do so manually.__


## Code Level Implementation

## Add the server client library to our app (Gradle)

* Using gradle:

> `compile 'com.google.cloud:google-cloud-firestore:1.32.0'`

### Initialize Firestore

> `import com.google.cloud.firestore.Firestore;`
> `import com.google.cloud.firestore.FirestoreOptions;`
> `FirestoreOptions firestoreOptions = FirestoreOptions.getDefaultInstance().toBuilder().setProjectId(projectId).setCredentials(GoogleCredentials.getApplicationDefault()).build();`
> `Firestore db = firestoreOptions.getService();`

### Add Data

* Firestore stores data in Documents, which are stored in Collections.
* Firestore creates collections and documents implicitly the first time we add to the document.
* We do not need to explicitly create collections or documents.
__To Create a new collection__

>`DocumentReference docRef = db.collectioon("users").document("alovelace");`
>`// Add documnet with id "alovelace" using a hashmap`
>`Map<String,Object> data = new HashMap<>();`
>`data.put("first", "Ada");`
>`data.put("last","Lovelace");`
>`data.put("born",1815);`
>`// Asychronously write data`
>`ApiFuture<WriteResult> result = docRef.set(data);`
>`// result.get() blocks on response`
>`System.out.println("Update time: " + result.get().getUpdateTime());`

* Now we add another document to the `users` collection.
* Notice that this document includes a key-value pair (middle name) that does not appear in the first document.
* Documents in a collection can contain different sets of information.

>`DocumentReference docRef = db.collection("users").document("aturing");`
>`// Add document data with an additional field ("middle")`
>`Map<String, Object> data = new HashMap<>();`
>`data.put("first", "Alan");`
>`data.put("middle", "Mathison");`
>`data.put("last", "Turing");`
>`data.put("born", 1912);`
>`ApiFuture<WriteResult> result = docRef.set(data);`
>`System.out.println("Update time : " + result.get().getUpdateTime());`

### Read Data

* To quickly verify that we have added data to Firestore, we use the data viewer in the Firebase console.

>`// Asychronously retrieve all users`
>`ApiFuture<QuerySnapshot> query = db.collection("users").get();`
>`// query.get() blocks on response`
>`QuerySnapshot querySnapshot = query.get();`
>`List<QueryDocumentSnapshot> documents = querySnapshot.getDcuments();`
>`for (QueryDocumentSnapshot document : documents) {`
>`System.out.println("User: " + document.getId());`
>`System.out.println("First: " + document.getString("first"));`
>`If (document.contains("middle")) {`
>`System.out.println("Middle: " + document.getString("middle"));`
>`}`
>`System.out.println("Last: " + document.getString("last"));`
>`System.out.println("Born: " + document.getString("born"));`
>`}`
