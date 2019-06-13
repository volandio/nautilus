package com.nautilus.repository.relation_db;

import com.nautilus.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u WHERE LOWER(u.login) = LOWER(:login)")
    User findByLoginCaseInsensitive(@Param("login") String login);
}
