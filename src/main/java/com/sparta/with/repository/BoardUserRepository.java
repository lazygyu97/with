package com.sparta.with.repository;

import com.sparta.with.entity.BoardUser;
import com.sparta.with.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardUserRepository extends JpaRepository<BoardUser, Long> {
    List<BoardUser> findAll();
    List<BoardUser> findByCollaborator(User collaborator);
    List<BoardUser> findByBoard_Id(Long id);

    boolean existsByBoard_IdAndCollaborator_Id(Long boardId, Long CollaboratorId);
}
