package ru.otus.spring.book.data;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.otus.spring.book.dto.CommentDto;

@AllArgsConstructor
@NoArgsConstructor(staticName = "comment")
@With
public class CommentDtoDataBuilder implements TestDataBuilder<CommentDto> {
    private Long id = 1L;
    private String text = "CommentTest1";
    private String bookTitle = "Test1";
    @Override
    public CommentDto build() {
        final CommentDto comment = new CommentDto();
        comment.setId(id);
        comment.setText(text);
        comment.setBookTitle(bookTitle);
        return comment;
    }
}
