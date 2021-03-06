package com.example.OODProject.Controller;

import com.example.OODProject.Exception.AvailableRecordException;
import com.example.OODProject.Exception.NotFoundException;
import com.example.OODProject.Model.Airport;
import com.example.OODProject.Request.AirportRequest;
import com.example.OODProject.Service.airport_services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@ComponentScan(basePackages = "com.example.OODProject")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/airport")
public class AirportController {
    @Autowired
    airport_services service;

    @PostMapping("/add_airport")
    @ExceptionHandler(AvailableRecordException.class)
    public ResponseEntity<?> create_airport(@RequestBody AirportRequest airport)
    {
        return service.add_airport(airport);
    }

    @DeleteMapping("/delete/{id}")
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> delete_airport(@PathVariable("id") String airport_code)
    {
        return service.delete_airport(airport_code);
    }

    @PutMapping("/modify_airport/{airport_code}")
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity update_airport(@PathVariable String airport_code,@RequestBody Airport airport) {
        return service.modify_airport(airport, airport_code);

    }

        @GetMapping("/view")
        public ResponseEntity<?> view() {
        return service.view_all();
    }


    @GetMapping("/viewByAirportCode/{id}")
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> viewByAirportCode(@PathVariable("id") String airport_code)
    {
        return service.view_specific_airport(airport_code);
    }
}
