package com.nautilus.repository.relation_db;

import com.nautilus.model.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GroupRepository extends JpaRepository<Group, Integer> {

    @Query("SELECT g FROM Group g WHERE LOWER(g.name) = LOWER(:name)")
    Group findByGroupNameCaseInsensitive(@Param("name") String name);
}
