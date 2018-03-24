package ml.ixplo.arenabot.exception;

public class ArenaUserException extends RuntimeException {

    public ArenaUserException(String message) {
        super(message);
    }

    public ArenaUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
