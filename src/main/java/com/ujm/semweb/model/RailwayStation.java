package com.ujm.semweb.model;

import org.apache.jena.vocabulary.RDFS;

import lombok.Data;

@Data
public class RailwayStation {
  public String stationUri;
  public String cityLabel;
  public String stationLabel;
  public String cityUri;
  public String stationCoordination;
  public String comment;
  public String coordination;
  public String lat;
  public String long_;
  public String instanceOf;
  public String branchCode;
  
  //REAL TIME DATA PREDICATES
  public String timeTableDirection;
  public String timeTableNetwork;
  public String timeTableLabel;
  public String arrivingTime;
  public String departingTime;
  public String transportMean;
  public String commercialModes;
  public String stopPoint;
  public String tripId;
  public String recordedAt;
  
}
