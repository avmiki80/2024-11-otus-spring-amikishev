package ru.otus.spring.hw18.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.otus.spring.hw18.domain.Genre;
import ru.otus.spring.hw18.repository.custom.GenreCustomRepository;
import ru.otus.spring.hw18.search.GenreSearch;

import java.util.List;


@Repository
public interface GenreRepository extends JpaRepository<Genre, Long>, GenreCustomRepository<Genre, GenreSearch> {
    @Query("select g from Genre g where :title is null or :title = '' or lower(g.title) like lower(concat(:title, '%'))")
    List<Genre> findByParams(@Param("title") String title);
}
