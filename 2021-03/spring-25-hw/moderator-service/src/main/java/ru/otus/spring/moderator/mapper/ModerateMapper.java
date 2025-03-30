package ru.otus.spring.moderator.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.spring.moderator.domain.Moderate;
import ru.otus.spring.moderator.dto.ModerateDto;

@Component
@RequiredArgsConstructor
public class ModerateMapper implements Mapper<ModerateDto, Moderate> {
    private final ObjectMapper objectMapper;
    @Override
    public ModerateDto toDto(Moderate entity) {
        return objectMapper.convertValue(entity, ModerateDto.class);
    }

    @Override
    public Moderate toEntity(ModerateDto dto) {
        return objectMapper.convertValue(dto, Moderate.class);
    }
}
