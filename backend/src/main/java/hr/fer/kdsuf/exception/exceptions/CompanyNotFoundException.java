package hr.fer.kdsuf.exception.exceptions;

public class CompanyNotFoundException extends RuntimeException{
    public CompanyNotFoundException(String id){
        super("Company with id: '" + id + "' not found!");
    }
}
