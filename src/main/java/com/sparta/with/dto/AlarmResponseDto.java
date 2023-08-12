package com.sparta.with.dto;

import com.sparta.with.entity.Alarm;
import com.sparta.with.entity.AlarmUser;
import com.sparta.with.entity.Board;
import com.sparta.with.entity.Card;
import com.sparta.with.entity.Comment;
import com.sparta.with.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AlarmResponseDto {
    private Long id;
    private String commentWriter;
    private String commentContent;
    private String cardWriter; // comment가 달린 card의 작성자
    private String cardTitle; // comment가 달린 card의 제목
    private String alarmMessage;
    private List<AlarmUserResponseDto> alarmUsers;

    public AlarmResponseDto of(Alarm alarm, Comment comment) {
        return AlarmResponseDto.builder()
            .commentWriter(comment.getAuthor().getNickname())
            .commentContent(comment.getContent())
            .cardWriter(comment.getCard().getAuthor().getNickname())
            .cardTitle(comment.getCard().getTitle())
            .alarmMessage(alarm.getAlarmMessage())
            .alarmUsers(alarm.getAlarmUsers().stream().map(AlarmUserResponseDto::of).toList())
            .build();
    }
}
