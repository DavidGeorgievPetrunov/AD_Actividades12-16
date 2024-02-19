package com.mycompany.ad_a15_davidgeorgievpetrunov;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnector {

    private MongoClient mongoClient;
    private MongoDatabase database;

    public MongoDBConnector() {
        this.mongoClient = MongoClients.create();
        this.database = mongoClient.getDatabase("reservesDB");
    }

    public MongoDatabase getDatabase() {
        return database;
    }

}
