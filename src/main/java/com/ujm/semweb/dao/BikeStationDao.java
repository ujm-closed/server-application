package com.ujm.semweb.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
//    = "http://localhost:7200/repositories/PersonData";
    @Value("${GRAPH_REPO_UPDATE}")
    private String GRAPH_REPO_UPDATE ;
//    = "http://localhost:7200/repositories/PersonData/statements";
	public List<BikeStation> getAllStationByCityName(String cityName) {
		List<BikeStation> bikeStations=new ArrayList<>();
		String graphQuery="PREFIX a: <https://www.wikidata.org/wiki/Property:> "
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
	private QueryExecution prepaerQueryExecution(String query) {
	    QueryExecution queryExecution = QueryExecutionFactory
	        .sparqlService(GRAPH_REPO_QUERY, query);
	    return queryExecution ;
	}
}
