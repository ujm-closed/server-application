package com.ujm.semweb.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ujm.semweb.model.BikeStation;
import com.ujm.semweb.model.City;
import com.ujm.semweb.model.Hospital;
import com.ujm.semweb.model.RailwayStation;
import com.ujm.semweb.service.BikeService;
import com.ujm.semweb.service.CityService;
import com.ujm.semweb.service.HospitalService;
import com.ujm.semweb.service.RestService;

@RestController
@RequestMapping(value = "/api/bikestation/")

@CrossOrigin
public class BikeStationController {
	@Autowired 
	private BikeService bikeService;
	@CrossOrigin
	@RequestMapping(method=RequestMethod.GET, path = "search/{cityName}")
	public ResponseEntity searchCity(@PathVariable("cityName") String cityName) throws Exception {
			List<BikeStation> cities=bikeService.searchCity(cityName);
	        return  ResponseEntity.ok(cities);
	}
	 @CrossOrigin
    @RequestMapping(method=RequestMethod.POST, path = "/upload")
    public ResponseEntity<String> uploadFile(@RequestBody List<BikeStation> bikeStations) throws IOException {
    	try {
    		this.bikeService.saveRdf(bikeStations);
            return  ResponseEntity.ok("Success");
		} catch (Exception e) {
			 return  ResponseEntity.ok(e.getMessage());
		}
    }
	@CrossOrigin
	@RequestMapping(method=RequestMethod.GET, path = "restTest")
	public ResponseEntity searchCity() throws Exception {
			RestService restService=new RestService();
			restService.test();
	        return  ResponseEntity.ok("PUNDA");
	}
}
