package com.sid.gl.services;

import com.sid.gl.dto.LanguageRequest;
import com.sid.gl.models.Language;

import java.util.List;

public interface ILanguage {
    Language addLanguage(LanguageRequest dto);
    List<LanguageRequest> listLanguage();
    LanguageRequest getLanguage(Long id);

}
