package com.practice.simpleWeb.Repository;

import com.practice.simpleWeb.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Long> {

     Optional<Member> findByEmail(String email);

     Boolean existsByEmail(String email);




}
