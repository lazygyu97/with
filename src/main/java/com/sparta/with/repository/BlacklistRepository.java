package com.sparta.with.repository;

import com.sparta.with.entity.redishash.Blacklist;
import org.springframework.data.repository.CrudRepository;

public interface BlacklistRepository extends CrudRepository<Blacklist, String> {
}
