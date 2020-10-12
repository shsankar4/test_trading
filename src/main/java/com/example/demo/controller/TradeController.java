package com.example.demo.controller;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.validation.Valid;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.classes.Trade;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;


@RestController
@RequestMapping("/trade")
@CrossOrigin
public class TradeController {
	public static MongoClientURI uri = new MongoClientURI("mongodb+srv://group4:admin@cluster0.1ikro.mongodb.net/tradedb?retryWrites=true&w=majority");
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String createTrade(@RequestBody Trade trade) {
		Stock stock = null;
		try {
			stock = YahooFinance.get(trade.getStockTicker());
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(stock == null) {
			return "Enter a valid ticker name";
		}
		else {
			MongoClient myMongo = new MongoClient(uri);
			MongoDatabase database = myMongo.getDatabase("tradedb");
			
			Document doc = new Document("date", trade.getDate()).append("ticker", trade.getStockTicker()).append("quantity",trade.getQuantity()).append("price", trade.getPrice()).append("status",trade.getStatus().toString());
			MongoCollection<Document> mycollection = database.getCollection("trade_details");
			mycollection.insertOne(doc);
			myMongo.close(); 
			return doc.getObjectId("_id").toString();
		}
	}
	@RequestMapping(value = "/read", method = RequestMethod.GET)
	public static String readTrade() {
		System.out.println("Connecting.....");
		MongoClient myMongo = new MongoClient(uri);
		MongoDatabase database = myMongo.getDatabase("tradedb");
		System.out.println("Connected");
		
		MongoCollection<Document> mycollection = database.getCollection("trade_details");
		FindIterable<Document> iterDoc = mycollection.find();
		Iterator<Document> it = iterDoc.iterator();
		String s = "";
		while(it.hasNext()) {
			s+=it.next().toString()+ "\n";
		}
		myMongo.close();
		return s;
	}
	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	private static String updateDocument(@RequestBody Map<String,String> request) {
		String id = request.get("id");
		ObjectId objId = new ObjectId(id);
		MongoClient myMongo = new MongoClient(uri);
		MongoDatabase database = myMongo.getDatabase("tradedb");
		MongoCollection<Document> mycollection = database.getCollection("trade_details");
	
		Document doc = new Document("status", Trade.status.EXECUTED.toString() );		
		mycollection.updateOne(Filters.eq("_id", objId), new Document("$set", doc));
		
		Document retriveTicker = mycollection.find(Filters.eq("_id",objId)).first();
		String ticker = retriveTicker.getString("ticker");
		int quantity = retriveTicker.getInteger("quantity");
		double price = retriveTicker.getDouble("price");	
		System.out.println("IN TRADE CONTROLLER FUNC ***** "+ ticker +"***");
		myMongo.close();
		
		//PortfolioController.update(ticker, quantity, price);
		return "Trade Executed";
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	private static String deleteTrade(@RequestBody Map<String,String> request) {
		String id = request.get("id");
		ObjectId objId = new ObjectId(id);
		MongoClient myMongo = new MongoClient(uri);
		MongoDatabase database = myMongo.getDatabase("tradedb");
		MongoCollection<Document> mycollection = database.getCollection("trade_details");
		DeleteResult deleteResult = mycollection.deleteOne(Filters.eq("_id",objId));
		System.out.println(deleteResult.getDeletedCount());
		myMongo.close();
		return "Deleted";
	}
}
