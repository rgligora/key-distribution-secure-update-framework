package hr.fer.kdsuf.exception.exceptions;

public class DeviceNotFoundException extends RuntimeException{

    public DeviceNotFoundException(String id){
        super("Device with id: '" + id + "' not found!");
    }
}
