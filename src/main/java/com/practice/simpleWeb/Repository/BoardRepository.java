package com.practice.simpleWeb.Repository;

import com.practice.simpleWeb.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board,Long> {


    Page<Board> findByMember_id(Long member_id, Pageable pageable);
}
