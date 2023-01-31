package br.com.matheusslr.restwithspringandjava.service;

import br.com.matheusslr.restwithspringandjava.controller.PersonController;
import br.com.matheusslr.restwithspringandjava.domain.Person;
import br.com.matheusslr.restwithspringandjava.exceptions.RequiredObjectIsNullException;
import br.com.matheusslr.restwithspringandjava.exceptions.ResourceNotFoundException;
import br.com.matheusslr.restwithspringandjava.mapper.MMapper;
import br.com.matheusslr.restwithspringandjava.repository.PersonRepository;
import br.com.matheusslr.restwithspringandjava.vo.v1.PersonVO;
import lombok.extern.log4j.Log4j2;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
        List<PersonVO> personVOList = MMapper.parseListObjects(personRepository.findAll(), PersonVO.class);
        personVOList.stream()
                .forEach(p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel()));
        return personVOList;
    }

    public PersonVO findById(Long id) {
        log.info("[INFO] Find one person");

        PersonVO vo = MMapper.parseObject(personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID")), PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return vo;
    }

    public PersonVO save(PersonVO personVO) {
        if (personVO == null) throw new RequiredObjectIsNullException();

        Person person = MMapper.parseObject(personVO, Person.class);
        PersonVO vo = MMapper.parseObject(personRepository.save(person), PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public PersonVO update(PersonVO personVO) {
        if (personVO == null) throw new RequiredObjectIsNullException();

        PersonVO personToUpdate = findById(personVO.getKey());
        personToUpdate.setFirstName(personVO.getFirstName());
        personToUpdate.setLastName(personVO.getLastName());
        personToUpdate.setAddress(personVO.getAddress());
        personToUpdate.setAddress(personVO.getAddress());

        Person person = MMapper.parseObject(personToUpdate, Person.class);

        PersonVO vo = MMapper.parseObject(personRepository.save(person), PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public void remove(Long id) {
        log.info("[INFO] Deleting one person");

        Person person = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

        personRepository.delete(person);
    }
}
