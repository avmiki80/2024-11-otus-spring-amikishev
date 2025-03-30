package ru.otus.spring.moderator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {
    @JsonProperty("text")
    private String commentText;
    @JsonProperty("id")
    private Long commentId;
}
