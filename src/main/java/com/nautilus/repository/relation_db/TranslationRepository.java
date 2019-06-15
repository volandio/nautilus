package com.nautilus.repository.relation_db;

import com.nautilus.model.entity.Translate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TranslationRepository extends JpaRepository<Translate, Integer> {
}
