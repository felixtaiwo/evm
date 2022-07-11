package com.dbest.sevs.security;

import com.dbest.sevs.service.Logic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JWTUserDetailService implements UserDetailsService {
    @Autowired
   private Logic service;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if(service.findUserByEmailAddress(email)!=null){
            return (UserDetails) service.findUserByEmailAddress(email);
        }
      throw new UsernameNotFoundException("Account Number Not found");
    }
}
