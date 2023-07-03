package com.sid.gl.controllers;

import com.sid.gl.dto.LanguageCodeRequest;
import com.sid.gl.models.Translator;
import com.sid.gl.services.TranslatorService;
import com.sid.gl.tools.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/translate")
@RequiredArgsConstructor
@Tag(name = "Translation language", description = "api for add key translate with language option")
public class TranslateController {
    private final TranslatorService translatorService;

    @Operation(
            summary = "create translation on json file option with code language",
            description = "create translation with code language option",
            tags = { "translator", "post" })
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Translator.class), mediaType = "application/json") }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })

    @PostMapping
    public ResponseEntity<ApiResponse> createTranslation(@RequestBody final Translator translator) throws IOException {
        ApiResponse apiResponse = translatorService.create(translator);
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(
            summary = "read all translations on json file option with code language",
            description = "read json file translations with  language option",
            tags = { "translator", "post" })
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Translator.class), mediaType = "application/json") }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @PostMapping("/read")
    public ResponseEntity<ApiResponse> listTranslations(@RequestBody final LanguageCodeRequest language) throws IOException {
        List<Translator> list = translatorService.read(language);
        ApiResponse apiResponse = new ApiResponse(true,"retrieving list translators ",list);
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(
            summary = "get i18n file with code language",
            description = "get i18n json file",
            tags = { "translator", "get" })
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Translator.class), mediaType = "application/json") }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })

    @GetMapping("/{code}")
    public ResponseEntity<ApiResponse> i18n(@PathVariable("code") String code) throws IOException {
       ApiResponse apiResponse = new ApiResponse();
       apiResponse.setStatus(true);
       apiResponse.setMessage("retrieve i18n json file");
       apiResponse.setData(translatorService.i18n(code));
       return ResponseEntity.ok(apiResponse);
    }

    @Operation(
            summary = "delete translator on json file",
            description = "delete translator on json file",
            tags = { "translator", "post" })
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Translator.class), mediaType = "application/json") }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @PostMapping("/delete")
    public ResponseEntity<ApiResponse> delete(@RequestBody final Translator translator) throws IOException {
        ApiResponse apiResponse = new ApiResponse();
        boolean statut = translatorService.delete(translator);
        apiResponse.setStatus(statut);
        apiResponse.setMessage(statut ? "configuration.traduction.notification.delete.success"
                : "configuration.traduction.notification.delete.failure");
        apiResponse.setData(null);
        return ResponseEntity.ok(apiResponse);
    }

}
