package ru.otus.spring.hw11.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.spring.hw11.domain.Author;
import ru.otus.spring.hw11.search.AuthorSearch;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AuthorRepository implements CommonRepository<Author, AuthorSearch>, CustomRepository<Author, AuthorSearch> {
    @PersistenceContext
    private EntityManager em;
    @Override
    public Author save(Author obj) {
        if(Objects.isNull(obj.getId()) || obj.getId().equals(0L)){
            em.persist(obj);
            return obj;
        }
        return em.merge(obj);
    }

    @Override
    public void deleteById(long id) {
        em.createQuery("delete from Author a where a.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public List<Author> findAll() {
        return em.createQuery("select a from Author a", Author.class).getResultList();
    }

    @Override
    public Optional<Author> findById(long id) {
        return Optional.ofNullable(em.find(Author.class, id));    }
    @Override
    public List<Author> findByParams(AuthorSearch params) {
        return em.createQuery("select a from Author a where " +
                        "(:firstname is null or :firstname = '' or lower(a.firstname) like lower(concat(:firstname, '%'))) and " +
                        "(:lastname is null or :lastname = '' or lower(a.lastname) like lower(concat(:lastname, '%')))")
                .setParameter("firstname", params.getFirstname())
                .setParameter("lastname", params.getLastname())
                .getResultList();
    }

    @Override
    public Author findAndCreateIfAbsent(AuthorSearch params) {
        return findByParams(params)
                .stream()
                .findAny()
                .orElseGet(() -> save(new Author(null, params.getFirstname(), params.getLastname())));
    }
}
