package com.ujm.semweb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ujm.semweb.dao.CityDao;
import com.ujm.semweb.model.City;

@Service
public class CityService {

	@Autowired
	private CityDao cityDao;
	
	public List<City> getAllCitiesFromFrance() {
		return cityDao.getAllCity();
	}
	public List<City> searchCity(String cityName) {
		return cityDao.searchEntityByCityName(cityName);
	}
	
	
}
