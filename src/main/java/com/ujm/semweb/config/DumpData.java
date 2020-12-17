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
import org.springframework.beans.factory.annotation.Value;
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
	// GraphDB SemWeb PersonData
	@Value("${GRAPH_REPO_QUERY}")
    private  String GRAPH_REPO_QUERY ;
	@Value("${GRAPH_REPO_UPDATE}")
    private String GRAPH_REPO_UPDATE ;
    @PostConstruct
	public void dumpData() {
  		//FUSEKI

  		LOG.info("RDF DATA STORING PROCESS INITIATED");
//		dumpTrainStationData();
//		dumpCities();
  		LOG.info("RDF DATA STORING PROCESS COMPLETED");
	}

//  	PROBLEMS |(LA )|<|>
  	
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
	

	public void dumpCitiesGraphV2() {
		String a="https://www.wikidata.org/wiki/Property:P31";
		String wd="https://www.wikidata.org/wiki/";
		String fileName = "src/main/resources/data/refined/city_data.csv";
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
				+" \""+values[3].toString()+"\" . ";
				//SettingUp-Comment
				cityGraph+="<"+cityQid+"> "
				+"<"+RDFS.comment.toString()+"> "
				+" \""+values[3].toString()+"\"@en . ";
				//SettingUp-Coordinate
				cityGraph+="<"+cityQid+"> "
				+"<"+model.createProperty(property+"P625").toString()+"> "
				+" \""+values[2].toString()+"\" . ";
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
	
	public void dumpTrainStationGraphV2() {
		//PREFIX
		String ex="http://www.example.com/";
		String exCommune="http://www.example.com/commune/";
		String exTrainStation="http://www.example.com/trainstation/";
		String rdfs= "http://www.w3.org/2000/01/rdf-schema#";
		String xsd= "http://www.w3.org/2001/XMLSchema#" ;
		String a="https://www.wikidata.org/wiki/Property:P31";
		String wd="https://www.wikidata.org/wiki/";
		String fileName = "src/main/resources/data/refined/wiki-data-station_stations.csv";
		String geo="http://www.w3.org/2003/01/geo/wgs84_pos#";
		String administrativeTerorityOf="https://www.wikidata.org/wiki/Property:P131";
		String coordinate="https://www.wikidata.org/wiki/Property:P625";
		String property="https://www.wikidata.org/wiki/Property:";
		String schema="https://schema.org/";
		//
		POIFSFileSystem fs;
		Model model =ModelFactory.createDefaultModel();
		String stationGraph="INSERT DATA {";
		LOG.info("PREPARING TRAIN STATION GRAPH");
		File cityFile=new File(fileName);
		try (CSVReader csvReader = new CSVReader(new FileReader(cityFile))) {
		    String[] values = null;
		    int count=0;
		    LOG.info("PREPARING TRAIN STATION RDF TRIPPLETS");
		    while ((values = csvReader.readNext()) != null) {
		    	//SKIPPING HEADER CELL
		    	if(count==0) {
		    		count++;
		    		continue;
		    	}
		    	if(count==330) {
		    		String as="asd";
		    	}
		    	String stationURI=values[34].toString();
		    	if(stationURI.equals("wiki_station_name")) {continue;}
		    	String comuneURI=values[18].toString();
		    	String stationName=values[25].toString();
		    	String cityName=values[7].toString();
//		    	String stationCoordinate=values[23].toString();
		    	String stationCoordinate="Point("+values[13].toString()+" "+values[14].toString()+")";
		    	String stopAreaId="stop_area:OCE:SA:"+values[0].toString();
		    	if(stationName.isEmpty() || stationName.equals(null)|| stationName.equals("")) {
		    		stationName=values[1].toString();
		    	}
		    	if(stationURI.isEmpty() || stationURI.equals(null)|| stationURI.equals("")) {
		    		stationURI=exTrainStation+values[0].toString();
		    	}
		    	if(comuneURI.isEmpty() || comuneURI.equals(null)|| comuneURI.equals("")) {
//		    		comuneURI=exCommune+values[7].toString();
		    		comuneURI=" \""+values[7].toString()+"\" ";
		    	}else {
		    		comuneURI=" <"+comuneURI+">";
		    	}
//		    	if(stationCoordinate.isEmpty() || stationCoordinate.equals(null)|| stationCoordinate.equals("")) {
//		    		stationCoordinate="Point("+values[13].toString()+" "+values[14].toString();
//		    	}
				//SettingUp-InstanceOf
				stationGraph+="<"+stationURI+"> "
				+"<"+a+"> "
				+"<"+model.createProperty(schema+"TrainStation").toString()+"> . ";
				//SettingUp-administartiveTerorityof
				stationGraph+="<"+stationURI+"> "
				+"<"+administrativeTerorityOf+"> "
				+comuneURI+ " . ";
				//SettingUp-Label
				stationGraph+="<"+stationURI+"> "
				+"<"+RDFS.label.toString()+"> "
				+" \""+stationName+"\"@en . ";
				//SettingUp-Comment
				stationGraph+="<"+stationURI+"> "
				+"<"+RDFS.comment.toString()+"> "
				+" \""+stationName+" "+stopAreaId +"\" . ";
				//SettingUp-Coordinate
				stationGraph+="<"+stationURI+"> "
				+"<"+model.createProperty(property+"P625").toString()+"> "
				+" \""+stationCoordinate+"\" . ";
				//Setting Up City Label
				stationGraph+="<"+stationURI+"> "
				+"<"+model.createProperty(schema+"City").toString()+"> "
				+" \""+cityName+"\" . ";
				//Setting Up BranchCode
				stationGraph+="<"+stationURI+"> "
				+"<"+model.createProperty(schema+"branchCode").toString()+"> "
				+" \""+stopAreaId+"\" . ";
				//Setting Up Lat
				stationGraph+="<"+stationURI+"> "
				+"<"+model.createProperty(geo+"lat").toString()+"> "
				+" \""+model.createTypedLiteral(Double.valueOf(values[14].toString())).toString()+"\" . ";
				//Setting Up Long
				stationGraph+="<"+stationURI+"> "
				+"<"+model.createProperty(geo+"long").toString()+"> "
				+" \""+model.createTypedLiteral(Double.valueOf(values[13].toString())).toString()+"\" . ";
				if(count%10==0) {
				  stationGraph+="}";
//				  LOG.info(stationGraph);
				  LOG.info("STORING RDF DATA TO DB AT >>>>>> "+count);
				  saveToGraphDb(stationGraph);
				  LOG.info("SUCCESSFULLY STORED THE GRAPH DATA>>>>>>");
				  stationGraph="INSERT DATA {";
				}
					count++;
					System.out.println("COUNT IS >>>>>>>>> "+count);
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

}
