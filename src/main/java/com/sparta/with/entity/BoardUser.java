package com.sparta.with.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

//User : 회원1, 회원2, 회원3
//User -> BoardUser : 회원 1을 Collaborator로 등록

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Table(name = "boardusers")
public class BoardUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collaborator_id", nullable = false)
    private User collaborator;

//    @ManyToMany(mappedBy = "alarmTargets")
//    private List<Alarm> alarms;

    public void updateCollaborator(User newCollaborator) {
        if (this.collaborator != null) {
            this.collaborator.getBoardUsers().remove(this);
        }
        this.collaborator = newCollaborator;
        newCollaborator.getBoardUsers().add(this);
    }
}
