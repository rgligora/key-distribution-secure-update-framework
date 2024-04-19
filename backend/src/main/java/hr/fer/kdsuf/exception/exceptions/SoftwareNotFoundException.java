package hr.fer.kdsuf.exception.exceptions;

public class SoftwareNotFoundException extends RuntimeException{
    public SoftwareNotFoundException(String id){
        super("Software with id: '" + id + "' not found!");
    }
}
