package br.com.matheusslr.restwithspringandjava.unittests.mocks;

import br.com.matheusslr.restwithspringandjava.domain.Book;
import br.com.matheusslr.restwithspringandjava.vo.v1.BookVO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MockBook {


    public Book mockEntity() {
        return mockEntity(0);
    }
    
    public BookVO mockVO() {
        return mockVO(0);
    }
    
    public List<Book> mockEntityList() {
        List<Book> books = new ArrayList<Book>();
        for (int i = 0; i < 14; i++) {
            books.add(mockEntity(i));
        }
        return books;
    }

    public List<BookVO> mockVOList() {
        List<BookVO> books = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            books.add(mockVO(i));
        }
        return books;
    }
    
    public Book mockEntity(Integer number) {
        Book book = new Book();
        book.setId(Long.valueOf(number));
        book.setAuthor("Author Test" + number);
        book.setTitle("Title Test" + number);
        book.setLaunchDate(new Date());
        book.setPrice(54.0);
        return book;
    }

    public BookVO mockVO(Integer number) {
        BookVO vo = new BookVO();
        vo.setKey(number.longValue());
        vo.setAuthor("Author Test" + number);
        vo.setTitle("Title Test" + number);
        vo.setLaunchDate(new Date());
        vo.setPrice(54.0);
        return vo;
    }

}
