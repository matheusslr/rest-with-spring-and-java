package br.com.matheusslr.restwithspringandjava.unittests.mocks.service;

import br.com.matheusslr.restwithspringandjava.domain.Book;
import br.com.matheusslr.restwithspringandjava.exceptions.RequiredObjectIsNullException;
import br.com.matheusslr.restwithspringandjava.repository.BookRepository;
import br.com.matheusslr.restwithspringandjava.service.BookService;
import br.com.matheusslr.restwithspringandjava.vo.v1.BookVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import br.com.matheusslr.restwithspringandjava.unittests.mocks.MockBook;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class BookServicesTest {

	MockBook input;

	BookRepository repository;
	private BookService service;


	@BeforeEach
	void setUpMocks() throws Exception {
		input = new MockBook();
		repository = mock(BookRepository.class);
//		service = new BookService(repository);
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testFindById() {
		Book entity = input.mockEntity(1);

		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		
		var result = service.findById(entity.getId());
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		assertNotNull(result.getLaunchDate());

		System.out.println(result.getLinks());

		assertTrue(result.toString().contains("</api/books/v1/1>;rel=\"self\""));
		assertEquals("Author Test1", result.getAuthor());
		assertEquals("Title Test1", result.getTitle());
		assertEquals(54.0, result.getPrice());
	}
	
	@Test
	void testSave() {
		Book entity = input.mockEntity(1); 
		entity.setId(1L);
		
		Book persisted = entity;
		persisted.setId(1L);
		
		BookVO vo = input.mockVO(1);
		vo.setKey(1L);
		
		when(repository.save(entity)).thenReturn(persisted);
		
		var result = service.save(vo);
		
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		assertNotNull(result.getLaunchDate());

		assertTrue(result.toString().contains("</api/books/v1/1>;rel=\"self\""));
		assertEquals("Author Test1", result.getAuthor());
		assertEquals("Title Test1", result.getTitle());
		assertEquals(54.0, result.getPrice());
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
		Book entity = input.mockEntity(1); 
		
		Book persisted = entity;
		persisted.setId(1L);
		
		BookVO vo = input.mockVO(1);
		vo.setKey(1L);
		

		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		when(repository.save(entity)).thenReturn(persisted);
		
		var result = service.update(vo);
		
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		assertNotNull(result.getLaunchDate());

		assertTrue(result.toString().contains("</api/books/v1/1>;rel=\"self\""));
		assertEquals("Author Test1", result.getAuthor());
		assertEquals("Title Test1", result.getTitle());
		assertEquals(54.0, result.getPrice());
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
		Book entity = input.mockEntity(1); 
		entity.setId(1L);
		
		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		
		service.remove(1L);
	}
	
//	@Test
//	void testFindAll() {
//		List<Book> list = input.mockEntityList();
//
//		when(repository.findAll()).thenReturn(list);
//
//		var people = service.findAll();
//
//		assertNotNull(people);
//		assertEquals(14, people.size());
//
//		var bookOne = people.get(1);
//
//		assertNotNull(bookOne);
//		assertNotNull(bookOne.getKey());
//		assertNotNull(bookOne.getLinks());
//		assertNotNull(bookOne.getLaunchDate());
//
//		assertTrue(bookOne.toString().contains("</api/books/v1/1>;rel=\"self\""));
//		assertEquals("Author Test1", bookOne.getAuthor());
//		assertEquals("Title Test1", bookOne.getTitle());
//		assertEquals(54.0, bookOne.getPrice());
//
//		var bookFour = people.get(4);
//
//		assertNotNull(bookFour);
//		assertNotNull(bookFour.getKey());
//		assertNotNull(bookFour.getLinks());
//		assertNotNull(bookFour.getLaunchDate());
//
//		assertTrue(bookFour.toString().contains("</api/books/v1/4>;rel=\"self\""));
//		assertEquals("Author Test4", bookFour.getAuthor());
//		assertEquals("Title Test4", bookFour.getTitle());
//		assertEquals(54.0, bookFour.getPrice());
//
//		var bookSeven = people.get(7);
//
//		assertNotNull(bookSeven);
//		assertNotNull(bookSeven.getKey());
//		assertNotNull(bookSeven.getLinks());
//		assertNotNull(bookSeven.getLaunchDate());
//
//		assertTrue(bookSeven.toString().contains("</api/books/v1/7>;rel=\"self\""));
//		assertEquals("Author Test7", bookSeven.getAuthor());
//		assertEquals("Title Test7", bookSeven.getTitle());
//		assertEquals(54.0, bookSeven.getPrice());
//
//	}

}
