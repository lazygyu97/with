package com.sparta.with.dto;

import com.sparta.with.entity.CheckList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckListResponseDto extends ApiResponseDto{
    private long id;
    private String contents;
    private boolean is_checked;

    public static CheckListResponseDto of(CheckList checkList) {
        return CheckListResponseDto.builder()
            .id(checkList.getId())
            .contents(checkList.getContent())
            .is_checked(checkList.isChecked())
            .build();
    }
}