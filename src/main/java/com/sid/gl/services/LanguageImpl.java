package com.sid.gl.services;

import com.sid.gl.Repository.LanguageRepository;
import com.sid.gl.dto.LanguageRequest;
import com.sid.gl.models.Language;
import com.sid.gl.tools.LanguageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LanguageImpl implements ILanguage {
    private final LanguageRepository languageRepository;

    @Override
    public Language addLanguage(LanguageRequest dto) {
        log.info("add language ..{} ",dto);
        return languageRepository.save(LanguageMapper.convertLanguage(dto));
    }

    @Override
    public List<LanguageRequest> listLanguage() {
        log.info("retrieve all languages.....");
        return languageRepository.findAll()
                .stream()
                .map(LanguageMapper::convertDTO)
                .collect(Collectors.toList());
    }

    @Override
    public LanguageRequest getLanguage(Long id) {
        log.info("language with id {} ",id);
        Language language = languageRepository
                .findById(id)
                .orElseThrow(()->new RuntimeException("Language selected not found !!!"));
        return LanguageMapper.convertDTO(language);
    }
}
