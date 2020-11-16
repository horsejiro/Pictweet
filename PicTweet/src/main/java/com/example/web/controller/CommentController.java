package com.example.web.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.example.business.domain.Comment;
import com.example.business.domain.Tweet;
import com.example.business.domain.User;
import com.example.business.repository.CommentRepository;
import com.example.business.repository.TweetRepository;
import com.example.business.repository.UserRepository;
import com.example.util.UserCustom;

@Controller
public class CommentController {

	@Autowired
	CommentRepository commentRepository;
	
	@Autowired
	TweetRepository tweetRepository;
	
	@Autowired
	UserRepository userRepository;
	
	//コメント保存
	@RequestMapping(value = "/tweet/{tweetId}/comment", method = RequestMethod.POST)
	ModelAndView createComment(@ModelAttribute Comment comment, @PathVariable Long tweetId,
			@AuthenticationPrincipal UserCustom userCustom, ModelAndView mav) {
		Tweet tweet = tweetRepository.findOne(tweetId);
		User user = userRepository.findOne(userCustom.getId());
		comment.setTweet(tweet);
		comment.setUser(user);
		commentRepository.saveAndFlush(comment);
		mav.setViewName("redirect:/tweet/" + tweetId);
		return mav;
		
	}
}
