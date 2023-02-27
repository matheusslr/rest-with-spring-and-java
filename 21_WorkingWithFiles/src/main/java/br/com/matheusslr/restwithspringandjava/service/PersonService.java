package br.com.matheusslr.restwithspringandjava.service;

import br.com.matheusslr.restwithspringandjava.controller.PersonController;
import br.com.matheusslr.restwithspringandjava.domain.Person;
import br.com.matheusslr.restwithspringandjava.exceptions.RequiredObjectIsNullException;
import br.com.matheusslr.restwithspringandjava.exceptions.ResourceNotFoundException;
import br.com.matheusslr.restwithspringandjava.mapper.MMapper;
import br.com.matheusslr.restwithspringandjava.repository.PersonRepository;
import br.com.matheusslr.restwithspringandjava.vo.v1.PersonVO;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@Log4j2
public class PersonService {
    private final PersonRepository personRepository;

    private PagedResourcesAssembler<PersonVO> assembler;

    public PersonService(PersonRepository personRepository, PagedResourcesAssembler<PersonVO> assembler) {
        this.personRepository = personRepository;
        this.assembler = assembler;
    }

    public PagedModel<EntityModel<PersonVO>> findAll(Pageable pageable) {
        log.info("[INFO] Finding all people!");

        Page<Person> personsPage = personRepository.findAll(pageable);

        Page<PersonVO> personVOPage = personsPage.map(p -> MMapper.parseObject(p, PersonVO.class));
        personVOPage.map(p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel()));

        Link link = linkTo(methodOn(PersonController.class).findAll(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                "asc")).withSelfRel();
        return assembler.toModel(personVOPage, link);
    }

    public PagedModel<EntityModel<PersonVO>> findPersonsByName(String firstname, Pageable pageable) {
        log.info("[INFO] Finding all people!");

        Page<Person> personsPage = personRepository.findPersonsByName(firstname, pageable);

        Page<PersonVO> personVOPage = personsPage.map(p -> MMapper.parseObject(p, PersonVO.class));
        personVOPage.map(p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel()));

        Link link = linkTo(methodOn(PersonController.class).findAll(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                "asc")).withSelfRel();
        return assembler.toModel(personVOPage, link);
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

    @Transactional
    public PersonVO disablePerson(Long id) {
        log.info("[INFO] Disabling one person");

        personRepository.disablePerson(id);

        PersonVO vo = MMapper.parseObject(personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID")), PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return vo;
    }

    public void remove(Long id) {
        log.info("[INFO] Deleting one person");

        Person person = personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

        personRepository.delete(person);
    }
}
