package com.sid.gl.services;

import com.sid.gl.Repository.LanguageRepository;
import com.sid.gl.dto.LanguageRequest;
import com.sid.gl.models.DirectionLanguage;
import com.sid.gl.models.Language;
import com.sid.gl.tools.LanguageMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class LanguageImplTest {

    @Mock
    private LanguageRepository languageRepository;
    private LanguageImpl service;

    @BeforeEach
    void setUp(){
        service = new LanguageImpl(languageRepository);
    }



    @Test
    void addLanguage() {
        LanguageRequest language = new LanguageRequest();
        language.setLngCode("fr");
        language.setCompletDescription("completion");
        language.setDirectionLanguage(DirectionLanguage.LEFT);

        service.addLanguage(language);
        ArgumentCaptor<Language> captor = ArgumentCaptor.forClass(Language.class);
        verify(languageRepository).save(captor.capture());
        Language languageCapture = captor.getValue();

        //assertThat(languageCapture).isEqualTo(language);
        assertNotNull(languageCapture);

    }

    @Test
    void listLanguage() {
        service.listLanguage();
        verify(languageRepository).findAll();
    }



}