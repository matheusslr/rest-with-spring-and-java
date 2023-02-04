package br.com.matheusslr.restwithspringandjava.unittests.mocks.service;

import br.com.matheusslr.restwithspringandjava.domain.Person;
import br.com.matheusslr.restwithspringandjava.exceptions.RequiredObjectIsNullException;
import br.com.matheusslr.restwithspringandjava.repository.PersonRepository;
import br.com.matheusslr.restwithspringandjava.service.PersonService;
import br.com.matheusslr.restwithspringandjava.unittests.mocks.MockPerson;
import br.com.matheusslr.restwithspringandjava.vo.v1.PersonVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class PersonServicesTest {

	MockPerson input;

	PersonRepository repository;
	private PersonService service;


	@BeforeEach
	void setUpMocks() throws Exception {
		input = new MockPerson();
		repository = mock(PersonRepository.class);
		service = new PersonService(repository);
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testFindById() {
		Person entity = input.mockEntity(1);

		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		
		var result = service.findById(entity.getId());
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());

		System.out.println(result.getLinks());

		assertTrue(result.toString().contains("</api/persons/v1/1>;rel=\"self\""));
		assertEquals("Addres Test1", result.getAddress());
		assertEquals("First Name Test1", result.getFirstName());
		assertEquals("Last Name Test1", result.getLastName());
		assertEquals("Female", result.getGender());
	}
	
	@Test
	void testSave() {
		Person entity = input.mockEntity(1); 
		entity.setId(1L);
		
		Person persisted = entity;
		persisted.setId(1L);
		
		PersonVO vo = input.mockVO(1);
		vo.setKey(1L);
		
		when(repository.save(entity)).thenReturn(persisted);
		
		var result = service.save(vo);
		
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		
		assertTrue(result.toString().contains("</api/persons/v1/1>;rel=\"self\""));
		assertEquals("Addres Test1", result.getAddress());
		assertEquals("First Name Test1", result.getFirstName());
		assertEquals("Last Name Test1", result.getLastName());
		assertEquals("Female", result.getGender());
	}
	
	@Test
	void testSaveWithNullPerson() {
		Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
			service.save(null);
		});

		String expectedMessage = "There's not allowed to persist a null object";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}


	@Test
	void testUpdate() {
		Person entity = input.mockEntity(1); 
		
		Person persisted = entity;
		persisted.setId(1L);
		
		PersonVO vo = input.mockVO(1);
		vo.setKey(1L);
		

		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		when(repository.save(entity)).thenReturn(persisted);
		
		var result = service.update(vo);
		
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		
		assertTrue(result.toString().contains("</api/persons/v1/1>;rel=\"self\""));
		assertEquals("Addres Test1", result.getAddress());
		assertEquals("First Name Test1", result.getFirstName());
		assertEquals("Last Name Test1", result.getLastName());
		assertEquals("Female", result.getGender());
	}
	

	
	@Test
	void testUpdateWithNullPerson() {
		Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
			service.update(null);
		});

		String expectedMessage = "There's not allowed to persist a null object";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	void testRemove() {
		Person entity = input.mockEntity(1); 
		entity.setId(1L);
		
		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		
		service.remove(1L);
	}
	
	@Test
	void testFindAll() {
		List<Person> list = input.mockEntityList(); 
		
		when(repository.findAll()).thenReturn(list);
		
		var people = service.findAll();
		
		assertNotNull(people);
		assertEquals(14, people.size());
		
		var personOne = people.get(1);
		
		assertNotNull(personOne);
		assertNotNull(personOne.getKey());
		assertNotNull(personOne.getLinks());
		
		assertTrue(personOne.toString().contains("</api/persons/v1/1>;rel=\"self\""));
		assertEquals("Addres Test1", personOne.getAddress());
		assertEquals("First Name Test1", personOne.getFirstName());
		assertEquals("Last Name Test1", personOne.getLastName());
		assertEquals("Female", personOne.getGender());
		
		var personFour = people.get(4);
		
		assertNotNull(personFour);
		assertNotNull(personFour.getKey());
		assertNotNull(personFour.getLinks());
		
		assertTrue(personFour.toString().contains("</api/persons/v1/4>;rel=\"self\""));
		assertEquals("Addres Test4", personFour.getAddress());
		assertEquals("First Name Test4", personFour.getFirstName());
		assertEquals("Last Name Test4", personFour.getLastName());
		assertEquals("Male", personFour.getGender());
		
		var personSeven = people.get(7);
		
		assertNotNull(personSeven);
		assertNotNull(personSeven.getKey());
		assertNotNull(personSeven.getLinks());
		
		assertTrue(personSeven.toString().contains("</api/persons/v1/7>;rel=\"self\""));
		assertEquals("Addres Test7", personSeven.getAddress());
		assertEquals("First Name Test7", personSeven.getFirstName());
		assertEquals("Last Name Test7", personSeven.getLastName());
		assertEquals("Female", personSeven.getGender());

	}

}
