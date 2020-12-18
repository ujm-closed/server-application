package com.ujm.semweb.service;

import java.io.File;
import java.util.Collections;

import org.apache.tomcat.util.json.JSONParser;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
//@Service
public class RestService {
	private RestTemplate restTemplate;
	public RestService() {
		   restTemplate = new RestTemplate();
	}
	
	public void test()
	{
		final String uri = "https://samples.openweathermap.org/data/2.5/weather?lat=35&lon=139&appid=b6907d289e10d714a6e88b30761fae22";
		 restTemplate.getForObject(uri,String.class);
	     ResponseEntity<Object> responseEntity = restTemplate.getForEntity(uri, Object.class);
	     Object objects = responseEntity.getBody();
	     System.out.println(objects);
	     MediaType contentType = responseEntity.getHeaders().getContentType();
	}
	public void getBikeData(){
		try {
			final String uri = "https://data.rennesmetropole.fr/api/records/1.0/search/?dataset=etat-des-stations-le-velo-star-en-temps-reel";
			restTemplate.getForObject(uri,String.class);
		    ResponseEntity<Object> responseEntity = restTemplate.getForEntity(uri, Object.class);
		    Object objects = responseEntity.getBody();
//		    JSONArray result = CDL.toJsonO(responseEntity.getBody().toString());
//		    JSONObject jsonObject = new JSONObject(responseEntity.getBody().toString());
		    Gson gson = new Gson();
		    com.ujm.semweb.model.Record obj2 = gson.fromJson(responseEntity.getBody().toString(), com.ujm.semweb.model.Record.class); 
		    System.out.print("asd");
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	public void initPackage(String param)
	{
		final String uri = "https://192.168.1.12/api/initiate_packaging?params="+param;
	    
		 restTemplate.getForObject(uri,String.class);
	     ResponseEntity<Object> responseEntity = restTemplate.getForEntity(uri, Object.class);
	     System.out.println( responseEntity);
	}
	
//	public void uploadFileRDF(File file) {
//		
//		  library(httr)
//  	    post.endpoint <- "http://localhost:7200//rest/data/import/upload/disease_diagnosis_dev/url"
//  	    update.body <- '{
//  	      "type":"url",
//  	      "format":"text/turtle",
//  	      "context": "http://purl.bioontology.org/ontology/ICD9CM/",
//  	      "data": "http://data.bioontology.org/ontologies/ICD9CM/submissions/17/download?apikey=9cf735c3-a44a-404f-8b2f-c49d48b2b8b2"
//  	    }'
//
//  	    post.result <- POST(post.endpoint,
//  	                        body = update.body,
//  	                        content_type("application/json"));
//  	    
//  	  final String uri = "http://localhost:7200//rest/data/import/upload/PersonData/url";
//		 
//	     ResponseEntity<Object> responseEntity = restTemplate.postForEntity(uri,, Object.class);
//	     Object objects = responseEntity.getBody();
//	     System.out.println(objects);
//	     MediaType contentType = responseEntity.getHeaders().getContentType();
//	     return objects;
//	}
//	public Object pottingWasteInform(WasteManagement obj)
//	{
//		final String uri = "https://192.168.1.12/api/pottingWasteInform?status=";
//		 restTemplate.getForObject(uri,String.class);
//	     ResponseEntity<Object> responseEntity = restTemplate.postForEntity(uri,obj, Object.class);
//	     Object objects = responseEntity.getBody();
//	     System.out.println(objects);
//	     MediaType contentType = responseEntity.getHeaders().getContentType();
//	     return objects;
//	}
//	public Object initiatePotting(Input_Prop obj)
//	{
//		final String uri = "https://localhost:8082/api/start_potting";
//		 restTemplate.getForObject(uri,String.class);
//	     ResponseEntity<Object> responseEntity = restTemplate.postForEntity(uri,obj, Object.class);
//	     Object objects = responseEntity.getBody();
//	     System.out.println(objects);
//	     MediaType contentType = responseEntity.getHeaders().getContentType();
//	     return objects;
//	}
	
	//this code can be found in restService class
	public Object getPowerAllocation()
	{
		final String uri = "https://192.168.1.12/api/opc/powerManagement?workshop=potting";
		 restTemplate.getForObject(uri,String.class);
	     ResponseEntity<Object> responseEntity = restTemplate.getForEntity(uri, Object.class);
	     Object objects = responseEntity.getBody();
	     MediaType contentType = responseEntity.getHeaders().getContentType();
	     return objects;
	}
	
//	    public Object getPlainJSON(String url) {
//	    	 HttpHeaders headers = new HttpHeaders();
//	        return this.restTemplate.getForObject(url,  Object.class);
//	    }
//	    
//	    public Object[] getPostsPlainJSONList(String url, Input_Prop obj) {
//	    	 HttpHeaders headers = new HttpHeaders();
//	    	 
//	        return this.restTemplate.postForObject(url, obj, Object[].class);
//	    }
	

}
