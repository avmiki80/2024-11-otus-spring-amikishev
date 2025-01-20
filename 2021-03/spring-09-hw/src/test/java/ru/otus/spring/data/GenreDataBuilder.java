package ru.otus.spring.data;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.otus.spring.domain.Genre;
@AllArgsConstructor
@NoArgsConstructor(staticName = "genre")
@With
public class GenreDataBuilder implements TestDataBuilder<Genre>{
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
