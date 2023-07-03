package com.sid.gl.controllers;

import com.sid.gl.dto.LanguageRequest;
import com.sid.gl.models.Language;
import com.sid.gl.services.ILanguage;
import com.sid.gl.tools.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/language")
@RequiredArgsConstructor
@Tag(name = "Parametize language", description = "option with code fr,en,ch,sp")
public class LanguageController {
    private final ILanguage iLanguage;

    @Operation(
            summary = "Retrieve all language",
            description = "Get all languages",
            tags = { "language", "get" })
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Language.class), mediaType = "application/json") }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })

    @GetMapping
    public ResponseEntity<ApiResponse> getAllLanguages(){
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus(true);
        apiResponse.setData(iLanguage.listLanguage());
        apiResponse.setMessage("all languages");
        return ResponseEntity.ok(apiResponse);
    }


    @Operation(
            summary = "add language",
            description = "post requestbody  language",
            tags = { "language", "post" })
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Language.class), mediaType = "application/json") }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @PostMapping
    public ResponseEntity<ApiResponse> addLanguage(@RequestBody @Valid final LanguageRequest languageDTO){
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus(true);
        apiResponse.setData(iLanguage.addLanguage(languageDTO));
        apiResponse.setMessage("add language");
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(
            summary = "Retrieve one language with id",
            description = "Get language with id",
            tags = { "language", "get" })
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Language.class), mediaType = "application/json") }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getLanguage(@PathVariable("id") Long id){
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus(true);
        apiResponse.setData(iLanguage.getLanguage(id));
        apiResponse.setMessage("get one language");
        return ResponseEntity.ok(apiResponse);
    }

}
