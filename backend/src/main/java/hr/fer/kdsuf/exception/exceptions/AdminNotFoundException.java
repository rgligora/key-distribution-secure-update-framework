package hr.fer.kdsuf.exception.exceptions;

public class AdminNotFoundException extends RuntimeException{

    public AdminNotFoundException(String id){
        super("Admin user with id: '" + id + "' not found!");
    }

}
