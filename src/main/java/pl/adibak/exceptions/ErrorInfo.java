package pl.adibak.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorInfo {

    private String message;

    public ErrorInfo(String message) {
        this.message = message;
    }

}
