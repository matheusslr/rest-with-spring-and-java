package br.com.matheusslr.restwithspringandjava.repository;

import br.com.matheusslr.restwithspringandjava.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

}
