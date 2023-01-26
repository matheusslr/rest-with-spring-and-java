package br.com.matheusslr.restwithspringandjava.service.v1;

import br.com.matheusslr.restwithspringandjava.domain.Person;
import br.com.matheusslr.restwithspringandjava.exceptions.ResourceNotFoundException;
import br.com.matheusslr.restwithspringandjava.mapper.MMapper;
import br.com.matheusslr.restwithspringandjava.repository.PersonRepository;
import br.com.matheusslr.restwithspringandjava.vo.v1.PersonVO;
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

    public List<PersonVO> findAll() {
        log.info("[INFO] Finding all people!");
        return MMapper.parseListObjects(personRepository.findAll(), PersonVO.class);
    }

    public PersonVO findById(Long id) {
        log.info("[INFO] Find one person");
        return MMapper.parseObject(personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID")), PersonVO.class);
    }

    public PersonVO save(PersonVO personVO) {
        log.info("[INFO] Saving one person");

        Person person = MMapper.parseObject(personVO, Person.class);
        return MMapper.parseObject(personRepository.save(person), PersonVO.class);
    }

    public PersonVO update(PersonVO personVO) {
        log.info("[INFO] Updating one person");

        PersonVO personToUpdate = findById(personVO.getId());
        personToUpdate.setFirstName(personVO.getFirstName());
        personToUpdate.setLastName(personVO.getLastName());
        personToUpdate.setAddress(personVO.getAddress());
        personToUpdate.setAddress(personVO.getAddress());

        Person person = MMapper.parseObject(personToUpdate, Person.class);

        return MMapper.parseObject(personRepository.save(person), PersonVO.class);
    }

    public void remove(Long id) {
        log.info("[INFO] Deleting one person");

        Person person = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

        personRepository.delete(person);

    }
}
