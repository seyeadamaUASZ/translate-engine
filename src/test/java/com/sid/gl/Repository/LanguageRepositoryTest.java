package com.sid.gl.Repository;

import com.sid.gl.models.DirectionLanguage;
import com.sid.gl.models.Language;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class LanguageRepositoryTest {
    @Autowired
    private LanguageRepository languageRepository;

    @Test
    void should_addLanguage(){
        Language language = new Language();
        language.setDirectionLanguage(DirectionLanguage.LEFT);
        language.setCompletDescription("fr description language");
        language.setLngCode("fr");

        Language result = languageRepository.save(language);

        assertNotNull(result);

    }

    @Test
    void should_find_LngCode(){
        Language language = new Language();
        language.setDirectionLanguage(DirectionLanguage.LEFT);
        language.setCompletDescription("fr description language");
        language.setLngCode("fr");

        Language result = languageRepository.save(language);

        Optional<Language> expected = languageRepository.findByLngCode(result.getLngCode());
        assertTrue(expected.isPresent());
    }

    @Test
    void should_list_Languages(){
        List<Language> lists = languageRepository.findAll();
        assertEquals(0,lists.size());
    }

   @Test
    void should_failed_insertion_duplicate_code(){
        Language language = new Language();
        language.setLngCode("fr");
        language.setCompletDescription("fr translation...");
        language.setDirectionLanguage(DirectionLanguage.LEFT);
        languageRepository.save(language);

        Language language1 = new Language();
        language.setLngCode("fr");
        language.setCompletDescription("fr translation...");
        language.setDirectionLanguage(DirectionLanguage.LEFT);

      // Mockito.doThrow(new Exception()).when(languageRepository).save(language1);

//      Language result = languageRepository.save(language1);
//       assertNull(result);
       Assertions.assertThrows(ConstraintViolationException.class, () -> languageRepository.save(language1));

    }



}