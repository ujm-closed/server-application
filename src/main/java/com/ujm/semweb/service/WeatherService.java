package com.ujm.semweb.service;

import java.io.File;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.springframework.stereotype.Service;

import com.ujm.semweb.model.Weather;

@Service
public class WeatherService {

	
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
		String dbpedia_ontology="http://dbpedia.org/ontology";
		String custom_ontology="http://www.semanticweb.org/dhayananth/ontologies/2020/11/untitled-ontology-7#";
		 File cityFile=new File(fileName);
		 Model model =ModelFactory.createDefaultModel();
		 String weatherQid=ex+"weather/"+weather.id;
		
		 String weatherGraph="INSERT DATA {";
		 weatherGraph+="<"+weatherQid+"> "
					+"<"+a+"> "
					+"<"+model.createProperty(custom_ontology+"TrainStationTimeTable").toString()+"> . ";
//		 weatherGraph+="<"+weatherQid+"> "
//					+"<"+custom_ontology+"identifiedBy"+"> "
//					+" \""+model.createTypedLiteral(Integer.valueOf(weather.id)).getInt()+"\"^^<http://www.w3.org/2001/XMLSchema#int> . ";
	        
	}
}
