package br.com.matheusslr.restwithspringandjava.controller.v2;

import br.com.matheusslr.restwithspringandjava.service.v2.PersonServiceV2;
import br.com.matheusslr.restwithspringandjava.vo.v2.PersonVOV2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/persons/v2")
public class PersonControllerV2 {
    private final PersonServiceV2 personService;

    public PersonControllerV2(PersonServiceV2 personService) {
        this.personService = personService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PersonVOV2>> findAll() {
        return ResponseEntity.ok(personService.findAll());
    }

    @GetMapping(path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PersonVOV2> findById(@PathVariable(value = "id") Long id) {
        return ResponseEntity.ok(personService.findById(id));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PersonVOV2> save(@RequestBody PersonVOV2 personVO) {
        return new ResponseEntity<>(personService.save(personVO), HttpStatus.CREATED);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PersonVOV2> update(@RequestBody PersonVOV2 personVO) {
        return new ResponseEntity<>(personService.update(personVO), HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable(value = "id") Long id) {
        personService.remove(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
