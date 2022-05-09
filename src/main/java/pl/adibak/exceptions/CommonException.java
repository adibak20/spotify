package pl.adibak.exceptions;

public class CommonException extends RuntimeException {

    private final ExceptionType type;

    public CommonException(ExceptionType type) {
        this.type   = type;
    }

    public ExceptionType getType() {
        return type;
    }
}
