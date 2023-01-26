package br.com.matheusslr.restwithspringandjava.controller;

import br.com.matheusslr.restwithspringandjava.service.PersonService;
import br.com.matheusslr.restwithspringandjava.vo.v1.PersonVO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/persons/v1")
public class PersonController {
    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PersonVO>> findAll() {
        return ResponseEntity.ok(personService.findAll());
    }

    @GetMapping(path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PersonVO> findById(@PathVariable(value = "id") Long id) {
        return ResponseEntity.ok(personService.findById(id));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PersonVO> save(@RequestBody PersonVO personVO) {
        return new ResponseEntity<>(personService.save(personVO), HttpStatus.CREATED);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PersonVO> update(@RequestBody PersonVO personVO) {
        return new ResponseEntity<>(personService.update(personVO), HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable(value = "id") Long id) {
        personService.remove(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
