package com.example.business.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.business.domain.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>{
	
}
