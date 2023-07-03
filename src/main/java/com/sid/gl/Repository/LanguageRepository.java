package com.sid.gl.Repository;

import com.sid.gl.models.Language;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LanguageRepository extends JpaRepository<Language,Long> {
  Optional<Language> findByLngCode(String code);
}
