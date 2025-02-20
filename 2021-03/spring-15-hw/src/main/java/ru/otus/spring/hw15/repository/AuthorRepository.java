package ru.otus.spring.hw15.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.otus.spring.hw15.domain.Author;
import ru.otus.spring.hw15.repository.custom.AuthorCustomRepository;
import ru.otus.spring.hw15.search.AuthorSearch;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long>, AuthorCustomRepository<Author, AuthorSearch> {
    @Query("select a from Author a where " +
            "(:firstname is null or :firstname = '' or lower(a.firstname) like lower(concat(:firstname, '%'))) and " +
            "(:lastname is null or :lastname = '' or lower(a.lastname) like lower(concat(:lastname, '%')))")
    List<Author> findByParams(@Param("firstname") String firstname, @Param("lastname") String lastname);
}
