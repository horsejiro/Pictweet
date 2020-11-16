package com.example.security;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.business.domain.User;
import com.example.business.repository.UserRepository;
import com.example.util.UserCustom;
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;
	
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	    User user = userRepository.findByEmail(email);
	    Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

	    return new UserCustom(user.getId(), user.getUsername(), user.getPassword(), grantedAuthorities);
	}
}

