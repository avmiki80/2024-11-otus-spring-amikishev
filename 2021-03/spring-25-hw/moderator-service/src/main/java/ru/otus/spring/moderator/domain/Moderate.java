package ru.otus.spring.moderator.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "moderate_comments")
public class Moderate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text", updatable = false)
    private String text;
    @CreationTimestamp
    @Column(name = "moderation_time", updatable = false)
    private LocalDateTime moderationTime;

    @Column(name = "comment_id", nullable = false, updatable = false)
    private Long commentId;

}
