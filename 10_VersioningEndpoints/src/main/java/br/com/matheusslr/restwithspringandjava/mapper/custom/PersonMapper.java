package br.com.matheusslr.restwithspringandjava.mapper.custom;

import br.com.matheusslr.restwithspringandjava.domain.Person;
import br.com.matheusslr.restwithspringandjava.vo.v2.PersonVOV2;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PersonMapper {

    public PersonVOV2 convertEntityToVo(Person person){
        PersonVOV2 personVOV2 = new PersonVOV2();
        personVOV2.setFirstName(person.getFirstName());
        personVOV2.setLastName(person.getLastName());
        personVOV2.setAddress(person.getAddress());
        personVOV2.setGender(person.getGender());
        personVOV2.setBirthDay(new Date());

        return personVOV2;
    }

    public Person convertVoToEntity(PersonVOV2 personVOV2){
        Person person = new Person();
        person.setFirstName(personVOV2.getFirstName());
        person.setLastName(personVOV2.getLastName());
        person.setAddress(personVOV2.getAddress());
        person.setGender(personVOV2.getGender());

        return person;
    }
}
