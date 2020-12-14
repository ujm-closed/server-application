package com.ujm.semweb.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ujm.semweb.model.City;
import com.ujm.semweb.service.CityService;

@RestController
@RequestMapping(value = "/api/city/")

@CrossOrigin
public class CityController {
	@Autowired 
	private CityService cityService;
	@CrossOrigin
	@RequestMapping(method=RequestMethod.GET, path = "all")
	public ResponseEntity getAllCity() throws Exception {
			List<City> cities=cityService.getAllCitiesFromFrance();
	        return  ResponseEntity.ok(cities);
	}
	@CrossOrigin
	@RequestMapping(method=RequestMethod.POST, path = "search")
	public ResponseEntity searchCity(@RequestBody String cityName) throws Exception {
			List<City> cities=cityService.searchCity(cityName);
	        return  ResponseEntity.ok(cities);
	}
	
}
