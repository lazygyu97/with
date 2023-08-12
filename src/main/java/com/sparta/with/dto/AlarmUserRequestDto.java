package com.sparta.with.dto;

import com.sparta.with.entity.Alarm;
import com.sparta.with.entity.AlarmUser;
import com.sparta.with.entity.Board;
import com.sparta.with.entity.BoardUser;
import com.sparta.with.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AlarmUserRequestDto {
    private Long userId;

    public static AlarmUser toEntity(User alarmTarget, Alarm alarm) {
        return AlarmUser.builder()
            .id(alarmTarget.getId())
            .alarmTarget(alarmTarget)
            .alarm(alarm)
            .build();
    }
}
