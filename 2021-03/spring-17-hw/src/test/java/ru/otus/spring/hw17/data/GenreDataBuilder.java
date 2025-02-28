package ru.otus.spring.hw17.data;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.otus.spring.hw17.domain.Genre;
@AllArgsConstructor
@NoArgsConstructor(staticName = "genre")
@With
public class GenreDataBuilder implements TestDataBuilder<Genre> {
    private Long id = 1L;
    private String title = "title";
    @Override
    public Genre build() {
        final Genre genre = new Genre();
        genre.setId(id);
        genre.setTitle(title);
        return genre;
    }
}
