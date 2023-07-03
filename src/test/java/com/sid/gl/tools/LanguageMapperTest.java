package com.sid.gl.tools;

import com.sid.gl.dto.LanguageRequest;
import com.sid.gl.models.DirectionLanguage;
import com.sid.gl.models.Language;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LanguageMapperTest {

    @Test
    void convertLanguage() {
        LanguageRequest request = new LanguageRequest();
        request.setCompletDescription("complete");
        request.setLngCode("fr");
        request.setDirectionLanguage(DirectionLanguage.LEFT);

        Language result = LanguageMapper.convertLanguage(request);
        assertNotNull(result);
        assertEquals("complete",result.getCompletDescription());
        assertEquals("fr",result.getLngCode());
        assertEquals(DirectionLanguage.LEFT,result.getDirectionLanguage());
    }

    @Test
    void convertDTO() {
        Language language = new Language();
        language.setCompletDescription("complete");
        language.setLngCode("fr");
        language.setDirectionLanguage(DirectionLanguage.LEFT);

        LanguageRequest result = LanguageMapper.convertDTO(language);
        assertNotNull(result);
        assertEquals("complete",result.getCompletDescription());
        assertEquals("fr",result.getLngCode());
    }
}