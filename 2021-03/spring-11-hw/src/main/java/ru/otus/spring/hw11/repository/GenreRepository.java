package ru.otus.spring.hw11.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.spring.hw11.domain.Genre;
import ru.otus.spring.hw11.search.GenreSearch;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GenreRepository implements CommonRepository<Genre, GenreSearch>, CustomRepository<Genre, GenreSearch>{
    @PersistenceContext
    private EntityManager em;
    @Override
    public Genre save(Genre obj) {
        if(Objects.isNull(obj.getId()) || obj.getId().equals(0L)){
            em.persist(obj);
            return obj;
        }
        return em.merge(obj);
    }

    @Override
    public void deleteById(long id) {
        em.createQuery("delete from Genre g where g.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public List<Genre> findAll() {
        return em.createQuery("select g from Genre g", Genre.class).getResultList();
    }

    @Override
    public Optional<Genre> findById(long id) {
        return Optional.ofNullable(em.find(Genre.class, id));
    }
    @Override
    public List<Genre> findByParams(GenreSearch params) {
        return em.createQuery("select g from Genre g where :title is null or :title = '' or lower(g.title) like lower(concat(:title, '%'))")
                .setParameter("title", params.getTitle())
                .getResultList();
    }

    @Override
    public Genre findAndCreateIfAbsent(GenreSearch params) {
        return findByParams(params)
                .stream()
                .findAny().orElseGet(() ->
                        save(new Genre(null, params.getTitle())));
    }
}
