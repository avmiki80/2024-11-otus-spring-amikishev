package ru.otus.spring.book.data;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.otus.spring.book.dto.AuthorDto;

@AllArgsConstructor
@NoArgsConstructor(staticName = "author")
@With
public class AuthorDtoDataBuilder implements TestDataBuilder<AuthorDto> {
    private Long id = 1L;
    private String firstname = "Ivan";
    private String lastname = "Ivanov";
    @Override
    public AuthorDto build() {
        final AuthorDto author = new AuthorDto();
        author.setId(id);
        author.setLastname(lastname);
        author.setFirstname(firstname);
        return author;
    }
}
