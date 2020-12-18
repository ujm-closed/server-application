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

import com.ujm.semweb.model.Hospital;
import com.ujm.semweb.model.RailwayStation;
@Component
public class HospitalDao {
	private final Logger LOG = Logger.getLogger(HospitalDao.class.getName());
	// GraphDB 
    @Value("${GRAPH_REPO_QUERY}")
	private String GRAPH_REPO_QUERY ;
    @Value("${GRAPH_REPO_UPDATE}")
    private String GRAPH_REPO_UPDATE ;
	public List<Hospital> getAllHospitalByCityName(String cityName) {
		List<Hospital> hospitals=new ArrayList<>();
		String graphQuery="PREFIX a: <https://www.wikidata.org/wiki/Property:> "
				+ "		PREFIX wd:<https://www.wikidata.org/wiki/> "
				+ "		PREFIX schema:<http://www.w3.org/2000/01/rdf-schema#> "
				+ "		PREFIX schemaOrg:<https://schema.org/> "
				+ "		SELECT DISTINCT ?cityComment ?hospitalComment ?city  ?hospital "
				+ "?hospitalLabel ?hospitalAddress  ?medicalSpecialty ?telephone ?coordinate {"
				+" 			?city  a:P31 wd:Q484170 ; "
				+ "            		schema:label \""+cityName.toUpperCase()+"\";"
				+ "            		schema:comment ?cityComment ."
				+ "			?hospital  a:P31 schemaOrg:Hospital ; "
				+ "                    schemaOrg:City ?city; "
				+ "			           schema:comment ?hospitalComment; "
				+ "			           schemaOrg:address ?hospitalAddress; "
				+ "			           	a:P625 ?coordinate ; "
				+ "			           schemaOrg:medicalSpecialty ?medicalSpecialty; "
				+ "			           schemaOrg:telephone ?telephone; "
				+ "				       schema:label ?hospitalLabel . "
				+ "				}";
		 QueryExecution queryExecution = prepaerQueryExecution(graphQuery);
		 LOG.info("EXECUTING SPAREQL QUERY"); 
		 try {
		 for (ResultSet results = queryExecution.execSelect(); results.hasNext();) {
		      QuerySolution qs = results.next();
		      Hospital hospital=new Hospital();
		      hospital.uri=qs.get("?hospital").toString();
		      hospital.label=qs.get("?hospitalLabel").toString();
		      hospital.cityUri=qs.get("?city").toString();
		      hospital.cityName=cityName;
		      hospital.medicalSpecialty=qs.get("?medicalSpecialty").toString();
		      hospital.address=qs.get("?hospitalAddress").toString();
		      hospital.telephone=qs.get("?telephone").toString();
		      hospital.coordination=qs.get("?coordinate").toString();
		      hospitals.add(hospital);
		    }    
		    queryExecution.close();
		    return hospitals;
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
/**
 * 
PREFIX a: <https://www.wikidata.org/wiki/Property:> 
				PREFIX wd:<https://www.wikidata.org/wiki/> 
				PREFIX schema:<http://www.w3.org/2000/01/rdf-schema#> 
				PREFIX schemaOrg:<https://schema.org/> 
			SELECT DISTINCT ?comment ?stationComment ?city  ?hospital ?stationLabel   {
				        ?city  a:P31 wd:Q484170 ; 
				           schema:label "Saint-Etienne"@en;
				            schema:comment ?comment .
				        ?hospital  a:P31 schemaOrg:Hospital ; 
                                schemaOrg:City ?city; 
			                   schema:comment ?stationComment; 
				               schema:label ?stationLabel . 
				}

 */
}
