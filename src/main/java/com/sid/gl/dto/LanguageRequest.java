package com.sid.gl.dto;

import com.sid.gl.models.DirectionLanguage;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LanguageRequest {
    private Long id;
    @NotNull
    private String lngCode;
    private DirectionLanguage directionLanguage;
    private String completDescription;
}
