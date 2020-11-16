package com.example.business.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.business.domain.Tweet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {


	List<Tweet> findAllByOrderByIdDesc();
	
	Page<Tweet> findAllByOrderByIdDesc(Pageable pageable);
	
}
