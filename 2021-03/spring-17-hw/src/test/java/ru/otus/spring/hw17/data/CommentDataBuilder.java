package ru.otus.spring.hw17.data;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.otus.spring.hw17.domain.Book;
import ru.otus.spring.hw17.domain.Comment;
@AllArgsConstructor
@NoArgsConstructor(staticName = "comment")
@With
public class CommentDataBuilder implements TestDataBuilder<Comment> {
    private Long id = 1L;
    private String text = "CommentTest1";
    private Book book = Book.builder().build();
    @Override
    public Comment build() {
        final Comment comment = new Comment();
        comment.setId(id);
        comment.setText(text);
        comment.setBook(book);
        return comment;
    }
}
