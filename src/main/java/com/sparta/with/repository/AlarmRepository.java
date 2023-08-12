package com.sparta.with.repository;

import com.sparta.with.dto.AlarmResponseDto;
import com.sparta.with.entity.Alarm;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    List<Alarm> findByBoard_Id(Long boardId);
}
