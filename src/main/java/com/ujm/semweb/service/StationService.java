package com.ujm.semweb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ujm.semweb.dao.CityDao;
import com.ujm.semweb.dao.StationDao;
import com.ujm.semweb.model.City;
import com.ujm.semweb.model.RailwayStation;

@Service
public class StationService {

	@Autowired
	private StationDao stationDao;

	public List<RailwayStation> searchByCityName(String cityName) {
		return stationDao.getAllStationByCityName(cityName);
	}
	public List<RailwayStation> getTimeTableDataByCityName(String cityName) {
		return stationDao.getStatisticalDataTimeTableV2(cityName);
	}
	public void saveRealTimeData(List<RailwayStation> stations) {
		 stationDao.saveRealTimeRdf(stations);
	}
	
	
	
}
