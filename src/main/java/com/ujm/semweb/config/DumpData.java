package com.ujm.semweb.config;


import javax.annotation.PostConstruct;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

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
	
  	/*
  	 * SELECT  * {
  <http://www.wikidata.org/entity/Q90> ?predicate ?d
}LIMIT 100
<http://www.w3.org/2000/01/rdf-schema#label>

SELECT  * {
    ?cities <https://www.wikidata.org/wiki/Property:P31> "https://www.wikidata.org/wiki/Q484170"
}LIMIT 100


SELECT  * {
  ?cities <https://www.wikidata.org/wiki/Property:P31> "https://www.wikidata.org/wiki/Q484170" .
  ?stations <https://www.wikidata.org/wiki/Property:P31> "https://www.wikidata.org/wiki/Q55488"; 
                      <https://www.wikidata.org/wiki/Property:P131> ?cities .
}LIMIT 10
  	 */
  
  	@PostConstruct
	public void dumpData() {
//		dumpTrainStationData();
		dumpCities();
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
	
//	https://github.com/trainline-eu/stations
//	https://ressources.data.sncf.com/explore/dataset/liste-des-gares/export/
	public void saveToTrippleStore(Model model) {
		String datasetURL = "http://localhost:3030/test";
		String sparqlEndpoint = datasetURL + "/sparql";
		String sparqlUpdate = datasetURL + "/update";
		String graphStore = datasetURL + "/data";
		RDFConnection conneg = RDFConnectionFactory.connect(sparqlEndpoint,sparqlUpdate,graphStore);
		conneg.load(model); 
		conneg.update("INSERT DATA { <test> a <TestClass> }"); 
	}
	

}