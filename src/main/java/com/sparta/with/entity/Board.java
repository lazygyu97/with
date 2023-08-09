package com.sparta.with.entity;

import com.sparta.with.dto.BoardRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/* Board (보드, 칸반 보드) : 렌더링되는 협업 화면 중 가장 큰 단위 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "boards")
public class Board extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Column(nullable = false)
    private String name;

    @Column
    private String color;

    @Column
    private String info;

    @Builder.Default
    @OneToMany(mappedBy = "board", orphanRemoval = true)
    private List<BoardUser> boardUsers = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "board", orphanRemoval = true)
    private List<Area> areas = new ArrayList<>();

    public Board(BoardRequestDto boardRequestDto, User author) {
        this.name = boardRequestDto.getName();
        this.color = boardRequestDto.getColor();
        this.info = boardRequestDto.getInfo();
        this.author = author;
    }

    public void updateName(BoardRequestDto boardRequestDto) {
        this.name = boardRequestDto.getName();
    }

    public void updateColor(BoardRequestDto boardRequestDto) {
        this.color = boardRequestDto.getColor();
    }

    public void updateInfo(BoardRequestDto boardRequestDto) {
        this.info = boardRequestDto.getInfo();
    }
}
