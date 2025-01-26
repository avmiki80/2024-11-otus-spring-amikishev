package ru.otus.spring.hw11.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@ToString(exclude = "comments")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "books")
@NamedEntityGraph(name = "genre-author-graph", attributeNodes = {
        @NamedAttributeNode("genre"), @NamedAttributeNode("author")
})
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id")
//    @JoinColumn(name = "genre_id", foreignKey = @ForeignKey(name = "fk_books_genres"))
    private Genre genre;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
//    @JoinColumn(name = "author_id", foreignKey = @ForeignKey(name = "fk_books_authors"))
    private Author author;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "book")
    private List<Comment> comments;
}
