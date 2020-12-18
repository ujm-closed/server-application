package com.ujm.semweb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ujm.semweb.dao.CityDao;
import com.ujm.semweb.dao.HospitalDao;
import com.ujm.semweb.model.City;
import com.ujm.semweb.model.Hospital;

@Service
public class HospitalService {

	@Autowired
	private HospitalDao hospitalDao;
	
	public List<Hospital> searchCity(String cityName) {
		return hospitalDao.getAllHospitalByCityName(cityName);
	}
	
	
}
