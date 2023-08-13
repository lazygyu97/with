package com.sparta.with.repository;

import com.sparta.with.entity.CheckList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckListRepository extends JpaRepository<CheckList, Long> {

}
