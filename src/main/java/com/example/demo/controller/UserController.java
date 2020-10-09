package com.example.demo.controller;

import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import org.bson.Document;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.demo.classes.Users;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

@RestController
@CrossOrigin
@RequestMapping("/")
public class UserController {
	
	MongoClientURI uri = new MongoClientURI("mongodb+srv://group4:admin@cluster0.1ikro.mongodb.net/tradedb?retryWrites=true&w=majority");
	
	Users users;
	
	@RequestMapping(value="signup/", method=RequestMethod.POST)
	public String storeDetails() {
		users = new Users("abc@gmail.com", "abc");
		
		MongoClient myMongo = new MongoClient(uri);
		MongoDatabase database = myMongo.getDatabase("tradedb");
		Document newUser = new Document("email", users.getEmailID()).append("password", users.getPassword());
		MongoCollection<Document> mycollection = database.getCollection("user_details");
		mycollection.insertOne(newUser);
		myMongo.close(); 
		
		return "Signup success";
	}
	
	@RequestMapping(value="login/", method=RequestMethod.GET)
	public String verifyCredentials(@RequestBody Map<String,String> request) {
		Scanner sc = new Scanner(System.in);
		String email = request.get("email");
		String pw = request.get("pw");
		
		MongoClient myMongo = new MongoClient(uri);
		MongoDatabase database = myMongo.getDatabase("tradedb");
		MongoCollection<Document> mycollection = database.getCollection("user_details");
		
		Document retriveUserMail = mycollection.find(Filters.eq("email",email)).first();
		String actualPW = retriveUserMail.getString("password");
		
		myMongo.close();
		
		if(pw.equals(actualPW))
			return "Login Success";
		else
			return "Login failed";
	}
}
