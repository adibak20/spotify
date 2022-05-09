package pl.adibak.exceptions;

import lombok.Getter;

@Getter
public enum ExceptionType {

    BAD_REQUEST(400, "Błąd systemu"),
    INTERNAL_SERVER_ERROR(500, "Błąd systemu"),
    UNAUTHORIZED(403, "Odmowa dostępu");

    private final int code;
    private final String message;

    ExceptionType(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
