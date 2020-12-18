package com.ujm.semweb.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import com.ujm.semweb.config.DumpData;
import com.ujm.semweb.model.City;

@Component
public class CityDao {
//	ResourceUtils.getFile("classpath:config/sample.txt")
	private final Logger LOG = Logger.getLogger(DumpData.class.getName());
	// GraphDB 
    private final String GRAPH_REPO_QUERY = 
      "http://localhost:7200/repositories/PersonData";
    private final String GRAPH_REPO_UPDATE = 
      "http://localhost:7200/repositories/PersonData/statements";
	public void searchCities(String cityName) {
		String queryString = "PREFIX bd: <http://www.bigdata.com/rdf#>\n" +
                "PREFIX wikibase: <http://wikiba.se/ontology#>\n" +
                "PREFIX wdt: <http://www.wikidata.org/prop/direct/>\n" +
                "PREFIX wd: <http://www.wikidata.org/entity/>\n" +
                "SELECT DISTINCT ?country ?countryLabel\n" +
                "WHERE\n" +
                "{\n" +
                "\t?country wdt:P31 wd:Q3624078 .\n" +
                "    ?country wdt:P1622 wd:Q13196750.\n" +
                "    ?country wdt:P30 wd:Q15\n" +
                "\tFILTER NOT EXISTS {?country wdt:P31 wd:Q3024240}\n" +
                "\tSERVICE wikibase:label { bd:serviceParam wikibase:language \"en\" }\n" +
                "}\n" +
                "ORDER BY ?countryLabel";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.sparqlService("https://query.wikidata.org/sparql?", queryString);
        try {
            ResultSet results = qexec.execSelect();
            ResultSetFormatter.out(System.out, results, query);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            qexec.close();
        }
	}
	
	public List<City> getAllCity(){
	 List<City>	cities=new ArrayList<City>();
	 String graphQuery =
		"		PREFIX a: <https://www.wikidata.org/wiki/Property:>"
        + "		PREFIX wd:<https://www.wikidata.org/wiki/>"
        + "		PREFIX schema:<http://www.w3.org/2000/01/rdf-schema#>"
        + "		SELECT  ?label ?city ?coordination {"
        + "				?city a:P31 wd:Q484170 ;"
        + "				a:P625 ?coordination	;"
        + "			   	schema:label ?label . "
        + "		}";
	 QueryExecution queryExecution =prepaerQueryExecution(graphQuery);
	 LOG.info("EXECUTING SPAREQL QUERY"); 
	 try {
	 for (ResultSet results = queryExecution.execSelect(); results.hasNext();) {
	      QuerySolution qs = results.next();
	      City city=new City();
	      city.uri=qs.get("?city").toString();
	      city.label=qs.get("?label").toString();
	      city.coordination=qs.get("?coordination").toString();
	      cities.add(city);
	    }    
	    queryExecution.close();
	    return cities;
	   } catch (Exception e) {
		   e.printStackTrace();
		   LOG.info(e.getMessage());
		   return null;
	   }
	   
	}
	public List<City> searchEntityByCityName(String cityName){
		 List<City>	cities=new ArrayList<City>();
		 String graphQuery =
			"		PREFIX a: <https://www.wikidata.org/wiki/Property:>"
			+ "PREFIX wd:<https://www.wikidata.org/wiki/>"
			+ "PREFIX schema:<http://www.w3.org/2000/01/rdf-schema#>"
			+ "SELECT  ?label ?city  ?station ?stationLabel  {"
			+ "	     ?city  a:P31 wd:Q484170 ;"
			+ "      		schema:label ?label ;"
			+ "	      		schema:comment \""+cityName+"\"@en ."
			+ "    	 ?station  a:P31 wd:Q55488 ;"
			+ "                a:P131 ?city;"
			+ "                schema:label ?stationLabel ."
			+ " }";
		 QueryExecution queryExecution =prepaerQueryExecution(graphQuery);
		 LOG.info("EXECUTING SPAREQL QUERY"); 
		 try {
		 for (ResultSet results = queryExecution.execSelect(); results.hasNext();) {
		      QuerySolution qs = results.next();
		      City city=new City();
		      city.uri=qs.get("?city").toString();
		      city.label=qs.get("?label").toString();
		      city.coordination=qs.get("?coordination").toString();
		      cities.add(city);
		    }    
		    queryExecution.close();
		    return cities;
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
