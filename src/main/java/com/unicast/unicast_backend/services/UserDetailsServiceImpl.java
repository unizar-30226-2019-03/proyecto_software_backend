package com.unicast.unicast_backend.services;

import java.util.Optional;

import com.unicast.unicast_backend.persistance.model.User;
import com.unicast.unicast_backend.persistance.repository.rest.UserRepository;
import com.unicast.unicast_backend.principal.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public UserDetails loadUserById(Long userId) {
        Optional<User> user = userRepository.findById(userId);

        if (!user.isPresent()) {
            throw new UsernameNotFoundException(userId.toString());
        }

        return new UserDetailsImpl(user.get());
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        return new UserDetailsImpl(user);
    }
}