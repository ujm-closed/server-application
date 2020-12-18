package com.ujm.semweb.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import com.ujm.semweb.config.DumpData;
import com.ujm.semweb.model.BikeStation;
import com.ujm.semweb.model.City;
import com.ujm.semweb.model.RailwayStation;
@Component
public class StationDao {
	private final Logger LOG = Logger.getLogger(StationDao.class.getName());
	// GraphDB 
    @Value("${GRAPH_REPO_QUERY}")
	private String GRAPH_REPO_QUERY ;
//    private String GRAPH_REPO_QUERY= "http://localhost:7200/repositories/PersonData";
    @Value("${GRAPH_REPO_UPDATE}")
    private String GRAPH_REPO_UPDATE ;
//    private String GRAPH_REPO_UPDATE = "http://localhost:7200/repositories/PersonData/statements";
	public List<RailwayStation> getAllStationByCityName(String cityName) {
		List<RailwayStation> railwayStations=new ArrayList<>();
		String graphQuery=""
				+ "PREFIX a: <https://www.wikidata.org/wiki/Property:> "
				+ "PREFIX wd:<https://www.wikidata.org/wiki/> "
				+ "PREFIX schema:<http://www.w3.org/2000/01/rdf-schema#> "
				+ "PREFIX schemaOrg:<https://schema.org/> "
				+ "SELECT DISTINCT ?comment ?stationComment ?city  ?station ?stationLabel ?stationCoordination ?branchCode {"
				+ "        ?city  a:P31 wd:Q484170 ; "
				+ "            schema:label \""+cityName.toUpperCase()+"\";"
				+ "            schema:comment ?comment ."
				+ "        ?station  a:P31 schemaOrg:TrainStation ; "
				+ "                a:P625 ?stationCoordination; "
				+ "                schemaOrg:branchCode  ?branchCode; "
				+ "                a:P131  ?city; "
				+ "                schema:comment ?stationComment; "
				+ "                schema:label ?stationLabel . "
				+ "}";
		 QueryExecution queryExecution = prepaerQueryExecution(graphQuery);
		 LOG.info("EXECUTING SPAREQL QUERY"); 
		 try {
		 for (ResultSet results = queryExecution.execSelect(); results.hasNext();) {
		      QuerySolution qs = results.next();
		      RailwayStation station=new RailwayStation();
		      station.stationUri=qs.get("?station").toString();
		      station.coordination=qs.get("?stationCoordination").toString();
		      station.cityUri=qs.get("?city").toString();
		      station.cityLabel=cityName;
		      station.branchCode=qs.get("?branchCode").toString();
		      station.stationLabel=qs.get("?stationLabel").toString();
		      
		      station.comment=qs.get("?stationComment").toString();
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
	
	
	public List<RailwayStation> getStatisticalDataTimeTableV2(String cityName) {
		List<RailwayStation> railwayStations=new ArrayList<>();

		String graphQuery="SELECT DISTINCT ?type  ?station ?TrainStationTimeTable  ?departingAt "
				+ "?arrivingAt ?hasDirection ?network ?recordedAt{"
				+ "   ?station  custom_ontology:hasTimeTable ?TrainStationTimeTable."
				+ "             ?TrainStationTimeTable a:P31 ?type;"
				+ "             custom_ontology:arrivingAt ?arrivingAt;"
				+ "            custom_ontology:departingAt ?departingAt;"
				+ "             custom_ontology:serviceProvidedBy ?network;"
				+ "             custom_ontology:recordedAt ?recordedAt;"
				+ "            custom_ontology:hasDirection ?hasDirection."
				+ "            ?station a:P131 ?city."
				+ "            ?city schema:label ?cityname"
				+ "            FILTER(?cityname = \""+cityName.toUpperCase()+"\")"
				+ "}";
		 QueryExecution queryExecution = prepaerQueryExecution(graphQuery);
		 LOG.info("EXECUTING SPAREQL QUERY"); 
		 try {
		 for (ResultSet results = queryExecution.execSelect(); results.hasNext();) {
		      QuerySolution qs = results.next();
		      
		      RailwayStation station=new RailwayStation();
		      station.stationUri=qs.get("?station").toString();
		      station.timeTableNetwork=qs.get("?network").toString();
		      station.cityUri=qs.get("?city").toString();
		      station.cityLabel=cityName;
		      station.departingTime=qs.get("?departingAt").toString();
		      station.arrivingTime=qs.get("?arrivingAt").toString();
		      station.recordedAt=qs.get("?recordedAt").toString();
		      station.timeTableDirection=qs.get("?hasDirection").toString();
		      station.instanceOf=qs.get("?TrainStationTimeTable").toString();;
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
	
	public List<RailwayStation> getAllStationStatisticTimeTableDataByCityName(String cityName) {
		List<RailwayStation> railwayStations=new ArrayList<>();
		String graphQuery="PREFIX a: <https://www.wikidata.org/wiki/Property:> "
				+ "PREFIX wd:<https://www.wikidata.org/wiki/> "
				+ "PREFIX schema:<http://www.w3.org/2000/01/rdf-schema#> "
				+ "PREFIX schemaOrg:<https://schema.org/> "
				+ "PREFIX dbo: <http://dbpedia.org/ontology/>"
				+ "PREFIX dbp: <http://dbpedia.org/property/>"
				+ "PREFIX custom_ontology:<http://www.semanticweb.org/dhayananth/ontologies/2020/11/untitled-ontology-7#>"
				+ ""
				+ "SELECT DISTINCT ?comment ?stationComment ?city  ?station ?stationLabel ?stationCoordination ?branchCode ?departingAt ?arrivingAt "
				+ "?hasDirection{"
				+ "    ?city  a:P31 wd:Q484170 ; "
				+ "    chema:label \""+cityName.toUpperCase()+"\" ;"
				+ "   schema:comment ?comment ."
				+ "    ?station  a:P31 schemaOrg:TrainStation ; "
				+ "              a:P625 ?stationCoordination; "
				+ "              schemaOrg:branchCode  ?branchCode; "
				+ "              a:P131  ?city; "
				+ "              schema:comment ?stationComment; "
				+ "              schema:label ?stationLabel "
				+ "            OPTIONAL{"
				+ "          		"
				+ "        		?station  custom_ontology:hasTimeTable ?TrainStationTimeTable;"
				+ "                                    custom_ontology:arrivingAt ?arrivingAt;"
				+ "                                    custom_ontology:hasDirection ?hasDirection;"
				+ "                                    custom_ontology:departingAt ?departingAt."
				+ "                }"
				+ "}"
				+ "";
		 QueryExecution queryExecution = prepaerQueryExecution(graphQuery);
		 LOG.info("EXECUTING SPAREQL QUERY"); 
		 try {
		 for (ResultSet results = queryExecution.execSelect(); results.hasNext();) {
		      QuerySolution qs = results.next();
		      RailwayStation station=new RailwayStation();
		      station.stationUri=qs.get("?station").toString();
		      station.coordination=qs.get("?stationCoordination").toString();
		      station.cityUri=qs.get("?city").toString();
		      station.cityLabel=cityName;
		      station.branchCode=qs.get("?branchCode").toString();
		      station.stationLabel=qs.get("?stationLabel").toString();
		      
		      station.comment=qs.get("?stationComment").toString();
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
	public void saveRealTimeRdf(List<RailwayStation> railwayStations) {
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
		String connecting_service="https://www.wikidata.org/wiki/Property:P1192";
		String dbpedia_ontology="http://dbpedia.org/ontology/";
		String dbpedia_property="http://dbpedia.org/property/";
		String custom_ontology="http://www.semanticweb.org/dhayananth/ontologies/2020/11/untitled-ontology-7#";
		 Model model =ModelFactory.createDefaultModel();
		 String timeTableGraph="INSERT DATA {";
		 int count=0;
		 SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
		 Date date = new Date(System.currentTimeMillis());
		 LOG.info("PREPARING CITY RDF DATA");
		 try {   
		 for(RailwayStation railwayStation: railwayStations) {
		        String stationQid=railwayStation.stationUri;
		        //SETTING UP AVAILABILITY BLANK NODE
		        timeTableGraph+="<"+stationQid+"> "
        		+"<"+model.createProperty(custom_ontology+"hasTimeTable").toString()+"> "
				+" [ <"+a+"> "
				+"<"+model.createProperty(custom_ontology+"TrainStationTimeTable").toString()+"> ; "
				
				+"<"+model.createProperty(custom_ontology+"hasStopName").toString()+"> "
				+" \""+railwayStation.stopPoint+"\"@en ; "
		        //SettingUp-parent_station
				+"<"+model.createProperty(custom_ontology+"definedBy").toString()+"> "
				+" \""+railwayStation.timeTableNetwork+"\"@en ; "
		      //SettingUp-trip_id
				+"<"+model.createProperty(custom_ontology+"hasTripId").toString()+"> "
				+" \""+railwayStation.tripId+"\" ; "
				//SettingUp-Direction
				+"<"+model.createProperty(custom_ontology+"hasDirection").toString()+"> "
				+" \""+railwayStation.timeTableDirection+"\" ; "
		      //SettingUp-arrival_time
				+"<"+model.createProperty(custom_ontology+"arrivingAt").toString()+"> "
				+" \""+railwayStation.arrivingTime+"\"^^<http://www.w3.org/2001/XMLSchema#dateTime> ; "
				//RecordedAt
				+"<"+model.createProperty(custom_ontology+"recordedAt").toString()+"> "
						+" \""+formatter.format(date).toString()+"\"^^<http://www.w3.org/2001/XMLSchema#dateTime> ;"
				//SettingUp-departure_time
				+"<"+model.createProperty(custom_ontology+"departingAt").toString()+"> "
				+" \""+railwayStation.departingTime+"\"^^<http://www.w3.org/2001/XMLSchema#dateTime> ; "
				//SettingUp-route_id
				+"<"+model.createProperty(custom_ontology+"hasRouteId").toString()+"> "
				+" \""+railwayStation.tripId+"\" ; "
				//SettingUp-agency_id
				+"<"+model.createProperty(custom_ontology+"serviceProvidedBy").toString()+"> "
				+" \""+railwayStation.timeTableNetwork+"\"@en  "
				+"] . ";
				if(count%2==0) {
					timeTableGraph+="}";
				  LOG.info(timeTableGraph);
				  LOG.info("STORING RDF DATA TO DB AT >>>>>> "+count);
				  saveToGraphDb(timeTableGraph);
				  LOG.info("SUCCESSFULLY STORED THE GRAPH DATA>>>>>>");
				  timeTableGraph="INSERT DATA {";
				}
				count++;
		    }
		 	timeTableGraph+="}";
		    LOG.info(timeTableGraph);
		    LOG.info("STORING RDF DATA TO DB>>>>>>");
		    saveToGraphDb(timeTableGraph);
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
