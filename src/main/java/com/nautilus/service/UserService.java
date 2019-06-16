package com.nautilus.service;

import com.nautilus.exception.ErrorCode;
import com.nautilus.exception.GenericRuntimeException;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LoginHelper loginHelper;
    @Autowired
    private I18nService i18n;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

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
        grantedAuthorities.add(new SimpleGrantedAuthority(userFromDatabase.getGroup().getName()));
        return new org.springframework.security.core.userdetails.User(userFromDatabase.getLogin(),
                userFromDatabase.getHashPassword(), grantedAuthorities);
    }

    public User save(User user) {
        User tempUser = userRepository.findByLoginCaseInsensitive(user.getLogin());
        if (tempUser != null) {
            throw new GenericRuntimeException(i18n.getMessage("error.user.login_is_busy"), ErrorCode.UNKNOWN);
        }
        user.setHashPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public void delete(Integer id) {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            throw new GenericRuntimeException(i18n.getMessage("error.user.login_id_not_exist"), ErrorCode.NOT_FOUND);
        }
        if (user.get().getLogin().equals("admin")) {
            throw new GenericRuntimeException(i18n.getMessage("error.user.login_admin"), ErrorCode.UNKNOWN);
        }
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<User> getAll() {
        return userRepository.findAll();
    }
}
