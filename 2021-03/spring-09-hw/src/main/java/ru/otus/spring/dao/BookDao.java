package ru.otus.spring.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class BookDao implements Dao<Book>{
    private final NamedParameterJdbcOperations jdbc;
    @Override
    public Long insert(Book obj) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("title", obj.getTitle());
        parameterSource.addValue("genreId", obj.getGenre().getId());
        parameterSource.addValue("authorId", obj.getAuthor().getId());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update("insert into books (`title`, `genre_id`, `author_id`) values (:title, :genreId, :authorId)",
                parameterSource, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public void update(Book obj) {
        jdbc.update("update books set `title`=:title, `genre_id`=:genreId, `author_id`=:authorId where `id`=:id",
                Map.of("title", obj.getTitle(), "genreId", obj.getGenre().getId(), "authorId", obj.getAuthor().getId(), "id", obj.getId())
        );
    }

    @Override
    public void deleteById(long id) {
        jdbc.update("delete from books where `id`=:id", Collections.singletonMap("id", id));
    }

    @Override
    public List<Book> findAll() {
        return jdbc.query(
                "select b.id, b.title, b.genre_id, b.author_id, g.title as genre, a.firstname, a.lastname from " +
                        "(books b left join genres g on b.genre_id=g.id) " +
                        "left join authors a on b.author_id=a.id", new BookMapper());
    }

    @Override
    public Book findById(long id) {
        return jdbc.queryForObject(
                "select b.id, b.title, b.genre_id, b.author_id, g.title as genre, a.firstname, a.lastname from " +
                        "(books b left join genres g on b.genre_id=g.id) " +
                        "left join authors a on b.author_id=a.id " +
                        "where b.id=:id", Collections.singletonMap("id", id), new BookMapper());
    }
    private static class BookMapper implements RowMapper<Book> {
        @Override
        public Book mapRow(ResultSet resultSet, int i) throws SQLException {
            return new Book(
                    resultSet.getLong("id"),
                    resultSet.getString("title"),
                    new Genre(resultSet.getLong("genre_id"), resultSet.getString("genre")),
                    new Author(resultSet.getLong("author_id"), resultSet.getString("firstname"), resultSet.getString("lastname"))
            );
        }
    }
}
