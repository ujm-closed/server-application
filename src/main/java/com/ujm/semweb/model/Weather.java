package com.ujm.semweb.model;

import lombok.Data;

@Data
public class Weather {

	public String description; //3
	public String airHumidity; //9
	public String temperatureValue; //4
	public String feelsLike; //5
	public String maxTemperature;//6 
	public String minTemperature; //7
	public String recordedAt;//12 this is the dt parameter in the json 1608244121 can be converted to 2020-12-17-23-30-42-gmt

	public String id;//1
	
	public String weather_main;
	public String mainPressure; //airPressure unit is hPa
	public String visibiltyAhead; //10000m = 10km. how far could have been seen in straight line without obstruction
	public String windSpeed; //km/hr
	public String sun_rise; //time given which is same as unix to regular dataTime conversion
	public String sun_set;
	
	
	
	
}
