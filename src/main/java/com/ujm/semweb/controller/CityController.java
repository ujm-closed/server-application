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
import com.ujm.semweb.model.Weather;
import com.ujm.semweb.service.CityService;
import com.ujm.semweb.service.WeatherService;

@RestController
@RequestMapping(value = "/api/city/")

@CrossOrigin
public class CityController {
	@Autowired 
	private CityService cityService;
	@Autowired 
	private WeatherService weatherService;
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
	@CrossOrigin
	@RequestMapping(method=RequestMethod.POST, path = "save")
	public ResponseEntity save(@RequestBody City city) throws Exception {
			cityService.save(city);
	        return  ResponseEntity.ok("ok");
	}
	@CrossOrigin
	@RequestMapping(method=RequestMethod.POST, path = "city/weather")
	public ResponseEntity saveWeather(@RequestBody Weather weather) throws Exception {
		weatherService.saveWeatherData(weather);
	        return  ResponseEntity.ok("ok");
	}
	
}
