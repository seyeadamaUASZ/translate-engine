package com.sid.gl.services;

import com.sid.gl.Repository.LanguageRepository;
import com.sid.gl.dto.LanguageCodeRequest;
import com.sid.gl.dto.LanguageRequest;
import com.sid.gl.models.Language;
import com.sid.gl.models.Translator;
import com.sid.gl.tools.ApiResponse;
import com.sid.gl.tools.LanguageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;


import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TranslatorService {
    @Value("${app.folder.i18n}")
    private String path_i18n;

    private final ILanguage iLanguage;
    private final LanguageRepository languageRepository;

    //read content json file
    private JSONObject getJsonFromFile(String code) throws IOException {
        log.info("get json file found on {} ",code);
        Path dir = Paths.get(path_i18n);
        if(!Files.exists(dir)){
            Files.createDirectories(dir);
        }
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("templates/assets/i18n/" + "fr" + ".json").getFile());

        File file_lngCode = new File(classLoader.getResource("templates/assets/i18n/" + code + ".json").getFile());
        File defaultFile = new File(path_i18n+"/fr.json");

        if(!file_lngCode.exists()) {
            try {
                if (defaultFile.exists()) {
                    Files.copy(defaultFile.toPath(), Paths.get(path_i18n + "/" + code + ".json"));
                } else
                    Files.copy(file.toPath(), Paths.get(path_i18n + "/" + code + ".json"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        StringBuffer jsonBuffer = new StringBuffer();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file_lngCode));
            String line;
            while ((line =reader.readLine()) != null) {
                jsonBuffer.append(line);
            }
        }catch (IOException e) {
            log.error("error to get jsonfromFile");
            e.printStackTrace();
            return null;
        }finally {
            if(jsonBuffer.toString().isEmpty()){
                return null;
            }else{
                try{
                    JSONObject jsonObject = new JSONObject(jsonBuffer.toString());
                    return jsonObject;
                }catch (JSONException ex) {
                    ex.printStackTrace();
                    return null;
                }
            }
        }
    }

    private String makeLinearObject(String key,JSONObject jsonObject){
        String keys[] = JSONObject.getNames(jsonObject);
        StringBuffer buffer = new StringBuffer();
        if(keys==null){
            return "";
        }
        if(keys.length==0)
            return buffer.toString();
        for(String k : keys){
            if(jsonObject.get(k) instanceof JSONObject){
                if(!key.trim().isEmpty() && key.lastIndexOf(".")!=key.length())
                    key+=".";
                buffer.append(makeLinearObject(key + k, (JSONObject) jsonObject.get(k)));
            }else{
                if(!key.isEmpty()){
                    if (!key.trim().isEmpty() && key.lastIndexOf(".") != (key.length() - 1))
                        key += ".";
                    buffer.append(JSONObject.quote(key + k) + ":" + JSONObject.quote(jsonObject.get(k).toString()) + "\r\n");
                }else
                    buffer.append(JSONObject.quote(k) + ":" + JSONObject.quote(jsonObject.get(k).toString()) + "\r\n");
            }
        }
        //log.info("debugging buffer {} ",buffer.toString());
        return buffer.toString();
    }

    private JSONObject rJsonAddOrUpdate(JSONObject js, String[] keys, String value) {
        JSONObject jsend = new JSONObject();
        if (keys.length == 0)
            return js;
        if (keys.length > 1) {
            String k0 = keys[0];

            keys = ArrayUtils.remove(keys, 0);
            if (js.has(k0)) {
                js.put(k0, rJsonAddOrUpdate((JSONObject) js.get(k0), keys, value));
            } else {
                js.put(k0, rJsonAddOrUpdate(jsend, keys, value));
            }
        } else if (keys.length == 1) {
            return js.put(keys[0], value);
        }

        return js;

    }

    public Object i18n(String codeLangue) throws IOException {
        log.info("retrieve a json file from code {} ",codeLangue);
        JSONObject jsonObject = getJsonFromFile(codeLangue);
        if(jsonObject==null){
            return getJsonFromFile("fr").toString();
        }
        return jsonObject.toString();
    }

    //ajouter une description
    public ApiResponse create(Translator translator) throws IOException {
        log.info("creating translator on body {} ",translator);
        String dirKeys[]=translator.getReference().split("\\.");
        JSONObject jsonObject = getJsonFromFile(translator.getLangue().getLngCode());
        System.out.println("json object "+jsonObject.toString());
        JSONObject js = null;
        boolean status = false;
        if(jsonObject !=null){
            js = rJsonAddOrUpdate(jsonObject,dirKeys,translator.getSelectedLangue());
            if(js!=null)
                status = setJsonOnFile(translator.getLangue().getLngCode(),jsonObject.toString());

            List<Language> languages = buildListLangue(iLanguage.listLanguage());
            for(Language l : languages){
                if(!l.getLngCode().equals(translator.getLangue().getLngCode())){
                   JSONObject json0 = getJsonFromFile(l.getLngCode());
                   JSONObject j = null;
                   if(json0 !=null){
                       boolean isFr = translator.getLangue().getLngCode().equals("fr");
                       j= rJsonAddOrUpdate(json0,dirKeys, isFr ? translator.getSelectedLangue() : translator.getDefaultLangue());
                       if(j!=null)
                           status = setJsonOnFile(l.getLngCode(),json0.toString());

                   }
                }
            }

        }
        return new ApiResponse(status,status?"config.success":"config.failed",js);
    }

    //for read all

    public List<Translator> read(LanguageCodeRequest langue) throws IOException {
        log.info("retrieve all list ....");
        JSONObject jsonObjectDefault = getJsonFromFile("fr");
        List<Translator> list = new ArrayList<>();
        StringBuffer jsonSelected;
        StringBuffer jsonDefault = new StringBuffer(makeLinearObject("", jsonObjectDefault));
        jsonDefault = new StringBuffer("{" + jsonDefault.toString().replaceAll("\r\n", ",") + "}");
        jsonDefault = jsonDefault.replace(jsonDefault.length() - 2, jsonDefault.length() - 1, "");

        Language language = languageRepository.findByLngCode(langue.getCode())
                .orElseThrow(()->new RuntimeException("Language parameter not found !!!"));

        if (!langue.getCode().equals("fr")) {
            //check this on database
            JSONObject jsonObject = getJsonFromFile(language.getLngCode());

            jsonSelected = new StringBuffer(makeLinearObject("", jsonObject));
            jsonSelected = new StringBuffer("{" + jsonSelected.toString().replaceAll("\r\n", ",") + "}");
            jsonSelected = jsonSelected.replace(jsonSelected.length() - 2, jsonSelected.length() - 1, "");
        } else {
            jsonSelected = jsonDefault;
        }

        try {
            JSONObject defaultJson = new JSONObject(jsonDefault.toString());
            JSONObject selectedJson = new JSONObject(jsonSelected.toString());

            for (String key : defaultJson.keySet()) {
                list.add(new Translator(key, defaultJson.getString(key),
                        selectedJson.has(key) ? selectedJson.getString(key) : "", language));
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
            log.error("error to retrieve list ....");
          throw new RuntimeException(ex.getMessage());

        }
        return list;

    }

    public boolean delete(Translator traduction) throws IOException {
        log.info("deleting traduction ....");
        JSONObject js = getJsonFromFile(traduction.getLangue().getLngCode());
        boolean statut = false;
        if (js != null) {
            String[] keys = traduction.getReference().split("\\.");
            js = rJsonRemove(js, keys);
            statut = setJsonOnFile(traduction.getLangue().getLngCode(), js.toString());

            // do the same for others language

            List<Language> list = buildListLangue(iLanguage.listLanguage());
            for (Language l : list) {

                if (!l.getLngCode().equals(traduction.getLangue().getLngCode())) {
                    JSONObject js2 = getJsonFromFile(l.getLngCode());
                    String[] keys2 = traduction.getReference().split("\\.");
                    js2 = rJsonRemove(js2, keys2);
                    statut = setJsonOnFile(l.getLngCode(), js2.toString());
                }
            }
        }
        return statut;
    }

    private JSONObject rJsonRemove(JSONObject js, String[] keys) {

        if (keys.length == 0)
            return js;
        if (keys.length > 1) {
            String k0 = keys[0];

            keys =  ArrayUtils.remove(keys, 0);
            if (js.has(k0)) {
                js.put(k0, rJsonRemove((JSONObject) js.get(k0), keys));
            } else {

                return js;
            }
        } else if (keys.length == 1) {

            if (js.has(keys[0])) {
                js.remove(keys[0]);
                return js;
            } else {
                return js;
            }
        }
        return js;
    }


    private boolean setJsonOnFile(String language, String json) {
        Path path = Paths.get(path_i18n);
        File file = new File(path.toString() + "/" + language + ".json");
        try {
            // general writer
            FileOutputStream fos = new FileOutputStream(file);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos, Charset.forName("UTF-8")));
            out.write(json);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return true;
    }


    private List<Language> buildListLangue(List<LanguageRequest> languageDTOS){
        return languageDTOS
                .stream()
                .map(LanguageMapper::convertLanguage)
                .collect(Collectors.toList());
    }

}
