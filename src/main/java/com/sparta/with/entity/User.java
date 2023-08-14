package com.sparta.with.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

import lombok.*;

//User : 회원1, 회원2, 회원3

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Table(name = "users")
@EqualsAndHashCode(of="id")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String image;

    @Builder.Default
    @OneToMany(mappedBy = "collaborator", orphanRemoval = true)
    private List<BoardUser> boardUsers = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "collaborator", orphanRemoval = true)
    private List<CardUser> cardUsers = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "author", orphanRemoval = true)
    private List<Board> boards = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "author", orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();


    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    public void updateImage(String image) {
        this.image = image;
    }

    public User update(String nickname, String profileImageUrl) {
        this.nickname = nickname;
        this.image = profileImageUrl;
        return this;
    }
}