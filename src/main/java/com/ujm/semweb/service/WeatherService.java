package com.ujm.semweb.service;

import java.io.File;
import java.io.FileReader;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.springframework.stereotype.Service;

import com.opencsv.CSVReader;
import com.ujm.semweb.config.asdasd;
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
	        
	
	
			//SettingUp-weatherId
	   
	        if(!weather.id.isEmpty() && !weather.id.equals(null) &&  !weather.id.equals("")) {
	        	weatherGraph+="<"+weatherGraph+"> "
	    				+"<"+model.createProperty(custom_ontology+"identifiedBy").toString()+"> "
	    				+" \""+weather.id.toString()+"\"@en . ";
	        }
	   
	
	    	//SettingUp-weatherMain
	        if(!weather.weather_main.isEmpty() && !weather.weather_main.equals(null) &&  !weather.weather_main.equals("")) {
	        	weatherGraph+="<"+weatherGraph+"> "
				+"<"+model.createProperty(custom_ontology+"mainWeatherDescription").toString()+"> "
				+" \""+weather.weather_main.toString()+"\"@en . ";
	        }
	    	//SettingUp-weatherDescription
	        if(!weather.description.isEmpty() && !weather.description.equals(null) &&  !weather.description.equals("")) {
	        	weatherGraph+="<"+weatherGraph+"> "
				+"<"+model.createProperty(custom_ontology+"weatherDescription").toString()+"> "
				+" \"Point("+weather.description.toString()+"\"@en . ";

	        }
	        
	        //SettingUp-mainTemperature

	        if(!weather.temperatureValue.isEmpty() && !weather.temperatureValue.equals(null) &&  !weather.temperatureValue.equals("")) {
	        	weatherGraph+="<"+weatherGraph+"> "
	        			+"<"+custom_ontology+"mainTemperature"+"> "
	        			+" \""+model.createTypedLiteral(Integer.valueOf(weather.temperatureValue)).getInt()+"\"^^<http://www.w3.org/2001/XMLSchema#double> . ";
	        	
//	   		 weatherGraph+="<"+weatherQid+"> "
//				+"<"+custom_ontology+"identifiedBy"+"> "
//				+" \""+model.createTypedLiteral(Integer.valueOf(weather.id)).getInt()+"\"^^<http://www.w3.org/2001/XMLSchema#int> . ";

	        }
	      //SettingUp-feelsLike

	        if(!weather.feelsLike.isEmpty() && weather.feelsLike.equals(null) &&  !weather.feelsLike.equals("")) {
	        	weatherGraph+="<"+weatherGraph+"> "
	        			+"<"+custom_ontology+"weatherFeelsLike"+"> "
	        			+" \""+model.createTypedLiteral(Integer.valueOf(weather.feelsLike)).getInt()+"\"^^<http://www.w3.org/2001/XMLSchema#double> . ";
	        	
	        }
	        
		      //SettingUp-temperatureMinimum
	        if(!weather.minTemperature.isEmpty() && !weather.minTemperature.equals(null) &&  !weather.minTemperature.equals("")) {
	        	weatherGraph+="<"+weatherGraph+"> "
	        			+"<"+custom_ontology+"hasWeatherMin"+"> "
	        			+" \""+model.createTypedLiteral(Integer.valueOf(weather.minTemperature)).getInt()+"\"^^<http://www.w3.org/2001/XMLSchema#double> . ";
	        	
	        }
	        //SettingUp-temperatureMaximum
	        if(!weather.maxTemperature.isEmpty() && !weather.maxTemperature.equals(null) &&  !weather.maxTemperature.equals("")) {
	        	weatherGraph+="<"+weatherGraph+"> "
	        			+"<"+custom_ontology+"hasWeatherMax"+"> "
	        			+" \""+model.createTypedLiteral(Integer.valueOf(weather.maxTemperature)).getInt()+"\"^^<http://www.w3.org/2001/XMLSchema#double> . ";
	        	}
	      //SettingUp-mainPressure
	        if(!weather.mainPressure.isEmpty() && !weather.mainPressure.equals(null) &&  !weather.mainPressure.equals("")) {
	        	weatherGraph+="<"+weatherGraph+"> "
	        	+"<"+custom_ontology+"hasMaxPressure"+"> "
	        			+" \""+model.createTypedLiteral(Integer.valueOf(weather.mainPressure)).getInt()+"\"^^<http://www.w3.org/2001/XMLSchema#double> . ";
	        	
	        }
	      //SettingUp-humidity
	        if(!weather.airHumidity.isEmpty() && !weather.airHumidity.equals(null) &&  !weather.airHumidity.equals("")) {
	        	weatherGraph+="<"+weatherGraph+"> "
	        			+"<"+custom_ontology+"hasHumidity"+"> "
	        			+" \""+model.createTypedLiteral(Integer.valueOf(weather.airHumidity)).getInt()+"\"^^<http://www.w3.org/2001/XMLSchema#double> . ";
	        	
	        }
	      //SettingUp-visibility
	        if(!weather.visibiltyAhead.isEmpty() && !weather.visibiltyAhead.equals(null) &&  !weather.visibiltyAhead.equals("")) {
	        	weatherGraph+="<"+weatherGraph+"> "
	        			+"<"+custom_ontology+"visibility"+"> "
	        			+" \""+model.createTypedLiteral(Integer.valueOf(weather.visibiltyAhead)).getInt()+"\"^^<http://www.w3.org/2001/XMLSchema#int> . ";
	        	
       	}
	        
	        //SettingUp-windSpeed
	        if(!weather.visibiltyAhead.isEmpty() && !weather.visibiltyAhead.equals(null) &&  !weather.visibiltyAhead.equals("")) {
	        	weatherGraph+="<"+weatherGraph+"> "
	        			+"<"+custom_ontology+"visibility"+"> "
	        			+" \""+model.createTypedLiteral(Integer.valueOf(weather.visibiltyAhead)).getInt()+"\"^^<http://www.w3.org/2001/XMLSchema#int> . ";
	        	
       	}
	        
	        //SettingUp-dateTime
	        if(!weather.windSpeed.isEmpty() && !weather.windSpeed.equals(null) &&  !weather.windSpeed.equals("")) {
	        	weatherGraph+="<"+weatherGraph+"> "
	        			+"<"+custom_ontology+"hasWindSpeed"+"> "
	        			+" \""+model.createTypedLiteral(Integer.valueOf(weather.windSpeed)).getInt()+"\"^^<http://www.w3.org/2001/XMLSchema#int> . ";
	        	
       	}
	        
	        //SettingUp-sunRise
	        if(!weather.sun_rise.isEmpty() && !weather.sun_rise.equals(null) &&  !weather.sun_rise.equals("")) {
	        	weatherGraph+="<"+weatherGraph+"> "
	        			+"<"+custom_ontology+"hasSunriseTime"+"> "
	        			+" \""+model.createTypedLiteral(Integer.valueOf(weather.sun_rise)).getInt()+"\"^^<http://www.w3.org/2001/XMLSchema#int> . ";
	        	
       	}
	        
	        //SettingUp-sunSet
	        if(!weather.sun_set.isEmpty() && !weather.sun_set.equals(null) &&  !weather.sun_set.equals("")) {
	        	weatherGraph+="<"+weatherGraph+"> "
	        			+"<"+custom_ontology+"hasSunsetTime"+"> "
	        			+" \""+model.createTypedLiteral(Integer.valueOf(weather.sun_set)).getInt()+"\"^^<https://www.w3.org/TR/2004/REC-xmlschema-2-20041028/#dateTimeStamp> . ";
	        	
       	}
	        
	        
			if(count%100==0) {
				weatherGraph+="}";
			  LOG.info(weatherGraph);
			  LOG.info("STORING RDF DATA TO DB AT >>>>>> "+count);
//			  saveToGraphDb(cityGraph);
//			  LOG.info("SUCCESSFULLY STORED THE GRAPH DATA>>>>>>");
			  weatherGraph="INSERT DATA {";
			}
			count++;
			System.out.println(count);
	    }
	    TimeTableGraph+="}";

	    LOG.info(TimeTableGraph);
	    LOG.info("STORING RDF DATA TO DB>>>>>>");
//	    saveToGraphDb(bikeStationGraph);
	    LOG.info("SUCCESSFULLY STORED THE GRAPH DATA>>>>>>");
	 }
	 catch(Exception e) {
		 e.printStackTrace();
	 }
}

}

}
