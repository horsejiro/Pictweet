package com.example.business.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.business.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);
	User findByEmail(String email);
}
