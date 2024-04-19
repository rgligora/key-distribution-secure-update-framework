package hr.fer.kdsuf.exception.exceptions;

public class SoftwarePackageNotFoundException extends RuntimeException{
    public SoftwarePackageNotFoundException(String id){
        super("Software Package with id: '" + id + "' not found!");
    }
}
