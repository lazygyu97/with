package com.sparta.with.entity;


import com.sparta.with.dto.UserResponseDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Table(name = "comments")
public class Comment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @Column(nullable = false)
    private String content;

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateAlarm(ApplicationEventPublisher eventPublisher) {
        eventPublisher.publishEvent(
            Alarm.builder()
                .cardWriter(UserResponseDto.of(getCard().getAuthor()).toString())
                .cardTitle(getCard().getTitle())
                .commentWriter(UserResponseDto.of(getCard().getAuthor()).toString())
                .commentContent(getContent())
                .alarmMessage(String.format("댓글 작성 알림 : %s가 '%s'를 '%s'에 작성하였습니다.", getAuthor().getNickname(), getContent(), getCard().getTitle()))
                .build()
        );
    }

//    public void updateAlarm(ApplicationEventPublisher eventPublisher, String alarmMessage) {
//        eventPublisher.publishEvent(Alarm.createFromComment(this, alarmMessage));
//    }
}
