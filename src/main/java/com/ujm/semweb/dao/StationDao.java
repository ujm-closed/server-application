package com.ujm.semweb.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.springframework.stereotype.Component;

import com.ujm.semweb.config.DumpData;
import com.ujm.semweb.model.City;
import com.ujm.semweb.model.RailwayStation;
@Component
public class StationDao {
	private final Logger LOG = Logger.getLogger(DumpData.class.getName());
	// GraphDB 
    private final String GRAPH_REPO_QUERY = 
      "http://localhost:7200/repositories/PersonData";
    private final String GRAPH_REPO_UPDATE = 
      "http://localhost:7200/repositories/PersonData/statements";
	public List<RailwayStation> getAllStationByCityName(String cityName) {
		List<RailwayStation> railwayStations=new ArrayList<>();
		 String graphQuery =
					" PREFIX a: <https://www.wikidata.org/wiki/Property:>"
					+ "PREFIX wd:<https://www.wikidata.org/wiki/>"
					+ "PREFIX schema:<http://www.w3.org/2000/01/rdf-schema#>"
					+ "SELECT  ?label ?city  ?station ?stationLabel ?stationCoordination {"
					+ "	     ?city  a:P31 wd:Q484170 ;"
					+ "      		schema:label ?label ;"
					+ "	      		schema:comment \""+cityName+"\"@en ."
					+ "    	 ?station  a:P31 wd:Q55488 ;"
					+ "                a:P625 ?stationCoordination;"
					+ "                a:P131 ?city;"
//					+ "                schema:comment ?stationComment;"
					+ "                schema:label ?stationLabel ."
					+ " }";
		 QueryExecution queryExecution = prepaerQueryExecution(graphQuery);
		 LOG.info("EXECUTING SPAREQL QUERY"); 
		 try {
		 for (ResultSet results = queryExecution.execSelect(); results.hasNext();) {
		      QuerySolution qs = results.next();
		      RailwayStation station=new RailwayStation();
		      station.stationUri=qs.get("?station").toString();
		      station.coordination=qs.get("?stationCoordination").toString();
		      station.cityUri=qs.get("?city").toString();
		      station.cityLabel=qs.get("?label").toString();
		      station.stationLabel=qs.get("?stationLabel").toString();
//		      station.comment=qs.get(" ?stationComment").toString();
		      station.instanceOf="Q484170";
		      railwayStations.add(station);
		    }    
		    queryExecution.close();
		    return railwayStations;
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
