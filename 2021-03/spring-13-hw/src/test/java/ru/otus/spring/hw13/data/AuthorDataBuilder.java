package ru.otus.spring.hw13.data;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.otus.spring.hw13.domain.Author;

@AllArgsConstructor
@NoArgsConstructor(staticName = "author")
@With
public class AuthorDataBuilder implements TestDataBuilder<Author> {
    private Long id = 1L;
    private String firstname = "Ivan";
    private String lastname = "Ivanov";
    @Override
    public Author build() {
        final Author author = new Author();
        author.setId(id);
        author.setLastname(lastname);
        author.setFirstname(firstname);
        return author;
    }
}
