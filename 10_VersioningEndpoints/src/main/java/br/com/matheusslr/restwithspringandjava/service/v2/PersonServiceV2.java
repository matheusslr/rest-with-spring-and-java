package br.com.matheusslr.restwithspringandjava.service.v2;

import br.com.matheusslr.restwithspringandjava.domain.Person;
import br.com.matheusslr.restwithspringandjava.exceptions.ResourceNotFoundException;
import br.com.matheusslr.restwithspringandjava.mapper.MMapper;
import br.com.matheusslr.restwithspringandjava.mapper.custom.PersonMapper;
import br.com.matheusslr.restwithspringandjava.repository.PersonRepository;
import br.com.matheusslr.restwithspringandjava.vo.v2.PersonVOV2;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class PersonServiceV2 {
    private final PersonRepository personRepository;

    private final PersonMapper personMapper;

    public PersonServiceV2(PersonRepository personRepository, PersonMapper personMapper) {
        this.personRepository = personRepository;
        this.personMapper = personMapper;
    }

    public List<PersonVOV2> findAll() {
        log.info("[INFO] Finding all people!");
        return MMapper.parseListObjects(personRepository.findAll(), PersonVOV2.class);
    }

    public PersonVOV2 findById(Long id) {
        log.info("[INFO] Find one person");
        return MMapper.parseObject(personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID")), PersonVOV2.class);
    }

    public PersonVOV2 save(PersonVOV2 personVO) {
        log.info("[INFO] Saving one person");

        Person person = personMapper.convertVoToEntity(personVO);
        return personMapper.convertEntityToVo(personRepository.save(person));
    }

    public PersonVOV2 update(PersonVOV2 personVO) {
        log.info("[INFO] Updating one person");

        PersonVOV2 personToUpdate = findById(personVO.getId());
        personToUpdate.setFirstName(personVO.getFirstName());
        personToUpdate.setLastName(personVO.getLastName());
        personToUpdate.setAddress(personVO.getAddress());
        personToUpdate.setAddress(personVO.getAddress());

        Person person = personMapper.convertVoToEntity(personToUpdate);

        return personMapper.convertEntityToVo(personRepository.save(person));
    }

    public void remove(Long id) {
        log.info("[INFO] Deleting one person");

        Person person = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

        personRepository.delete(person);
    }
}
