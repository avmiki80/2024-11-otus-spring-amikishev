package ru.otus.spring.moderator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckedCommentDto {
    private Boolean commentStatus;
    private String reason;
}
