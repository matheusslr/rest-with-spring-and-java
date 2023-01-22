package br.com.matheusslr.restwithspringandjava.controller;

import br.com.matheusslr.restwithspringandjava.entity.Greetings;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/greetings")
public class GreetingsController {
    private static final String template = "Hello, %s!";

    @GetMapping
    public ResponseEntity<Greetings> greetings(@RequestParam(value = "name", defaultValue = "World") String name){
        return ResponseEntity.ok(new Greetings(0, String.format(template, name)));
    }
}
