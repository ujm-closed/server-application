package com.ujm.semweb.config;


import javax.annotation.PostConstruct;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Logger;

@Component
public class DumpData {
	private final Logger LOG = Logger.getLogger(DumpData.class.getName());
	// GraphDB 
    private final String GRAPH_REPO_QUERY = 
      "http://localhost:7200/repositories/PersonData";
    private final String GRAPH_REPO_UPDATE = 
      "http://localhost:7200/repositories/PersonData/statements";
  	@PostConstruct
	public void dumpData() {
  		//FUSEKI
//		dumpTrainStationData();
//		dumpCities();
	}
	
	public void dumpCities() {
	String a="https://www.wikidata.org/wiki/Property:P31";
	String wd="https://www.wikidata.org/wiki/";
	String fileName = "src/main/resources/data/cities.csv";
	String geo="http://www.w3.org/2003/01/geo/wgs84_pos#";
	String administrativeTerorityOf="https://www.wikidata.org/wiki/Property:P131";
	String property="https://www.wikidata.org/wiki/Property:";
	String coordinate="https://www.wikidata.org/wiki/Property:P625";
	 ClassLoader classLoader = getClass().getClassLoader();
//	 File cityFile=new File(classLoader.getResource("queries").getFile());
	 File cityFile=new File(fileName);
	 Model model =ModelFactory.createDefaultModel();
	 try (CSVReader csvReader = new CSVReader(new FileReader(cityFile))) {
	    String[] values = null;
	    int count=0;
	    while ((values = csvReader.readNext()) != null) {
	    	//SKINPING HEADING
	    	if(count==0) {
	    		count++;
	    		continue;
	    	}
	      
//	    	System.out.println(values[3].split(" ")[0].replace("Point(",""));
//	    	System.out.println(values[3].split(" ")[1].replace(")",""));
	          
	        Resource city
			  = model.createResource(values[0])
//			         .addProperty(model.createProperty(a), wd+"Q484170")
					 .addProperty(model.createProperty(a), model.createProperty(wd+"Q484170"))
					 .addProperty(model.createProperty(property+"P17"), model.createProperty(wd+"Q142"))
			         .addProperty(RDFS.label,model.createLiteral(values[1], "en"))
			         
			         .addProperty(RDFS.comment,model.createLiteral(values[1], "en"))
			         .addProperty(model.createProperty(property+"P625"),model.createLiteral(values[3], "en"))
//			         .addProperty(model.createProperty(geo+"long"),model.createTypedLiteral(Double.valueOf(values[3].split(" ")[0].replace("Point(",""))))
//			         .addProperty(model.createProperty(geo+"lat"),model.createTypedLiteral(Double.valueOf(values[3].split(" ")[1].replace("Point(",""))))
		    ;    
	    }
	 }
	 catch(Exception ex) {
		 ex.printStackTrace();
	 }
	 saveToTrippleStore(model);
	}
	
	public void dumpCitiesGraph() {
		String a="https://www.wikidata.org/wiki/Property:P31";
		String wd="https://www.wikidata.org/wiki/";
		String fileName = "src/main/resources/data/cities.csv";
		String geo="http://www.w3.org/2003/01/geo/wgs84_pos#";
		String administrativeTerorityOf="https://www.wikidata.org/wiki/Property:P131";
		String property="https://www.wikidata.org/wiki/Property:";
		String coordinate="https://www.wikidata.org/wiki/Property:P625";
		 ClassLoader classLoader = getClass().getClassLoader();
		 File cityFile=new File(fileName);
		 Model model =ModelFactory.createDefaultModel();
		 String cityGraph="INSERT DATA {";
		 try (CSVReader csvReader = new CSVReader(new FileReader(cityFile))) {
		    String[] values = null;
		    int count=0;
		    LOG.info("PREPARING CITY RDF DATA");
		    while ((values = csvReader.readNext()) != null) {
		    	//SKINPING HEADING
		    	if(count==0) {
		    		count++;
		    		continue;
		    	}
		        String cityQid=values[0];
				//SettingUp-InstanceOf
		        cityGraph+="<"+cityQid+"> "
				+"<"+a+"> "
				+"<"+model.createProperty(wd+"Q484170").toString()+"> . ";
				//SettingUp-Country
				cityGraph+="<"+cityQid+"> "
				+"<"+model.createProperty(property+"P17").toString()+"> "
				+"<"+model.createProperty(property+"P17").toString()+"> . ";
				//SettingUp-Label
				cityGraph+="<"+cityQid+"> "
				+"<"+RDFS.label.toString()+"> "
				+" \""+values[1].toString()+"\"@en . ";
				//SettingUp-Comment
				cityGraph+="<"+cityQid+"> "
				+"<"+RDFS.comment.toString()+"> "
				+" \""+values[1].toString()+"\"@en . ";
				//SettingUp-Coordinate
				cityGraph+="<"+cityQid+"> "
				+"<"+model.createProperty(property+"P625").toString()+"> "
				+" \""+values[3].toString()+"\" . ";
//				model.createTypedLiteral(Double.valueOf(stopLat)).getDatatypeURI()
				if(count%100==0) {
				  cityGraph+="}";
				  LOG.info(cityGraph);
				  LOG.info("STORING RDF DATA TO DB AT >>>>>> "+count);
				  saveToGraphDb(cityGraph);
				  LOG.info("SUCCESSFULLY STORED THE GRAPH DATA>>>>>>");
				  cityGraph="INSERT DATA {";
				}
				count++;
		    }
		    cityGraph+="}";

		    LOG.info(cityGraph);
		    LOG.info("STORING RDF DATA TO DB>>>>>>");
		    saveToGraphDb(cityGraph);
		    LOG.info("SUCCESSFULLY STORED THE GRAPH DATA>>>>>>");
		 }
		 catch(Exception ex) {
			 ex.printStackTrace();
		 }
	}
	
	public void dumpTrainStationGraph() {
		//PREFIX
		String ex="http://www.example.com/";
		String exCommune="http://www.example.com/commune/";
		String exTrainStation="http://www.example.com/trainstation/";
		String rdfs= "http://www.w3.org/2000/01/rdf-schema#";
		String xsd= "http://www.w3.org/2001/XMLSchema#" ;
		
		String a="https://www.wikidata.org/wiki/Property:P31";
		String wd="https://www.wikidata.org/wiki/";
		String fileName = "src/main/resources/data/train_stations.xls";
		String geo="http://www.w3.org/2003/01/geo/wgs84_pos#";
		String administrativeTerorityOf="https://www.wikidata.org/wiki/Property:P131";
		String coordinate="https://www.wikidata.org/wiki/Property:P625";
		String property="https://www.wikidata.org/wiki/Property:";
		//
		POIFSFileSystem fs;
		Model model =ModelFactory.createDefaultModel();
		String stationGraph="INSERT DATA {";
		LOG.info("PREPARING TRAIN STATION GRAPH");
		try {
			fs = new POIFSFileSystem(new FileInputStream(fileName));
		
		    HSSFWorkbook wb = new HSSFWorkbook(fs);
		    HSSFSheet sheet = wb.getSheetAt(0);
		    Iterator<Row> rowIterator = sheet.iterator();
		    int count=0;
		    while (rowIterator.hasNext()) {
		    	//SKIPPING HEADER CELL
		    	if(count==0) {
		    		count++;
		    		continue;
		    	}
		    	Row row = rowIterator.next();
		    	String stationURI=row.getCell(21).getStringCellValue();
		    	if(stationURI.equals("station")) {continue;}
		    	String comuneURI=row.getCell(18).getStringCellValue();
		    	String stationName=row.getCell(1).getStringCellValue();
		    	String stationCoordinate=row.getCell(23).getStringCellValue();
		    	if(stationURI.isEmpty() || stationURI.equals(null)|| stationURI.equals("")) {
		    		stationURI=exTrainStation+row.getCell(0).getNumericCellValue();
		    	}
		    	if(comuneURI.isEmpty() || comuneURI.equals(null)|| comuneURI.equals("")) {
		    		comuneURI=exCommune+row.getCell(7).getStringCellValue();
		    	}
		    	if(stationCoordinate.isEmpty() || stationCoordinate.equals(null)|| stationCoordinate.equals("")) {
		    		stationCoordinate="P("+row.getCell(13).getNumericCellValue()+" "+row.getCell(14).getNumericCellValue();
		    	}
				//SettingUp-InstanceOf
				stationGraph+="<"+stationURI+"> "
				+"<"+a+"> "
				+"<"+model.createProperty(wd+"Q55488").toString()+"> . ";
				//SettingUp-administartiveTerorityof
				stationGraph+="<"+stationURI+"> "
				+"<"+administrativeTerorityOf+"> "
				+"<"+comuneURI+"> . ";
				//SettingUp-Label
				stationGraph+="<"+stationURI+"> "
				+"<"+RDFS.label.toString()+"> "
				+" \""+stationName+"\"@en . ";
				//SettingUp-Comment
				stationGraph+="<"+stationURI+"> "
				+"<"+RDFS.comment.toString()+"> "
				+" \""+stationName+"\"@en . ";
				//SettingUp-Coordinate
				stationGraph+="<"+stationURI+"> "
				+"<"+model.createProperty(property+"P625").toString()+"> "
				+" \""+stationCoordinate+"\" . ";
				
				if(count==399) {
					System.out.print("SS");
				}
				if(count%100==0) {
				  stationGraph+="}";
				  LOG.info(stationGraph);
				  LOG.info("STORING RDF DATA TO DB AT >>>>>> "+count);
				  saveToGraphDb(stationGraph);
				  LOG.info("SUCCESSFULLY STORED THE GRAPH DATA>>>>>>");
				  stationGraph="INSERT DATA {";
				}
					count++;
		    }
	    stationGraph+="}";
	    LOG.info(stationGraph);
	    saveToGraphDb(stationGraph);
	    LOG.info("SUCCESSFULLY UPDATED GRAPH TO THE TRIPPLESTORE");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void dumpTrainStationData() {
		//PREFIX
		String ex="http://www.example.com/";
		String exCommune="http://www.example.com/commune/";
		String exTrainStation="http://www.example.com/trainstation/";
		String rdfs= "http://www.w3.org/2000/01/rdf-schema#";
		String xsd= "http://www.w3.org/2001/XMLSchema#" ;
		
		String a="https://www.wikidata.org/wiki/Property:P31";
		String wd="https://www.wikidata.org/wiki/";
		String fileName = "src/main/resources/data/train_stations.xls";
		String geo="http://www.w3.org/2003/01/geo/wgs84_pos#";
		String administrativeTerorityOf="https://www.wikidata.org/wiki/Property:P131";
		String coordinate="https://www.wikidata.org/wiki/Property:P625";
		String property="https://www.wikidata.org/wiki/Property:";
		//
		POIFSFileSystem fs;
		Model model =ModelFactory.createDefaultModel();
		try {
			fs = new POIFSFileSystem(new FileInputStream(fileName));
		
		    HSSFWorkbook wb = new HSSFWorkbook(fs);
		    HSSFSheet sheet = wb.getSheetAt(0);
		    Iterator<Row> rowIterator = sheet.iterator();
		    while (rowIterator.hasNext()) {
		    	Row row = rowIterator.next();
		    	String stationURI=row.getCell(21).getStringCellValue();
		    	String comuneURI=row.getCell(18).getStringCellValue();
		    	String stationName=row.getCell(1).getStringCellValue();
		    	String stationCoordinate=row.getCell(23).getStringCellValue();
		    	if(stationURI.isEmpty() || stationURI.equals(null)|| stationURI.equals("")) {
		    		stationURI=exTrainStation+row.getCell(0).getNumericCellValue();
		    	}
		    	if(comuneURI.isEmpty() || comuneURI.equals(null)|| comuneURI.equals("")) {
		    		comuneURI=exCommune+row.getCell(7).getStringCellValue();
		    	}
		    	if(stationCoordinate.isEmpty() || stationCoordinate.equals(null)|| stationCoordinate.equals("")) {
		    		stationCoordinate="P("+row.getCell(13).getNumericCellValue()+" "+row.getCell(14).getNumericCellValue();
		    	}
		    	
		    	Resource station
				  = model.createResource(stationURI)
				  .addProperty(model.createProperty(a),model.createProperty(wd+"Q55488"))
				  .addProperty(model.createProperty(administrativeTerorityOf), model.createProperty(comuneURI))
				  .addProperty(RDFS.label,model.createLiteral(stationName, "en"))
				  .addProperty(RDFS.comment,model.createLiteral(stationName, "en"))
//				  coordinate
		          .addProperty(model.createProperty(property+"P625"),model.createLiteral(stationCoordinate));
            }
		    saveToTrippleStore(model);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void saveToTrippleStore(Model model) {
		String datasetURL = "http://localhost:3030/test";
		String sparqlEndpoint = datasetURL + "/sparql";
		String sparqlUpdate = datasetURL + "/update";
		String graphStore = datasetURL + "/data";
		RDFConnection conneg = RDFConnectionFactory.connect(sparqlEndpoint,sparqlUpdate,graphStore);
		conneg.load(model); 
		conneg.update("INSERT DATA { <test> a <TestClass> }"); 
	}
	
	private void saveToGraphDb(String insertStatement) {
	    UpdateRequest updateRequest = UpdateFactory.create(insertStatement);
	    UpdateProcessor updateProcessor = UpdateExecutionFactory
	        .createRemote(updateRequest, 
    		GRAPH_REPO_UPDATE);
	    updateProcessor.execute();
    }
	
	//***
	public void dumpHospitalGraph() {
		String a="https://www.wikidata.org/wiki/Property:P31";
		String wd="https://www.wikidata.org/wiki/";
		String fileName = "src/main/resources/data/hospital.csv";
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
		 ClassLoader classLoader = getClass().getClassLoader();
		 File cityFile=new File(fileName);
		 Model model =ModelFactory.createDefaultModel();
		 String hospitalGraph="INSERT DATA {";
		 try (CSVReader csvReader = new CSVReader(new FileReader(cityFile))) {
		    String[] values = null;
		    int count=0;
		    LOG.info("PREPARING CITY RDF DATA");
		    while ((values = csvReader.readNext()) != null) {
		    	//SKINPING HEADING
		    	if(count==0) {
		    		count++;
		    		continue;
		    	}
		        String hospitalQid=ex+values[0];
        		//DEBUGGINH
		        if(hospitalQid.equals("780000410")) {
		        	System.out.print("SAD");
		        }
		        
		        //SettingUp-InstanceOf
		        hospitalGraph+="<"+hospitalQid+"> "
				+"<"+a+"> "
				+"<"+model.createProperty(schema+"Hospital").toString()+"> . ";
		        
//				//SettingUp-Address
//		        hospitalGraph+="<"+hospitalQid+"> "
//				+"<"+model.createProperty(schema+"address").toString()+"> "
//				+" \""+values[6].toString()+"\"@en . ";
		        
				//SettingUp-Ã¯Â»Â¿finess_et
		   
		        if(!values[0].isEmpty() && !values[0].equals(null) &&  !values[0].equals("")) {
		        	hospitalGraph+="<"+hospitalQid+"> "
		    				+"<"+model.createProperty(property+"P1901").toString()+"> "
		    				+" \""+values[0].toString()+"\"@en . ";
		        }
		      //SettingUp-raison_sociale
		        if(!values[1].isEmpty() && !values[1].equals(null) &&  !values[1].equals("")) {
			        hospitalGraph+="<"+hospitalQid+"> "
					+"<"+model.createProperty(schema+"medicalSpecialty").toString()+"> "
					+" \""+values[1].toString()+"\"@en . ";
		        }
		    	//SettingUp-addressLocality
		        if(!values[2].isEmpty() && !values[2].equals(null) &&  !values[2].equals("")) {
			        hospitalGraph+="<"+hospitalQid+"> "
					+"<"+model.createProperty(schema+"addressLocality").toString()+"> "
					+" \""+values[2].toString()+"\"@en . ";
		        }
		    	//SettingUp-address
		        if(!values[3].isEmpty() && !values[3].equals(null) &&  !values[3].equals("")) {
			        hospitalGraph+="<"+hospitalQid+"> "
					+"<"+model.createProperty(schema+"address").toString()+"> "
					+" \""+values[3].toString()+"\"@en . ";
		        }
		        if(!values[4].isEmpty() && !values[4].equals(null) &&  !values[4].equals("")) {
		    	//SettingUp-telephone
			        hospitalGraph+="<"+hospitalQid+"> "
					+"<"+model.createProperty(schema+"telephone").toString()+"> "
					+" \""+values[4].toString()+"\"@en . ";
		        }
		        if(!values[5].isEmpty() && !values[5].equals(null) &&  !values[5].equals("")) {
		    	//SettingUp-faxNumber
		        	hospitalGraph+="<"+hospitalQid+"> "
					+"<"+model.createProperty(schema+"faxNumber").toString()+"> "
					+" \""+values[5].toString()+"\"@en . ";
		        }
		    	//SettingUp-type_etablissement
		        if(!values[6].isEmpty() && !values[6].equals(null) &&  !values[6].equals("")) {
			        hospitalGraph+="<"+hospitalQid+"> "
					+"<"+model.createProperty(property+"P2541").toString()+"> "
					+" \""+values[6].toString()+"\"@en . ";
		        }
		        if(!values[7].isEmpty() && !values[7].equals(null) &&  !values[7].equals("")) {
		    	//SettingUp-date_ouverture
		        hospitalGraph+="<"+hospitalQid+"> "
				+"<"+model.createProperty(date_ouverture).toString()+"> "
				+" \""+values[7].toString()+"\"@en . ";
		        }
		    	//SettingUp-lat
		        if(!values[8].isEmpty() && !values[8].equals(null) &&  !values[8].equals("")) {
		        hospitalGraph+="<"+hospitalQid+"> "
				+"<"+model.createProperty(geo+"lat").toString()+"> "
				+" \""+values[8].toString()+"\"@en . ";
		        }
		    	//SettingUp-long
		        if(!values[9].isEmpty() && !values[9].equals(null) &&  !values[9].equals("")) {
		        hospitalGraph+="<"+hospitalQid+"> "
				+"<"+model.createProperty(geo+"long").toString()+"> "
				+" \""+values[9].toString()+"\"@en . ";
		        }
		    	//SettingUp-wgs84
		        if(!values[10].isEmpty() && !values[10].equals(null) &&  !values[10].equals("")) {
			        hospitalGraph+="<"+hospitalQid+"> "
					+"<"+model.createProperty(wgs84).toString()+"> "
					+" \""+values[10].toString()+"\"@en . ";
			    }
		    	//SettingUp-COMMUNE
		        if(!values[11].isEmpty() && !values[11].equals(null) &&  !values[11].equals("")) {
			        hospitalGraph+="<"+hospitalQid+"> "
					+"<"+model.createProperty(schema+"branchCode").toString()+"> "
					+" \""+values[11].toString()+"\"@en . ";
			    }
		        
		    	//SettingUp-typeOf
		        if(!values[12].isEmpty() && !values[12].equals(null) &&  !values[12].equals("")) {
			        hospitalGraph+="<"+hospitalQid+"> "
					+"<"+model.createProperty(schema+"City").toString()+"> "
					+"<"+values[12].toString()+"> . ";
		        }
		        
				//SettingUp-Label
		        if(!values[6].isEmpty() && !values[6].equals(null) &&  !values[6].equals("")) {
			        hospitalGraph+="<"+hospitalQid+"> "
					+"<"+RDFS.label.toString()+"> "
					+" \""+values[6].toString()+"\"@en . ";
			    }
		        
		        if(!values[6].isEmpty() && !values[6].equals(null) &&  !values[6].equals("")) {	
					//SettingUp-Comment
			        hospitalGraph+="<"+hospitalQid+"> "
					+"<"+RDFS.comment.toString()+"> "
					+" \""+values[6].toString()+"\" . ";
			    }
				//SettingUp-Coordinate
		        if(!values[9].isEmpty() && !values[9].equals(null) &&  !values[9].equals("")) {
		        	hospitalGraph+="<"+hospitalQid+"> "
					+"<"+model.createProperty(property+"P625").toString()+"> "
					+" \"Point("+values[9].toString()+" "+ values[8].toString()+")\" . ";
	//				model.createTypedLiteral(Double.valueOf(stopLat)).getDatatypeURI()
		        }
				if(count%100==0) {
					hospitalGraph+="}";
				LOG.info("STORING RDF DATA TO DB AT >>>>>> "+count);
				  LOG.info(hospitalGraph);
				  LOG.info("STORING RDF DATA TO DB AT >>>>>> "+count);
//				  saveToGraphDb(cityGraph);
//				  LOG.info("SUCCESSFULLY STORED THE GRAPH DATA>>>>>>");
				  hospitalGraph+="INSERT DATA {";
				}
				LOG.info("COUNT IS >>>>>> "+count);
				count++;
		    }
		    hospitalGraph+="}";

		    LOG.info(hospitalGraph);
		    LOG.info("STORING RDF DATA TO DB>>>>>>");
//		    saveToGraphDb(hospitalGraph);
		    LOG.info("SUCCESSFULLY STORED THE GRAPH DATA>>>>>>");
		 }
		 catch(Exception e) {
			 e.printStackTrace();
		 }
	}
	

	public void dumpBiksStationGraph() {
		String a="https://www.wikidata.org/wiki/Property:P31";
		String wd="https://www.wikidata.org/wiki/";
		String fileName = "src/main/resources/data/lyon_bike_station.csv";
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
		 File cityFile=new File(fileName);
		 Model model =ModelFactory.createDefaultModel();
		 String bikeStationGraph="INSERT DATA {";
		 try (CSVReader csvReader = new CSVReader(new FileReader(cityFile))) {
		    String[] values = null;
		    int count=0;
		    LOG.info("PREPARING CITY RDF DATA");
		    while ((values = csvReader.readNext()) != null) {
		    	//SKINPING HEADING
		    	if(count==0) {
		    		count++;
		    		continue;
		    	}
		        String bikeStationQid=ex+values[0];
        		//DEBUGGINH
//		        if(hospitalQid.equals("780000410")) {
//		        	System.out.print("SAD");
//		        }
		        
//		        //SettingUp-InstanceOf
		        bikeStationGraph+="<"+bikeStationQid+"> "
				+"<"+a+"> "
				+"<"+model.createProperty(dbpedia_ontology+"Station").toString()+"> . ";
		        
//				//SettingUp-Address
//		        hospitalGraph+="<"+hospitalQid+"> "
//				+"<"+model.createProperty(schema+"address").toString()+"> "
//				+" \""+values[6].toString()+"\"@en . ";
		        
				//SettingUp-Ã¯Â»Â¿finess_et
		   
		        if(!values[0].isEmpty() && !values[0].equals(null) &&  !values[0].equals("")) {
		        	bikeStationGraph+="<"+bikeStationQid+"> "
		    				+"<"+model.createProperty(property+"P1901").toString()+"> "
		    				+" \""+values[0].toString()+"\"@en . ";
		        }
		   
		
		    	//SettingUp-connecting service
		        if(!values[1].isEmpty() && !values[1].equals(null) &&  !values[1].equals("")) {
		        	bikeStationGraph+="<"+bikeStationQid+"> "
					+"<"+model.createProperty(connecting_service).toString()+"> "
					+" \""+values[1].toString()+"\"@en . ";
		        }
		    	//SettingUp-address
		        if(!values[2].isEmpty() && !values[2].equals(null) &&  !values[2].equals("")) {
		        	bikeStationGraph+="<"+bikeStationQid+"> "
					+"<"+model.createProperty(schema+"address").toString()+"> "
					+" \""+values[3].toString()+"\"@en . ";
		        }
		        
		    	
			
		 		//SettingUp-Coordinate
		        if(!values[4].isEmpty() && !values[4].equals(null) &&  !values[4].equals("")) {
		        	bikeStationGraph+="<"+bikeStationQid+"> "
					+"<"+model.createProperty(property+"P625").toString()+"> "
					+" \"Point("+values[4].toString()+" "+ values[3].toString()+")\" . ";
	//				model.createTypedLiteral(Double.valueOf(stopLat)).getDatatypeURI()
		        }
		        
		        //SettingUp-brand
		        if(!values[5].isEmpty() && !values[5].equals(null) &&  !values[5].equals("")) {
		        	bikeStationGraph+="<"+bikeStationQid+"> "
					+"<"+model.createProperty(schema+"brand").toString()+"> "
					+" \""+values[5].toString()+"\"@en . ";
		        }
		      //SettingUp-product_id
		        if(!values[6].isEmpty() && !values[6].equals(null) &&  !values[6].equals("")) {
		        	bikeStationGraph+="<"+bikeStationQid+"> "
					+"<"+model.createProperty(schema+"productID").toString()+"> "
					+" \""+values[6].toString()+"\"@en . ";
		        }
		        
			      //SettingUp-number of racks
		        if(!values[7].isEmpty() && !values[7].equals(null) &&  !values[7].equals("")) {
		        	bikeStationGraph+="<"+bikeStationQid+"> "
					+"<"+model.createProperty(schema+"additionalProperty").toString()+"> "
					+" \""+values[7].toString()+"\"@en . ";
		        }
		        
		        
				if(count%100==0) {
					bikeStationGraph+="}";
				  LOG.info(bikeStationGraph);
				  LOG.info("STORING RDF DATA TO DB AT >>>>>> "+count);
//				  saveToGraphDb(cityGraph);
//				  LOG.info("SUCCESSFULLY STORED THE GRAPH DATA>>>>>>");
				  bikeStationGraph="INSERT DATA {";
				}
				count++;
				System.out.println(count);
		    }
		    bikeStationGraph+="}";

		    LOG.info(bikeStationGraph);
		    LOG.info("STORING RDF DATA TO DB>>>>>>");
//		    saveToGraphDb(bikeStationGraph);
		    LOG.info("SUCCESSFULLY STORED THE GRAPH DATA>>>>>>");
		 }
		 catch(Exception e) {
			 e.printStackTrace();
		 }
	}
	

}
