package com.sparta.with.repository;

import com.sparta.with.entity.redishash.EmailVerification;
import org.springframework.data.repository.CrudRepository;

public interface EmailVerificationRepository extends CrudRepository<EmailVerification, String> {
}
