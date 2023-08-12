package com.sparta.with.dto;

import com.sparta.with.entity.AlarmUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AlarmUserResponseDto {
    private String alarmTarget;

    public static AlarmUserResponseDto of(AlarmUser alarmUser) {
        return AlarmUserResponseDto.builder()
            .alarmTarget(alarmUser.getAlarmTarget().toString())
            .build();
    }
}
