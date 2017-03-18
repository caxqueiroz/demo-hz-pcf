package com.example;

import com.example.model.Tick;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * Created by cq on 18/3/17.
 */
@RestController
public class MainController {


    @Autowired
    TickRepository tickRepository;

    @PostMapping(produces = "application/json")
    public ResponseEntity<String> process(@RequestBody String value) throws IOException{
        //1461790265397139,9.23263,9.22159,EUR_NOK
        String[] fields= value.split(",");
        Tick t = new Tick();
        t.setTime(Long.valueOf(fields[0]));
        t.setAsk(Double.valueOf(fields[1]));
        t.setBid(Double.valueOf(fields[2]));
        t.setInstrument(fields[3]);
        tickRepository.save(t);
        return new ResponseEntity<>("inserted",HttpStatus.CREATED);
    }

    @GetMapping(path = "/total")
    public ResponseEntity<String> getTotal() throws IOException{

        long total = tickRepository.count();
        String msg = String.format("{\"total\": %1$d}", total);
        return new ResponseEntity<>(msg,HttpStatus.OK);
    }

    @GetMapping(path = "/{tickid}")
    public ResponseEntity<String> getTick(@PathVariable("tickid") Long tickId) throws IOException{
        Tick tick = tickRepository.findOne(tickId);
        return new ResponseEntity<>(tick.toString(),HttpStatus.OK);
    }

    @GetMapping(path = "/instrument/{instrument}")
    public ResponseEntity<String> byInstrument(@PathVariable("instrument") String instrument) throws IOException{
        List<Tick> ticks = tickRepository.findAll(instrument,50,0);
        return new ResponseEntity<>(ticks.toString(),HttpStatus.OK);
    }
}