package br.com.matheusslr.restwithspringandjava.service;

import br.com.matheusslr.restwithspringandjava.domain.Person;
import br.com.matheusslr.restwithspringandjava.exceptions.ResourceNotFoundException;
import br.com.matheusslr.restwithspringandjava.repository.PersonRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class PersonService {
    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> findAll() {
        log.info("[INFO] Finding all people!");
        return personRepository.findAll();
    }

    public Person findById(Long id) {
        log.info("[INFO] Find one person");
        return personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
    }

    public Person save(Person person){
        log.info("[INFO] Saving one person");
        return personRepository.save(person);
    }

    public Person update(Person person){
        log.info("[INFO] Updating one person");

        Person personToUpdate = findById(person.getId());
        personToUpdate.setFirstName(person.getFirstName());
        personToUpdate.setLastName(person.getLastName());
        personToUpdate.setAddress(person.getAddress());
        personToUpdate.setAddress(person.getAddress());

        return personRepository.save(personToUpdate);
    }

    public void remove(Long id){
        log.info("[INFO] Deleting one person");
        personRepository.delete(findById(id));
    }
}
