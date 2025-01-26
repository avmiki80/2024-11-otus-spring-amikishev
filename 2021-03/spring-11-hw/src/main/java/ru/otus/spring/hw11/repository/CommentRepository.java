package ru.otus.spring.hw11.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.spring.hw11.domain.Comment;
import ru.otus.spring.hw11.search.CommentSearch;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
@Repository
@RequiredArgsConstructor
public class CommentRepository implements CommonRepository<Comment, CommentSearch> {
    @PersistenceContext
    private EntityManager em;
    @Override
    public Comment save(Comment obj) {
        if(Objects.isNull(obj.getId()) || obj.getId().equals(0L)){
            em.persist(obj);
            return obj;
        }
        return em.merge(obj);
    }

    @Override
    public void deleteById(long id) {
        em.createQuery("delete from Comment с where с.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public List<Comment> findAll() {
        return em.createQuery("select c from Comment c left join fetch c.book", Comment.class).getResultList();
    }

    @Override
    public Optional<Comment> findById(long id) {
        EntityGraph<?> entityGraph = em.getEntityGraph("book-graph");
        TypedQuery<Comment> query =  em.createQuery("select c from Comment c where c.id = :id", Comment.class)
                .setParameter("id", id);
        query.setHint("javax.persistence.fetchgraph", entityGraph);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public List<Comment> findByParams(CommentSearch params) {
        return em.createQuery("select c from Comment c left join fetch c.book where " +
                        "(:bookIds is null or c.book.id in(:bookIds)) and " +
                        "(:text is null or :text = '' or lower(c.text) like lower(concat('%', :text, '%'))) and " +
                        "(:bookTitle is null or :bookTitle = '' or lower(c.book.title) like lower(concat(:bookTitle, '%')))")
                .setParameter("text", params.getText())
                .setParameter("bookTitle", params.getBookTitle())
                .setParameter("bookIds", params.getBookIds())
                .getResultList();
    }

}
