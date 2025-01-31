package ru.otus.spring.hw11.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.spring.hw11.domain.Book;
import ru.otus.spring.hw11.search.BookSearch;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookRepository implements CommonRepository<Book, BookSearch> {
    @PersistenceContext
    private EntityManager em;
    @Override
    public Book save(Book obj) {
        if(Objects.isNull(obj.getId()) || obj.getId().equals(0L)){
            em.persist(obj);
            return obj;
        } else {
            return em.merge(obj);
        }
    }

    @Override
    public void deleteById(long id) {
        em.createQuery("delete from Book b where b.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public List<Book> findAll() {
        EntityGraph<?> entityGraph = em.getEntityGraph("genre-author-graph");
        TypedQuery<Book> query =  em.createQuery("select b from Book b", Book.class);
        query.setHint("javax.persistence.fetchgraph", entityGraph);
        return query.getResultList();
    }

    @Override
    public Optional<Book> findById(long id) {
        EntityGraph<?> entityGraph = em.getEntityGraph("genre-author-graph");
        TypedQuery<Book> query =  em.createQuery("select b from Book b where b.id = :id", Book.class)
                .setParameter("id", id);
        query.setHint("javax.persistence.fetchgraph", entityGraph);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public List<Book> findByParams(BookSearch params) {
        TypedQuery<Book> query =  em.createQuery("select b from Book b left join fetch b.genre left join fetch b.author where " +
                        "(:title is null or :title = '' or lower(b.title) like lower(concat(:title, '%'))) and " +
                        "(:genre is null or :genre = '' or lower(b.genre.title) like lower(concat(:genre, '%'))) and " +
                        "(:firstname is null or :firstname = '' or lower(b.author.firstname) like lower(concat(:firstname, '%'))) and " +
                        "(:lastname is null or :lastname = '' or lower(b.author.lastname) like lower(concat(:lastname, '%')))"
                        , Book.class)
                .setParameter("title", params.getTitle())
                .setParameter("genre", params.getGenre())
                .setParameter("firstname", params.getFirstname())
                .setParameter("lastname", params.getLastname());
        return query.getResultList();
    }

}
