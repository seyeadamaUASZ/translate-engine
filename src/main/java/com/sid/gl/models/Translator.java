package com.sid.gl.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Translator {
    private String reference;
    private String defaultLangue;
    private String selectedLangue;
    private Language langue;
}
