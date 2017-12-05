package com.handsomelee.gotroute.Services;

import android.os.AsyncTask;
import android.util.Log;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.Console;

public class DatabaseConnect {
  
  MongoClientURI uri;
  MongoClient mongo;
  MongoDatabase database;
  MongoCollection<Document> collection;
  
  
  public DatabaseConnect(String host) {
    uri = new MongoClientURI(host);
    mongo = new MongoClient(uri);
    
  }
  
  public DatabaseConnect() {
    try {
      uri = new MongoClientURI("mongodb://bnuby:373511@cluster0-shard-00-00-c0x3w.mongodb.net:27017,cluster0-shard-00-01-c0x3w.mongodb.net:27017,cluster0-shard-00-02-c0x3w.mongodb.net:27017/test?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin");
      mongo = new MongoClient(uri);
      database = mongo.getDatabase("handsomelee");
      Log.v("Done", "done");
    } catch (Exception e){
      Log.e("Exception", e.getLocalizedMessage());
    }
    
  }
  
  public Boolean collectionFind(final String collectionName) {
    
    try {
      
      new AsyncTask<Void, Void, Void>() {
        @Override
        protected Void doInBackground(Void... voids) {
          collection = database.getCollection(collectionName);
          MongoCursor<Document> cursor = collection.find().iterator();
          while (cursor.hasNext()) {
            Log.v("collection", cursor.next().toJson());
          }
          Log.v("find", "End");
          return null;
        }
      }.execute();
      
      Log.v("Thread", "End");
      return true;
    } catch (Exception e) {
      return false;
    }
    
  }
}
