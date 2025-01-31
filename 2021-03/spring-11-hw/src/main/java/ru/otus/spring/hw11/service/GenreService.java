package ru.otus.spring.hw11.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.hw11.domain.Genre;
import ru.otus.spring.hw11.dto.GenreDto;
import ru.otus.spring.hw11.exception.ServiceException;
import ru.otus.spring.hw11.mapper.Mapper;
import ru.otus.spring.hw11.repository.CommonRepository;
import ru.otus.spring.hw11.search.GenreSearch;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenreService implements CrudService<GenreDto, GenreSearch>{
    private final CommonRepository<Genre, GenreSearch> genreRepository;
    private final Mapper<GenreDto, Genre> genreMapper;

    @Override
    @Transactional
    public GenreDto save(GenreDto obj) {
        Genre genre = genreMapper.toEntity(obj);
        return genreMapper.toDto(genreRepository.save(genre));
    }

    @Override
    public GenreDto update(long id, GenreDto obj) {
        throw new UnsupportedOperationException("The method is not needed now, but must have for REST API in future");
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        try {
            genreRepository.deleteById(id);
        } catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<GenreDto> findAll() {
        return genreRepository.findAll().stream()
                .map(genreMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public GenreDto findById(long id) {
        return genreMapper.toDto(genreRepository.findById(id).orElseThrow(() -> new ServiceException("exception.object-not-found")));
    }
    @Override
    public List<GenreDto> findByParams(GenreSearch params) {
        return genreRepository.findByParams(params).stream()
                .map(genreMapper::toDto)
                .collect(Collectors.toList());
    }
}
