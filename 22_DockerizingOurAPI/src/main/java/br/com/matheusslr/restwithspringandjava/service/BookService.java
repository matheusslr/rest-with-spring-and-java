package br.com.matheusslr.restwithspringandjava.service;

import br.com.matheusslr.restwithspringandjava.controller.BookController;
import br.com.matheusslr.restwithspringandjava.domain.Book;
import br.com.matheusslr.restwithspringandjava.exceptions.RequiredObjectIsNullException;
import br.com.matheusslr.restwithspringandjava.exceptions.ResourceNotFoundException;
import br.com.matheusslr.restwithspringandjava.mapper.MMapper;
import br.com.matheusslr.restwithspringandjava.repository.BookRepository;
import br.com.matheusslr.restwithspringandjava.vo.v1.BookVO;
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
public class BookService {
    private final BookRepository bookRepository;

    private PagedResourcesAssembler<BookVO> assembler;

    public BookService(BookRepository bookRepository, PagedResourcesAssembler<BookVO> assembler) {
        this.bookRepository = bookRepository;
        this.assembler = assembler;
    }

    public PagedModel<EntityModel<BookVO>> findAll(Pageable pageable) {

        Page<Book> bookPage = bookRepository.findAll(pageable);

        Page<BookVO> bookVOPage = bookPage.map(b -> MMapper.parseObject(b, BookVO.class));
        bookVOPage.map(b -> b.add(linkTo(methodOn(BookController.class).findById(b.getKey())).withSelfRel()));

        Link link = linkTo(methodOn(BookController.class).findAll(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                "asc")).withSelfRel();
        return assembler.toModel(bookVOPage, link);
    }

    public BookVO findById(Long id) {
        BookVO vo = MMapper.parseObject(bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID")), BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public BookVO save(BookVO bookVO) {
        if (bookVO == null) throw new RequiredObjectIsNullException();

        Book saved = MMapper.parseObject(bookVO, Book.class);
        BookVO vo = MMapper.parseObject(bookRepository.save(saved), BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public BookVO update(BookVO bookVO) {
        if (bookVO == null) throw new RequiredObjectIsNullException();

        BookVO bookVO1 = findById(bookVO.getKey());
        bookVO1.setAuthor(bookVO.getAuthor());
        bookVO1.setLaunchDate(bookVO.getLaunchDate());
        bookVO1.setTitle(bookVO.getTitle());
        bookVO1.setPrice(bookVO.getPrice());

        Book toUpdate = MMapper.parseObject(bookVO1, Book.class);
        BookVO vo = MMapper.parseObject(bookRepository.save(toUpdate), BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public void remove(Long id){
        BookVO toRemove = findById(id);
        bookRepository.delete(MMapper.parseObject(toRemove, Book.class));
    }
}
