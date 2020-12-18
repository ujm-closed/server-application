package com.ujm.semweb.dao;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFWriter;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;
import org.apache.jena.vocabulary.RDFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import com.opencsv.CSVReader;

public class GraphDBTest {

	private static Logger logger = LoggerFactory.getLogger(GraphDBTest.class);
	  // Why This Failure marker
	  private static final Marker WTF_MARKER = MarkerFactory.getMarker("WTF");
	  
	  
	  // GraphDB 
	  private static final String PERSONDATA_REPO_QUERY = 
	      "http://localhost:7200/repositories/PersonData";
	  
	  private static final String PERSONDATA_REPO_UPDATE = 
	      "http://localhost:7200/repositories/PersonData/statements";


	  private static String strInsert;
	  private static String strQuery;
	  
	  static {
	    strInsert = 
	        "INSERT DATA {"
	         + "<http://dbpedia.org/resource/Grace_Hopper> "
	         + "<http://dbpedia.org/ontology/birthDate>"
	         + " \"1906-12-09\"^^<http://www.w3.org/2001/XMLSchema#date> ."
	         + "<http://dbpedia.org/resource/Grace_Hopper> "
	         + "<http://dbpedia.org/ontology/birthPlace> "
	         + "<http://dbpedia.org/resource/New_York_City> ."
	         + "<http://dbpedia.org/resource/Grace_Hopper> "
	         + "<http://dbpedia.org/ontology/deathDate>"
	         + " \"1992-01-01\"^^<http://www.w3.org/2001/XMLSchema#date> ."
	         + "<http://dbpedia.org/resource/Grace_Hopper> "
	         + "<http://dbpedia.org/ontology/deathPlace> "
	         + "<http://dbpedia.org/resource/Arlington_County,_Virginia> ."
	         + "<http://dbpedia.org/resource/Grace_Hopper> "
	         + "<http://purl.org/dc/terms/description>"
	         + " \"American computer scientist and United States Navy officer.\" ."
	         + "<http://dbpedia.org/resource/Grace_Hopper> "
	         + "<http://www.w3.org/1999/02/22-rdf-syntax-ns#type> "
	         + "<http://dbpedia.org/ontology/Person> ."
	         + "<http://dbpedia.org/resource/Grace_Hopper> "
	         + "<http://xmlns.com/foaf/0.1/gender> \"female\" ."
	         + "<http://dbpedia.org/resource/Grace_Hopper> "
	         + "<http://xmlns.com/foaf/0.1/givenName> \"Grace\" ."
	         + "<http://dbpedia.org/resource/Grace_Hopper> "
	         + "<http://xmlns.com/foaf/0.1/name> \"Grace Hopper\" ."
	         + "<http://dbpedia.org/resource/Grace_Hopper>"
	         + " <http://xmlns.com/foaf/0.1/surname> \"Hopper\" ."        
	         + "}";
	    
	    strQuery = 
	        "SELECT ?name WHERE {?s <http://xmlns.com/foaf/0.1/name> ?name .}";
	  }   
	  
	  private static void insert() {
	    UpdateRequest updateRequest = UpdateFactory.create(strInsert);
	    UpdateProcessor updateProcessor = UpdateExecutionFactory
	        .createRemote(updateRequest, 
	        PERSONDATA_REPO_UPDATE);
	    updateProcessor.execute();
	  }
	  
	  public void saveToTrippleStore() {
		  String a="https://www.wikidata.org/wiki/Property:P31";
			String wd="https://www.wikidata.org/wiki/";
//			String datasetURL = "http://localhost:3030/test";
//			String sparqlEndpoint = datasetURL + "/sparql";
//			String sparqlUpdate = datasetURL + "/update";
//			String graphStore = datasetURL + "/data";
//			RDFConnection conneg = RDFConnectionFactory.connect(sparqlEndpoint,sparqlUpdate,graphStore);
//			conneg.load(model); 
//			conneg.update("INSERT DATA { <test> a <TestClass> }"); 
		  Model model =ModelFactory.createDefaultModel();
		  Resource station
		  = model.createResource("http://dbpedia.org/resource/New_York_City")
		  .addProperty(model.createProperty(a),model.createProperty(wd+"Q55488"));
		  
		  UpdateRequest updateRequest =UpdateFactory.create();
		  RDFConnection conneg = RDFConnectionFactory.connect(PERSONDATA_REPO_UPDATE);
			conneg.load(model); 
			conneg.update("INSERT DATA { <test> a <TestClass> }"); 
		}
	  
	  private static void query() {
	    QueryExecution queryExecution = QueryExecutionFactory
	        .sparqlService(PERSONDATA_REPO_QUERY, strQuery);
	    for (ResultSet results = queryExecution.execSelect(); results.hasNext();) {
	      QuerySolution qs = results.next();
	      String strName = qs.get("?name").toString();
	      logger.trace("name = " + strName);
	    }    
	    queryExecution.close();
	  }

	  public static void dumpCities() {
			String a="https://www.wikidata.org/wiki/Property:P31";
			String wd="https://www.wikidata.org/wiki/";
			String fileName = "src/main/resources/data/cities.csv";
			String geo="http://www.w3.org/2003/01/geo/wgs84_pos#";
			String administrativeTerorityOf="https://www.wikidata.org/wiki/Property:P131";
			String property="https://www.wikidata.org/wiki/Property:";
			String coordinate="https://www.wikidata.org/wiki/Property:P625";
			 File cityFile=new File(fileName);
			 Model model =ModelFactory.createDefaultModel();
			 try (CSVReader csvReader = new CSVReader(new FileReader(cityFile))) {
			    String[] values = null;
			    int count=0;
			    while ((values = csvReader.readNext()) != null) {
			    	if(count==0) {
			    		count++;
			    		continue;
			    	}
			      
//			    	System.out.println(values[3].split(" ")[0].replace("Point(",""));
//			    	System.out.println(values[3].split(" ")[1].replace(")",""));
			          
			        Resource city
					  = model.createResource(values[0])
//					         .addProperty(model.createProperty(a), wd+"Q484170")
							 .addProperty(model.createProperty(a), model.createProperty(wd+"Q484170"))
							 .addProperty(model.createProperty(property+"P17"), model.createProperty(wd+"Q142"))
					         .addProperty(RDFS.label,model.createLiteral(values[1], "en"))
					         
					         .addProperty(RDFS.comment,model.createLiteral(values[1], "en"))
					         .addProperty(model.createProperty(property+"P625"),model.createLiteral(values[3], "en"))
//					         .addProperty(model.createProperty(geo+"long"),model.createTypedLiteral(Double.valueOf(values[3].split(" ")[0].replace("Point(",""))))
//					         .addProperty(model.createProperty(geo+"lat"),model.createTypedLiteral(Double.valueOf(values[3].split(" ")[1].replace("Point(",""))))
					         ; 
			        String s=  city.toString();
			        model.listObjects().toList().toString();
			        
			    }
			   
			 
			 }
			 catch(Exception ex) {
				 ex.printStackTrace();
			 }
	  }
	  
	  
	  public static void main(String[] args) {
	    try {
//	      insert(); 
//	      query();
//	    	saveToTrippleStore();
	    } catch (Throwable t) {
	      logger.error(WTF_MARKER, t.getMessage(), t);
	    }   
	  }
}
