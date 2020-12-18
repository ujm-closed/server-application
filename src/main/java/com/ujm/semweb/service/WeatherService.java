package com.ujm.semweb.service;

import java.io.File;
import java.io.FileReader;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.opencsv.CSVReader;
import com.ujm.semweb.model.Weather;

@Service
public class WeatherService {
	 @Value("${GRAPH_REPO_QUERY}")
	 private String GRAPH_REPO_QUERY ;
     @Value("${GRAPH_REPO_UPDATE}")
     private String GRAPH_REPO_UPDATE ;
	
	public void saveWeatherData(Weather weather) {
		String a="https://www.wikidata.org/wiki/Property:P31";
		String wd="https://www.wikidata.org/wiki/";
		String fileName = "src/main/resources/data/train_station_time_table.csv";
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
		String custom_ontology="http://www.semanticweb.org/dhayananth/ontologies/2020/11/untitled-ontology-7#";
		Model model =ModelFactory.createDefaultModel();
		String weatherQid=ex+"weather/"+weather.id;
		
		String weatherGraph="INSERT DATA {";
		 weatherGraph+="<"+weatherQid+"> "
					+"<"+a+"> "
					+"<"+model.createProperty("http://dbpedia.org/page/Weather").toString()+"> . ";
			//SettingUp-weatherId
	        	weatherGraph+="<"+weatherQid+"> "
	    				+"<"+model.createProperty(custom_ontology+"identifiedBy").toString()+"> "
	    				+" \""+weather.id.toString()+"\"@en . ";
	    	//SettingUp-weatherMain
	        	weatherGraph+="<"+weatherQid+"> "
				+"<"+model.createProperty(custom_ontology+"mainWeatherDescription").toString()+"> "
				+" \""+weather.weather_main.toString()+"\"@en . ";
	    	//SettingUp-weatherDescription
	        	weatherGraph+="<"+weatherQid+"> "
				+"<"+model.createProperty(custom_ontology+"weatherDescription").toString()+"> "
				+" \"Point("+weather.description.toString()+"\"@en . ";

	        //SettingUp-mainTemperature
	        	weatherGraph+="<"+weatherQid+"> "
	        			+"<"+custom_ontology+"mainTemperature"+"> "
	        			+" \""+weather.temperatureValue+"\"^^<http://www.w3.org/2001/XMLSchema#double> . ";
//	   		 weatherGraph+="<"+weatherQid+"> "
//				+"<"+custom_ontology+"identifiedBy"+"> "
//				+" \""+model.createTypedLiteral(Integer.valueOf(weather.id)).getInt()+"\"^^<http://www.w3.org/2001/XMLSchema#int> . ";

	      //SettingUp-feelsLike
	        	weatherGraph+="<"+weatherQid+"> "
	        			+"<"+custom_ontology+"weatherFeelsLike"+"> "
	        			+" \""+weather.feelsLike+"\"^^<http://www.w3.org/2001/XMLSchema#double> . ";
		      //SettingUp-temperatureMinimum
	        	weatherGraph+="<"+weatherQid+"> "
	        			+"<"+custom_ontology+"hasWeatherMin"+"> "
	        			+" \""+weather.minTemperature+"\"^^<http://www.w3.org/2001/XMLSchema#double> . ";
	        //SettingUp-temperatureMaximum
	        	weatherGraph+="<"+weatherQid+"> "
	        			+"<"+custom_ontology+"hasWeatherMax"+"> "
	        			+" \""+weather.maxTemperature+"\"^^<http://www.w3.org/2001/XMLSchema#double> . ";
	      //SettingUp-mainPressure
	        	weatherGraph+="<"+weatherQid+"> "
	        	+"<"+custom_ontology+"hasMaxPressure"+"> "
	        			+" \""+weather.mainPressure+"\"^^<http://www.w3.org/2001/XMLSchema#double> . ";
	      //SettingUp-humidity
	        	weatherGraph+="<"+weatherQid+"> "
	        			+"<"+custom_ontology+"hasHumidity"+"> "
	        			+" \""+weather.airHumidity+"\"^^<http://www.w3.org/2001/XMLSchema#double> . ";
	      
//	        //SettingUp-windSpeed
//	        	weatherGraph+="<"+weatherQid+"> "
//	        			+"<"+custom_ontology+"visibility"+"> "
//	        			+" \""+weather.visibiltyAhead+"\"^^<http://www.w3.org/2001/XMLSchema#int> . ";
	        //SettingUp-dateTime
//	        	weatherGraph+="<"+weatherQid+"> "
//	        			+"<"+custom_ontology+"hasWindSpeed"+"> "
//	        			+" \""+weather.windSpeed+"\"^^<http://www.w3.org/2001/XMLSchema#int> . ";
	        //SettingUp-sunRise
	        	weatherGraph+="<"+weatherQid+"> "
	        			+"<"+custom_ontology+"hasSunriseTime"+"> "
	        			+" \""+weather.sun_rise+"\"^^<http://www.w3.org/2001/XMLSchema#int> . ";
	        //SettingUp-sunSet
	        	weatherGraph+="<"+weatherQid+"> "
	        			+"<"+custom_ontology+"hasSunsetTime"+"> "
	        			+" \""+weather.sun_set+"\"^^<https://www.w3.org/TR/2004/REC-xmlschema-2-20041028/#dateTimeStamp> . ";
	        weatherGraph+=" }";
	        saveToGraphDb(weatherGraph);
		}
		    
	private void saveToGraphDb(String insertStatement) {
	    UpdateRequest updateRequest = UpdateFactory.create(insertStatement);
	    UpdateProcessor updateProcessor = UpdateExecutionFactory
	        .createRemote(updateRequest, 
			GRAPH_REPO_UPDATE);
	    updateProcessor.execute();
	}

}
