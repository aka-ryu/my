package com.example.my.repository;

import com.example.my.domain.entity.Board;
import com.example.my.domain.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReplyRepo extends JpaRepository<Reply, Long> {

    List<Reply> findAllByBoard(Board board);

    Optional<Reply> findById(Long rno);
}
