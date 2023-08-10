package com.sparta.with.repository;

import com.sparta.with.entity.Card;
import com.sparta.with.entity.CardUser;
import com.sparta.with.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardUserRepository extends JpaRepository<CardUser, Long> {
  Optional<CardUser> findByCollaboratorAndCard(User user, Card card);
  Boolean existsByCollaboratorAndCard(User user, Card comment);
  List<CardUser> findByCard_Id(Long id);
}
