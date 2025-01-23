package ru.otus.spring.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.search.GenreSearch;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class GenreDao implements Dao<Genre, GenreSearch>{
    private final NamedParameterJdbcOperations jdbc;
    @Override
    public Long insert(Genre obj) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("title", obj.getTitle());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update("insert into genres (`title`) values (:title)", parameterSource, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public void update(Genre obj) {
        jdbc.update("update genres set `title`=:title where `id`=:id",
                Map.of("title", obj.getTitle(), "id", obj.getId())
        );
    }

    @Override
    public void deleteById(long id) {
        jdbc.update("delete from genres where `id`=:id", Collections.singletonMap("id", id));
    }

    @Override
    public List<Genre> findAll() {
        return jdbc.query("select g.id, g.title from genres g", new GenreMapper());
    }

    @Override
    public Genre findById(long id) {
        return jdbc.queryForObject("select g.id, g.title from genres g where id=:id", Collections.singletonMap("id", id), new GenreMapper());
    }
    @Override
    public List<Genre> findByParams(GenreSearch param) {
        return jdbc.query("select g.id, g.title from genres g where :title = '' or lower(title) like lower(concat(:title, '%'))",
                Collections.singletonMap("title", Objects.isNull(param.getTitle()) ? "" : param.getTitle()), new GenreMapper());
    }
    private static class GenreMapper implements RowMapper<Genre> {
        @Override
        public Genre mapRow(ResultSet resultSet, int i) throws SQLException {
            return new Genre(resultSet.getLong("id"), resultSet.getString("title"));
        }
    }
}
