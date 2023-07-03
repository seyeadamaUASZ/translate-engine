package com.sid.gl.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LanguageCodeRequest {
    @NotNull
    private String code;
}
