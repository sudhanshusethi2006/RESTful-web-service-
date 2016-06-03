package com.bluespurs.starterkit.objects;

import org.springframework.stereotype.Service;


/**
 * Simple object to store the latest best (cheapest) search result
 */
@Service
public class ProductData {

    private String productName;

    private String bestPrice;

    private String currency;

    private String location;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBestPrice() {
        return bestPrice;
    }

    public void setBestPrice(String bestPrice) {
        this.bestPrice = bestPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

	@Override
	public String toString() {
		
		return "{"+"productName: " + productName + ", bestPrice: " + bestPrice + ", currency: "
				+ currency + ", location: " + location+"}"  ;
		
	
	}
    
    

}
