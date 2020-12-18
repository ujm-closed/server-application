package com.ujm.semweb.model;

import java.util.Date;
import java.util.List;

public class Record {

	    public String datasetid;
	    public String recordid;
	    public List<Fields> fields;
	    public Geometry geometry;
	    public Date record_timestamp;
	
	public class Fields{
	    public String etat;
	    public Date lastupdate;
	    public int nombrevelosdisponibles;
	    public int nombreemplacementsactuels;
	    public String nom;
	    public int nombreemplacementsdisponibles;
	    public String idstation;
	    public List<Double> coordonnees;
	}

	public class Geometry{
	    public String type;
	    public List<Double> coordinates;
	}

	

	public class Root{
	    public int nhits;
	    public Parameters parameters;
	    public List<Record> records;
	}
	public class Parameters{
    public List<String> dataset;
    public String timezone;
    public int rows;
    public int start;
    public String format;
}
}
