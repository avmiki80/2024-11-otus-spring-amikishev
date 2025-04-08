package ru.otus.spring.book.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.otus.spring.book.dto.CommentDto;
import ru.otus.spring.book.dto.ModerateDto;
import ru.otus.spring.book.exception.ServiceException;

import java.util.Optional;

@Service
@Slf4j
public class ModeratorServiceImpl implements ModeratorService{
    private final RestTemplate restTemplate;
    private final String url;

    public ModeratorServiceImpl(
            @Value("${moderator-url}")String url,
            RestTemplate restTemplate
    ) {
        this.url = url;
        log.info("Moderator service: {}", url);
        this.restTemplate = restTemplate;
    }

    @Override
    public ModerateDto toModerate(CommentDto commentDto) {
        log.debug("Sending comment for moderation: {}", commentDto);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<CommentDto> requestEntity = new HttpEntity<>(commentDto, headers);

            ResponseEntity<ModerateDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    ModerateDto.class
            );
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new ServiceException("Moderation service returned status: " + response.getStatusCode());
            }
            return response.getBody();
        } catch (Exception e) {
            throw new ServiceException("Failed to moderate comment: " +  e.getMessage());
        }
    }
}
