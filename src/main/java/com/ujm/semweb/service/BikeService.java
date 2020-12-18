package com.ujm.semweb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ujm.semweb.dao.BikeStationDao;
import com.ujm.semweb.dao.CityDao;
import com.ujm.semweb.dao.HospitalDao;
import com.ujm.semweb.model.BikeStation;
import com.ujm.semweb.model.City;
import com.ujm.semweb.model.Hospital;

@Service
public class BikeService {

	@Autowired
	private BikeStationDao bikeStationDao;
	
	public List<BikeStation> searchCity(String cityName) {
		return bikeStationDao.getAllStationByCityName(cityName);
	}
	public void saveRdf(List<BikeStation> bikeStations) {
		 bikeStationDao.saveRdf(bikeStations);
	}
	
	
}
