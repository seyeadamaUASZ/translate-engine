package com.sid.gl.tools;

import com.sid.gl.dto.LanguageRequest;
import com.sid.gl.models.Language;
import org.springframework.beans.BeanUtils;

public class LanguageMapper {

    public static Language convertLanguage(final LanguageRequest dto){
        Language language = new Language();
        BeanUtils.copyProperties(dto,language);
        return language;
    }

    public static LanguageRequest convertDTO(final Language language){
        LanguageRequest languageDTO = new LanguageRequest();
        BeanUtils.copyProperties(language,languageDTO);
        return languageDTO;
    }
}
