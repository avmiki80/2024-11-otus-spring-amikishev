package ru.otus.spring.moderator.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.moderator.domain.Moderate;
import ru.otus.spring.moderator.dto.ModerateDto;
import ru.otus.spring.moderator.exception.ServiceException;
import ru.otus.spring.moderator.mapper.Mapper;
import ru.otus.spring.moderator.repository.ModerateRepository;
import ru.otus.spring.moderator.search.ModerateSearch;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.otus.spring.moderator.util.PageRequestUtil.createPageRequest;

@Service
@RequiredArgsConstructor
public class CrudServiceImpl implements CrudService<ModerateDto, ModerateSearch>{
    private final ModerateRepository moderateRepository;
    private final Mapper<ModerateDto, Moderate> mapper;
    @Override
    @Transactional
    public ModerateDto save(ModerateDto obj) {
        return mapper.toDto(moderateRepository.save(mapper.toEntity(obj)));
    }

    @Override
    public ModerateDto update(long id, ModerateDto obj) {
        throw new UnsupportedOperationException("The method is prohibited");

    }

    @Override
    @Transactional
    public void deleteById(long id) {
        try {
            moderateRepository.deleteById(id);
        } catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<ModerateDto> findAll() {
        return moderateRepository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ModerateDto findById(long id) {
        return mapper.toDto(moderateRepository.findById(id).orElseThrow(() -> new ServiceException("exception.object-not-found")));

    }

    @Override
    public Page<ModerateDto> findByParams(ModerateSearch params) {
        PageRequest pageRequest = createPageRequest(params);
        Page<Moderate> result = moderateRepository.findByParams(
                params.getText(),
                params.getFrom(),
                params.getTo(),
                pageRequest
        );
        return new PageImpl<>(
                result.getContent().stream().map(mapper::toDto).collect(Collectors.toList()),
                pageRequest,
                result.getTotalElements()
        );
    }
}
