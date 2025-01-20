package ru.otus.spring.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Author;
import ru.otus.spring.search.AuthorSearch;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class AuthorDao implements Dao<Author, AuthorSearch>{
    private final NamedParameterJdbcOperations jdbc;
    @Override
    public Long insert(Author obj) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("firstname", obj.getFirstname());
        parameterSource.addValue("lastname", obj.getLastname());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update("insert into authors (`firstname`, `lastname`) values (:firstname, :lastname)",
                parameterSource, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public void update(Author obj) {
        jdbc.update("update authors set `firstname`=:firstname, `lastname`=:lastname where `id`=:id",
                Map.of("firstname", obj.getFirstname(), "lastname", obj.getLastname(),"id", obj.getId())
        );
    }

    @Override
    public void deleteById(long id) {
        jdbc.update("delete from authors where `id`=:id", Collections.singletonMap("id", id));
    }

    @Override
    public List<Author> findAll() {
        return jdbc.query("select a.id, a.firstname, a.lastname from authors a", new AuthorMapper());
    }

    @Override
    public Author findById(long id) {
        return jdbc.queryForObject("select a.id, a.firstname, a.lastname from authors a where id=:id", Collections.singletonMap("id", id), new AuthorMapper());
    }

    @Override
    public List<Author> findByParams(AuthorSearch params) {
        return jdbc.query("select a.id, a.firstname, a.lastname from authors a where " +
                        "(:firstname = '' or lower(firstname) like lower(concat(:firstname, '%'))) and " +
                        "(:lastname = '' or lower(lastname) like lower(concat(:lastname, '%')))",
                Map.of(
                        "firstname", Objects.isNull(params.getFirstname()) ? "" : params.getFirstname(),
                        "lastname", Objects.isNull(params.getLastname()) ? "" : params.getLastname()),
                new AuthorMapper());
    }
    private static class AuthorMapper implements RowMapper<Author> {
        @Override
        public Author mapRow(ResultSet resultSet, int i) throws SQLException {
            return new Author(
                    resultSet.getLong("id"),
                    resultSet.getString("firstname"),
                    resultSet.getString("lastname")
            );
        }
    }
}
