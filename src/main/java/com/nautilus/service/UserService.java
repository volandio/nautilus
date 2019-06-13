package com.nautilus.service;

import com.nautilus.model.entity.Group;
import com.nautilus.model.entity.User;
import com.nautilus.repository.relation_db.UserRepository;
import com.nautilus.util.LoginHelper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final LoginHelper loginHelper;

    @Autowired
    public UserService(UserRepository userRepository,
                       LoginHelper loginHelper) {
        this.userRepository = userRepository;
        this.loginHelper = loginHelper;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(@NonNull String login) {
        String ip = loginHelper.getClientIP();
        User userFromDatabase = userRepository.findByLoginCaseInsensitive(login);
        if (userFromDatabase == null) {
            log.debug("Request to authorize a non-existent user with a login {} from ip {}.", login, ip);
            throw new UsernameNotFoundException("User not found");
        }
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (Group group : userFromDatabase.getGroups()) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(group.getName());
            grantedAuthorities.add(grantedAuthority);
        }
        return new org.springframework.security.core.userdetails.User(userFromDatabase.getLogin(),
                userFromDatabase.getHashPassword(), grantedAuthorities);
    }
}
