package com.example.web.controller;

import com.example.util.UserCustom;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.List;
import com.example.business.domain.Tweet;
import com.example.business.domain.User;
import com.example.business.repository.TweetRepository;
import com.example.business.repository.UserRepository;

@Controller
public class TweetController {
	
	@Autowired
	private TweetRepository tweetRepository;
	
	UserRepository userRepository;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView index(@PageableDefault(size = 5) Pageable pageable, ModelAndView mav, @AuthenticationPrincipal UserDetails userDetails) {
		Page<Tweet> tweets = tweetRepository.findAllByOrderByIdDesc(pageable);
	    mav.addObject("tweets", tweets);
	    mav.setViewName("tweet/index");
	    return mav;
	}

	@RequestMapping(value = "/tweet/new", method = RequestMethod.GET)
	public ModelAndView newTweet(ModelAndView mav) {
	    mav.setViewName("tweet/new");
	    return mav;
	}
	
	@RequestMapping(value = "/tweet/new", method = RequestMethod.POST)
    public ModelAndView createTweet(@ModelAttribute Tweet tweet, @AuthenticationPrincipal UserCustom userCustom, ModelAndView mav) {
        	User user = userRepository.findOne(userCustom.getId());
        	tweet.setUser(user);
		tweetRepository.saveAndFlush(tweet);
        mav.setViewName("tweet/create");
        return mav;
    }
	
	@RequestMapping(value = "/tweet/{id}/edit", method = RequestMethod.GET)
	public ModelAndView editTweet(@PathVariable("id") Long id, ModelAndView mav) {
		//@PathVariableアノテーションは既に存在するツイートを変更する
	    Tweet tweet = tweetRepository.findOne(id);
	    mav.addObject("tweet", tweet);
	    mav.setViewName("tweet/edit");
	    return mav;
	}
	
	@RequestMapping(value = "/tweet/{id}/delete", method = RequestMethod.POST)
	public ModelAndView deleteTweet(@PathVariable("id") Long id, @AuthenticationPrincipal UserCustom userCustom, ModelAndView mav) {
	    Tweet tweet = tweetRepository.findOne(id);
	    if (!tweet.getUser().getId().equals(userCustom.getId())) {
	        mav.setViewName("redirect:/");
	        return mav;
	    }
	    tweetRepository.delete(tweet);
	    //deleteメソッドはJPAが提供するデータベースから指定したデータを削除するやつ
	    mav.setViewName("redirect:/");
	    return mav;
	}
	
	@RequestMapping(value = "/tweet/{id}/edit", method = RequestMethod.POST)
	public ModelAndView updateTweet(@ModelAttribute Tweet editTweet, @PathVariable("id") Long id, @AuthenticationPrincipal UserCustom userCustom, ModelAndView mav) {        
	    Tweet tweet = tweetRepository.findOne(id);
	    if (!tweet.getUser().getId().equals(userCustom.getId())) {
	    	mav.setViewName("redirect:/tweet" + id + "/edit");
	    	return mav;
	    }
	    BeanUtils.copyProperties(editTweet, tweet);
	    //BeansUtils.copyPropertiesメソッドを使用。フォームから送られてきた
	    //ツイート情報をデータベースから取得したツイートにコピーしている。
	    //editTweetに与えられたオブジェクトの値をtweetにコピーするもの。内容が変更されたツイートを保存。という処理の形.
	    tweetRepository.save(tweet);
	    mav.setViewName("tweet/update");
	    return mav;
	}
	
	
	
	@RequestMapping(value = "/tweet/{id}", method = RequestMethod.GET)
	ModelAndView show(@PathVariable Long id, ModelAndView mav) {
		Tweet tweet = tweetRepository.findOne(id);
		mav.addObject("tweet", tweet);
		mav.setViewName("tweet/show");
		return mav;
	}
	
	@ModelAttribute(name = "login_user")
	public UserDetails setLoginUser(@AuthenticationPrincipal UserCustom userCustom) {
		return userCustom;
	}
	

}