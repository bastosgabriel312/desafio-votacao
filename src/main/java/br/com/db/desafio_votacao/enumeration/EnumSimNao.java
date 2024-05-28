package br.com.db.desafio_votacao.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EnumSimNao {
    SIM("S", "Sim"),
    NAO("N", "Não");

    private final String code;

    EnumSimNao(String code, String description) {
        this.code = code;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    @JsonCreator
    public static EnumSimNao fromCode(String code) {
        for (EnumSimNao value : EnumSimNao.values()) {
            if (value.code.equalsIgnoreCase(code)) {
                return value;
            }
        }
        throw new IllegalArgumentException("Voto inválido: " + code);
    }
}

