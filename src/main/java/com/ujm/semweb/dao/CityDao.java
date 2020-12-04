package com.ujm.semweb.dao;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.springframework.stereotype.Component;

@Component
public class CityDao {
	
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
		QueryExecution qexec = QueryExecutionFactory.sparqlService("query.wikidata.org/sparql", queryString);
        try {
            ResultSet results = qexec.execSelect();
            ResultSetFormatter.out(System.out, results, query);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            qexec.close();
        }
	}
}
