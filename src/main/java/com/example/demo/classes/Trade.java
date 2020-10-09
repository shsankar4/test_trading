package com.example.demo.classes;

import java.util.Date;


public class Trade {
	public static enum status{
		CREATED,EXECUTED
	}
	private Date dateCreated;
	private String tradeID;
	private String stockTicker;
	private int quantity;
	private double price;
	private status status;
	public Trade( String stockTicker, int quantity, double price) {
		this.dateCreated = new Date();
		this.stockTicker = stockTicker;
		this.quantity = quantity;
		this.price = price;
		this.status = status.CREATED;
	}
	
	public status getStatus() {
		return status;
	}

	public void setStatus(status status) {
		this.status = status;
	}

	public Date getDate() {
		return dateCreated;
	}
	public void setDate(Date date) {
		this.dateCreated = date;
	}
	public String getStockTicker() {
		return stockTicker;
	}
	public void setStockTicker(String stockTicker) {
		this.stockTicker = stockTicker;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
}
