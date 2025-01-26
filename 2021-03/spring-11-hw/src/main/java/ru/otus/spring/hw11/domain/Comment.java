package ru.otus.spring.hw11.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
@NamedEntityGraph(name = "book-graph", attributeNodes = {
        @NamedAttributeNode("book")
})
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text")
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
//    @JoinColumn(name = "book_id", foreignKey = @ForeignKey(name = "fk_comments_books"))
    private Book book;
}
