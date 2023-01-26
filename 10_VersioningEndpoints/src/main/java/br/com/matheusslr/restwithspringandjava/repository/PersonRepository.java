package br.com.matheusslr.restwithspringandjava.repository;

import br.com.matheusslr.restwithspringandjava.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
