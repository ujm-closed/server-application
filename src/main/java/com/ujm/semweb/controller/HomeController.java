package com.ujm.semweb.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/api/")

@CrossOrigin
public class HomeController {
	@RequestMapping(method=RequestMethod.GET, path= "" )
    public String greet(){
        return "Hello";
    }
    @CrossOrigin
    @RequestMapping(method=RequestMethod.POST, path = "/upload")
    public ResponseEntity<String> uploadFile(@RequestBody MultipartFile files) throws IOException {
    	try {
    		
            return  ResponseEntity.ok("Success");
		} catch (Exception e) {
			 return  ResponseEntity.ok(e.getMessage());
		}
    }
}