package com.ujm.semweb.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ujm.semweb.model.BikeStation;
import com.ujm.semweb.model.City;
import com.ujm.semweb.model.RailwayStation;
import com.ujm.semweb.service.CityService;
import com.ujm.semweb.service.StationService;

@RestController
@RequestMapping(value = "/api/station/")

@CrossOrigin
public class StationController {
	@Autowired 
	private StationService stationService;
//	@CrossOrigin
//	@RequestMapping(method=RequestMethod.GET, path = "all")
//	public ResponseEntity getAllCity() throws Exception {
//			List<City> cities=cityService.getAllCitiesFromFrance();
//	        return  ResponseEntity.ok(cities);
//	}
	@CrossOrigin
	@RequestMapping(method=RequestMethod.GET, path = "search/{cityName}")
	public ResponseEntity searchCity(@PathVariable("cityName") String cityName) throws Exception {
			List<RailwayStation> stations=stationService.searchByCityName(cityName);
	        return  ResponseEntity.ok(stations);
	}

	 @CrossOrigin
    @RequestMapping(method=RequestMethod.POST, path = "upload")
    public ResponseEntity<String> uploadStatisticData(@RequestBody List<RailwayStation> stations) throws IOException {
    	try {
    		this.stationService.saveRealTimeData(stations);
            return  ResponseEntity.ok("Success");
		} catch (Exception e) {
			 return  ResponseEntity.ok(e.getMessage());
		}
    }
	 
	 @CrossOrigin
	@RequestMapping(method=RequestMethod.GET, path = "timetable/{cityName}")
	public ResponseEntity getStatisticDataByCityName(@PathVariable("cityName") String cityName) throws Exception {
			List<RailwayStation> stations=stationService.getTimeTableDataByCityName(cityName);
	        return  ResponseEntity.ok(stations);
	}
}