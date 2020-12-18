package com.ujm.semweb.dao;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.opencsv.CSVReader;
import com.ujm.semweb.config.DumpData;
import com.ujm.semweb.model.BikeStation;
import com.ujm.semweb.model.City;
import com.ujm.semweb.model.Hospital;
import com.ujm.semweb.model.RailwayStation;
@Component
public class BikeStationDao {
	private final Logger LOG = Logger.getLogger(BikeStationDao.class.getName());
	// GraphDB 
    @Value("${GRAPH_REPO_QUERY}")
	private String GRAPH_REPO_QUERY ;
//	private String GRAPH_REPO_QUERY = "http://localhost:7200/repositories/PersonData";
    @Value("${GRAPH_REPO_UPDATE}")
    private String GRAPH_REPO_UPDATE ;
//    private String GRAPH_REPO_UPDATE  = "http://localhost:7200/repositories/PersonData/statements";
	public List<BikeStation> getAllStationByCityName(String cityName) {
		List<BikeStation> bikeStations=new ArrayList<>();
		String graphQuery="";
		if(cityName.toUpperCase().equals("LYON")) {
		 graphQuery="PREFIX a: <https://www.wikidata.org/wiki/Property:> "
				+ "PREFIX wd:<https://www.wikidata.org/wiki/> "
				+ "PREFIX schema:<http://www.w3.org/2000/01/rdf-schema#> "
				+ "PREFIX schemaOrg:<https://schema.org/> "
				+ "PREFIX dbo: <http://dbpedia.org/ontology/>"
				+ "PREFIX dbp: <http://dbpedia.org/property/>"
				+ ""
				+ "SELECT DISTINCT ?cityComment ?coordinate  ?city  ?name  ?bikestation ?address ?brand ?capacity{"
				+ "    ?city  a:P31 wd:Q484170 ; "
				+ "           schema:label \""+cityName.toUpperCase()+"\" ;"
				+ "           schema:comment ?cityComment ."
				+ "       ?bikestation  a:P31 dbo:Station ;"
				+ "            a:P131 ?city; "
				+ "             a:P1192 ?name;             "
				+ "            schemaOrg:address ?address; "
				+ "            a:P625 ?coordinate  ;"
				+ "            schemaOrg:brand ?brand; "
				+ "            dbp:storage ?capacity . "
				+ "}";
					 QueryExecution queryExecution = prepaerQueryExecution(graphQuery);
					 LOG.info("EXECUTING SPAREQL QUERY"); 
					 try {
					 for (ResultSet results = queryExecution.execSelect(); results.hasNext();) {
					      QuerySolution qs = results.next();
					      BikeStation bikeStation=new BikeStation();
					      bikeStation.uri=qs.get("?bikestation").toString();
					      bikeStation.label=qs.get("?name").toString();
					      bikeStation.cityUri=qs.get("?city").toString();
					      bikeStation.cityName=cityName;
					      bikeStation.brand=qs.get("?brand").toString();
					      bikeStation.address=qs.get("?address").toString();
					      bikeStation.recordedAt="N/A";
					      bikeStation.availability="N/A";;
					      bikeStation.capacity=qs.get("?capacity").toString();
					      bikeStation.coordination=qs.get("?coordinate").toString();
					      bikeStations.add(bikeStation);
					    }    
					    queryExecution.close();
					    return bikeStations;
					   } catch (Exception e) {
						   e.printStackTrace();
						   LOG.info(e.getMessage());
						   return null;
					   }
			}else {
				graphQuery="PREFIX a: <https://www.wikidata.org/wiki/Property:> "
						+ "		PREFIX wd:<https://www.wikidata.org/wiki/> "
						+ "		PREFIX schema:<http://www.w3.org/2000/01/rdf-schema#> "
						+ "		PREFIX schemaOrg:<https://schema.org/> "
						+ "		PREFIX dbo: <http://dbpedia.org/ontology/>"
						+ "		PREFIX dbp: <http://dbpedia.org/property/>"
						+ "		PREFIX custom_ontology:<http://www.semanticweb.org/dhayananth/ontologies/2020/11/untitled-ontology-7#>"
						+ "		SELECT DISTINCT "
						+ "		?cityComment  "
						+ "		?city ?label"
						+ "		?name  ?bikestation ?capacity ?brand ?recorderAt ?coordinate ?numOf{"
						+ "    ?city  a:P31 wd:Q484170 ; "
						+ "           schema:label \""+cityName.toUpperCase()+"\" ;"
						+ "           schema:comment ?cityComment ."
						+ "		?bikestation  a:P31 dbo:Station ;"
						+ "		            a:P131 ?city ; "
						+ "		             a:P1192 ?name ;  "
						+ "		            dbp:storage ?capacity;"
						+ "		            a:P625 ?coordinate ."
						+ "		 OPTIONAL{"
						+ "		       ?bikestation custom_ontology:hasAvailability ?availability."
						+ "		       ?availability a:P131 custom_ontology:Availability;"
						+ "		                     custom_ontology:numberOfAvailability ?numOf;"
						+ "		                   custom_ontology:recordedAt ?recorderAt."
						+ "		       }"
						+ " OPTIONAL{"
						+ "                ?bikestation schemaOrg:brand ?brand . "
						+ "            }"
						
						+ "		}";
				 QueryExecution queryExecution = prepaerQueryExecution(graphQuery);
				 LOG.info("EXECUTING SPAREQL QUERY"); 
				 try {
				 for (ResultSet results = queryExecution.execSelect(); results.hasNext();) {
				      QuerySolution qs = results.next();
				      BikeStation bikeStation=new BikeStation();
				      bikeStation.uri=qs.get("?bikestation").toString();
				      bikeStation.label=qs.get("?name").toString();
				      bikeStation.cityUri=qs.get("?city").toString();
				      bikeStation.cityName=cityName;
				      bikeStation.brand="VERT";
				      bikeStation.address="N/A";
				      bikeStation.recordedAt=qs.get("?recorderAt").toString();
				      bikeStation.availability=qs.get("?numOf").toString();
				      bikeStation.capacity=qs.get("?capacity").toString();
				      bikeStation.coordination=qs.get("?coordinate").toString();
				      bikeStations.add(bikeStation);
				    }    
				    queryExecution.close();
				    return bikeStations;
				   } catch (Exception e) {
					   e.printStackTrace();
					   LOG.info(e.getMessage());
					   return null;
				   }
			}
		
	}
	
	
	
	
	//Saving Real Time Bike Availability with Blank Node
	public void saveRdf(List<BikeStation> bikeStations) {
		String a="https://www.wikidata.org/wiki/Property:P31";
		String wd="https://www.wikidata.org/wiki/";
		String geo="http://www.w3.org/2003/01/geo/wgs84_pos#";
		String organisationID="https://www.wikidata.org/wiki/Property:P1901";
		String organisation="https://www.wikidata.org/wiki/Property:P2541";
		String property="https://www.wikidata.org/wiki/Property:";
		String coordinate="https://www.wikidata.org/wiki/Property:P625";
		String ex="http://www.example.org/";
		String schemaHospitalInstance="https://schema.org/Hospital";
		String schema="https://schema.org/";
		String date_ouverture="https://www.wikidata.org/wiki/Property:P580";
		String wgs84="https://www.wikidata.org/wiki/Property:P625";
		String connecting_service="https://www.wikidata.org/wiki/Property:P1192";
		String dbpedia_ontology="http://dbpedia.org/ontology/";
		String dbpedia_property="http://dbpedia.org/property/";
		String custom_ontology="http://www.semanticweb.org/dhayananth/ontologies/2020/11/untitled-ontology-7#";
		
		 Model model =ModelFactory.createDefaultModel();
		 String bikeStationGraph="INSERT DATA {";
		 int count=0;
		 LOG.info("PREPARING CITY RDF DATA");
		 try {   
		 for(BikeStation bikestation: bikeStations) {
		        String bikeStationQid=bikestation.uri;
//		        //SettingUp-InstanceOf
//		        bikeStationGraph+=" <"+bikeStationQid+"> "
//				+" <"+a+"> "
//				+" <"+model.createProperty(dbpedia_ontology+"Station").toString()+"> . ";
//		        //SettipUp IN-ADIMINSTRATION-TERIORITY-OF 
//		        bikeStationGraph+="<"+bikeStationQid+"> "
//				+" <"+property+"P131> "
//				+" <"+bikestation.cityUri+"> . ";
//				        
//		    	//SettingUp-connecting service
//		        	bikeStationGraph+="<"+bikeStationQid+"> "
//					+"<"+model.createProperty(connecting_service).toString()+"> "
//					+" \""+bikestation.label+"\"@en . ";
//		    	//SettingUp-address
//		        if(bikestation.address!=null) {
//		        	bikeStationGraph+="<"+bikeStationQid+"> "
//					+"<"+model.createProperty(schema+"address").toString()+"> "
//					+" \""+bikestation.address.toString()+"\"@en . ";
//		        }
//		 		//SettingUp-Coordinate
//		        	bikeStationGraph+="<"+bikeStationQid+"> "
//					+"<"+model.createProperty(property+"P625").toString()+"> "
//					+" \""+bikestation.coordination+"\" . ";
//		        //SettingUp-brand
//		        if(bikestation.brand!=null) {
//		        	bikeStationGraph+="<"+bikeStationQid+"> "
//					+"<"+model.createProperty(schema+"brand").toString()+"> "
//					+" \""+bikestation.brand.toString()+"\"@en . ";
//		        }
//		      //SettingUp-product_id
//			      //SettingUp-number of racks
//		        if(bikestation.capacity!=null) {
//		        	bikeStationGraph+="<"+bikeStationQid+"> "
//					+"<"+model.createProperty(dbpedia_property+"storage").toString()+"> "
//					+" \""+bikestation.capacity.toString()+"\"^^<http://www.w3.org/2001/XMLSchema#int> . ";
//		        }
		        //SETTING UP AVAILABILITY BLANK NODE
		        bikeStationGraph+="<"+bikeStationQid+"> "
					+"<"+model.createProperty(custom_ontology+"hasAvailability").toString()+"> "
					+" [ "
//						Setting up blank node type
						+ "<"+property+"P31> "
						+"<"+model.createProperty(custom_ontology+"Availability").toString()+"> ; "
	//					Setting up recorded time
						+"<"+model.createProperty(custom_ontology+"recordedAt").toString()+"> "
						+" \""+bikestation.recordedAt+"\"^^<http://www.w3.org/2001/XMLSchema#dateTime> ;"
//						Setting up available
						+"<"+model.createProperty(custom_ontology+"numberOfAvailability").toString()+"> "
						+" \""+bikestation.availability+"\"^^<http://www.w3.org/2001/XMLSchema#int> "
					+ "] . ";
				if(count%2==0) {
					bikeStationGraph+="}";
				  LOG.info(bikeStationGraph);
				  LOG.info("STORING RDF DATA TO DB AT >>>>>> "+count);
				  saveToGraphDb(bikeStationGraph);
				  LOG.info("SUCCESSFULLY STORED THE GRAPH DATA>>>>>>");
				  bikeStationGraph="INSERT DATA {";
				}
				count++;
		    }
		    bikeStationGraph+="}";
		    LOG.info(bikeStationGraph);
		    LOG.info("STORING RDF DATA TO DB>>>>>>");
		    saveToGraphDb(bikeStationGraph);
		    LOG.info("SUCCESSFULLY STORED THE GRAPH DATA>>>>>>");
		 }
		 catch(Exception e) {
			 e.printStackTrace();
		 }
	}
	private void saveToGraphDb(String insertStatement) {
	    UpdateRequest updateRequest = UpdateFactory.create(insertStatement);
	    UpdateProcessor updateProcessor = UpdateExecutionFactory
	        .createRemote(updateRequest, 
			GRAPH_REPO_UPDATE);
	    updateProcessor.execute();
	}
	private QueryExecution prepaerQueryExecution(String query) {
	    QueryExecution queryExecution = QueryExecutionFactory
	        .sparqlService(GRAPH_REPO_QUERY, query);
	    return queryExecution ;
	}
}
