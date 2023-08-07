package com.sparta.with.entity;

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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Table(name = "cards")
public class Card extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id", nullable = false)
    private Area area;

    @Builder.Default
    @OneToMany(mappedBy = "card",orphanRemoval = true)
    private List<CardUser> cardUsers = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "card",orphanRemoval = true)
    private List<CheckList> checkLists = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "card",orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Column(nullable = false)
    private String content;

    @Column
    private LocalDateTime startDate;

    @Column
    private LocalDateTime dueDate;

    @Column
    private String color;

    @Column
    private String image;

}
