package com.bluespurs.starterkit.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.WebRequest;

import com.bluespurs.starterkit.objects.ProductData;
import com.bluespurs.starterkit.service.EmailServiceImpl;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@RestController
public class HelloWorldController {
	public static final String INTRO = "The Bluespurs Interview Starter Kit is running properly.";
	public static final Logger log = LoggerFactory.getLogger(HelloWorldController.class);
	public RestTemplate restTemplate = new RestTemplate();
	public EmailServiceImpl emailService = null;

	/**
	 * The index page returns a simple String message to indicate if everything
	 * is working properly. The method is mapped to "/" as a GET request.
	 */
	@RequestMapping("/")
	public String helloWorld() {
		log.info("Visiting index page");
		return INTRO;
	}

	// Method to get the cheapest product details ---- TASK 1

	@RequestMapping(value = "/product/search", method = RequestMethod.GET)
	public @ResponseBody String getCheapestProduct(@RequestParam("name") String name) {
		String WalmartException = "";
		String bestBuyException = "";
		List<ProductData> BestBuyProductsList = new ArrayList<>();
		List<ProductData> WalmartProductsList = new ArrayList<>();

		try {

			BestBuyProductsList = GetBestBuyDetails(name);
		} catch (Exception e) {
			bestBuyException = "Failed to reach Best Buy API With Message: \n\n" + e.getMessage();
		}

		try {
			WalmartProductsList = GetWalmartDetails(name);
		} catch (Exception e) {
			WalmartException = "Failed to reach Walmart API With Message: \n\n" + e.getMessage();
		}

		if (BestBuyProductsList.isEmpty() && WalmartProductsList.isEmpty()) {

			return "No result found";
		} else if (WalmartException != "" && bestBuyException != "") {
			return " Service Not Available OR Invalid request " + WalmartException + "\n\n" + bestBuyException;
		} else {

			Double bestBuyPrice = 0.00, walmartPrice = 0.00;
			ProductData productDetail = new ProductData();

			if (!BestBuyProductsList.isEmpty()) {

				bestBuyPrice = Double.parseDouble(BestBuyProductsList.get(0).getBestPrice());
			}

			if (!WalmartProductsList.isEmpty()) {

				walmartPrice = Double.parseDouble(WalmartProductsList.get(0).getBestPrice());
			}

			double Min = Math.min(bestBuyPrice, walmartPrice);
			productDetail = (Min == bestBuyPrice) ? BestBuyProductsList.get(0) : WalmartProductsList.get(0);

			Gson g = new Gson();

			return "200 OK\n\n" + g.toJson(productDetail.toString());
		}
	}

	private List<ProductData> GetWalmartDetails(String name) {
		List<ProductData> WalmartProductsList = new ArrayList<ProductData>();

		String walmartResult = restTemplate
				.getForObject("http://api.walmartlabs.com/v1/search?apiKey=rm25tyum3p9jm9x9x7zxshfa&query=" + name
						+ "&sort=price&ord=asc", String.class);

		JsonObject jsonWalmartMessyResult = new JsonParser().parse(walmartResult).getAsJsonObject();

		JsonElement WalmartProducts = jsonWalmartMessyResult.get("items");
		for (JsonElement element : WalmartProducts.getAsJsonArray()) {
			JsonObject obj = element.getAsJsonObject();
			ProductData searchResult = new ProductData();
			searchResult.setProductName(obj.get("name").getAsString());
			searchResult.setBestPrice(obj.get("salePrice").getAsString());
			searchResult.setCurrency("CAD");
			searchResult.setLocation("Walmart");

			WalmartProductsList.add(searchResult);

		}
		return WalmartProductsList;

	}

	private List<ProductData> GetBestBuyDetails(String name) {
		List<ProductData> BestBuyProductsList = new ArrayList<ProductData>();

		String bestBuyResult = restTemplate.getForObject(
				"http://api.bestbuy.com/v1/products(name=" + name
						+ "*)?show=name,salePrice&apiKey=pfe9fpy68yg28hvvma49sc89&sort=salePrice.asc&format=json",
				String.class);

		JsonObject jsonBestBuyMessyResult = new JsonParser().parse(bestBuyResult).getAsJsonObject();

		JsonElement bestBuyProducts = jsonBestBuyMessyResult.get("products");

		for (JsonElement element : bestBuyProducts.getAsJsonArray()) {
			JsonObject obj = element.getAsJsonObject();
			ProductData searchResult = new ProductData();
			searchResult.setProductName(obj.get("name").getAsString());
			searchResult.setBestPrice(obj.get("salePrice").getAsString());
			searchResult.setCurrency("CAD");
			searchResult.setLocation("BestBuy");

			BestBuyProductsList.add(searchResult);
		}
		return BestBuyProductsList;

	}

	// Sending Email ---- TASK 2

	@RequestMapping(value = "/product/alert", method = RequestMethod.POST)
	public @ResponseBody String SendEmail(WebRequest EmailRequestData) {

		String EmailMessage = "Icorrect Information";
		String productName = EmailRequestData.getParameter("productName");
		String email = EmailRequestData.getParameter("email");
		if (productName != "" && email != "" && !productName.isEmpty() && !email.isEmpty()) {
			emailService.sendEmail(email, "The price of the " + productName + " has dropped!", "The price of the "
					+ productName + "has dropped!' and  'The price of the ipad has dropped Get it quick!.");

			return "Sending email to " + email + " 'The price of the " + productName
					+ " has dropped!' and  'The price of the ipad has dropped from 150.00CAD to 148.00CAD! Get it quick!'";
		}

		else {
			return EmailMessage;
		}

	}
}
