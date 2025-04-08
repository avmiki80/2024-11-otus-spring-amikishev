package ru.otus.spring.moderator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;
import ru.otus.spring.moderator.domain.Moderate;
import ru.otus.spring.moderator.dto.CommentDto;
import ru.otus.spring.moderator.dto.CheckedCommentDto;
import ru.otus.spring.moderator.dto.ModerateDto;
import ru.otus.spring.moderator.search.ModerateSearch;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class ModeratorServiceImpl implements ModeratorService{
    private final CrudService<ModerateDto, ModerateSearch> moderateService;
    private static final Integer THRESHOLD = 8;
    @Override
    public CheckedCommentDto moderate(CommentDto commentDto) {
        log.debug(commentDto.getCommentText());
        if(RandomUtils.nextInt(0, 10) < THRESHOLD){
            return CheckedCommentDto.builder().commentStatus(true).build();
        } else {
            moderateService.save(
                    ModerateDto.builder()
                            .commentId(commentDto.getCommentId())
                            .text(commentDto.getCommentText())
                            .build()
            );
            return CheckedCommentDto.builder().reason("Not valid comment: " + commentDto.getCommentText()).commentStatus(false).build();
        }
    }
}
