package ru.otus.spring.hw11.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.hw11.domain.Author;
import ru.otus.spring.hw11.dto.AuthorDto;
import ru.otus.spring.hw11.exception.ServiceException;
import ru.otus.spring.hw11.mapper.Mapper;
import ru.otus.spring.hw11.repository.CommonRepository;
import ru.otus.spring.hw11.search.AuthorSearch;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorService implements CrudService<AuthorDto, AuthorSearch>{
    private final CommonRepository<Author, AuthorSearch> authorRepository;
    private final Mapper<AuthorDto, Author> authorMapper;
    @Override
    @Transactional
    public AuthorDto save(AuthorDto obj) {
        Author author = authorMapper.toEntity(obj);
        return authorMapper.toDto(authorRepository.save(author));
    }

    @Override
//    @Transactional
    public AuthorDto update(long id, AuthorDto obj) {
        throw new UnsupportedOperationException("The method is not needed now, but must have for REST API in future");
    }


    @Override
    @Transactional
    public void deleteById(long id) {
        try {
            authorRepository.deleteById(id);
        } catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<AuthorDto> findAll() {
        return authorRepository.findAll().stream()
                .map(authorMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public AuthorDto findById(long id) {
        return authorMapper.toDto(authorRepository.findById(id).orElseThrow(() -> new ServiceException("exception.object-not-found")));
    }
    @Override
    public List<AuthorDto> findByParams(AuthorSearch params) {
        return authorRepository.findByParams(params).stream()
                .map(authorMapper::toDto)
                .collect(Collectors.toList());
    }
}
