package hr.fer.kdsuf.exception.exceptions;

public class ModelNotFoundException extends RuntimeException{

    public ModelNotFoundException(String id){
        super("Model with id: '" + id + "' not found!");
    }
}
