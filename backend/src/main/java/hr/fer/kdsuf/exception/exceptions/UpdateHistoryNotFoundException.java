package hr.fer.kdsuf.exception.exceptions;

public class UpdateHistoryNotFoundException extends RuntimeException{
    public UpdateHistoryNotFoundException(String id) {
        super("Update history with id: '" + id + "' not found!");
    }
}
