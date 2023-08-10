package com.sparta.with.repository;

import com.sparta.with.entity.redishash.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    Optional<RefreshToken> findByMemberId(Long id);

    void deleteByMemberId(Long id);
}