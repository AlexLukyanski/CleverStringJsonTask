package by.clever.stringtask.exception;

import java.io.Serial;

public class JsonParserException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 7665657603430758825L;

    public JsonParserException() {
        super();
    }

    public JsonParserException(String message) {
        super(message);
    }

    public JsonParserException(Exception e) {
        super(e);
    }

    public JsonParserException(String message, Exception e) {
        super(message, e);
    }
}
