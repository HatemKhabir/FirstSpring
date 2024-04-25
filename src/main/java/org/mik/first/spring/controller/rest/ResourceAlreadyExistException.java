package org.mik.first.spring.controller.rest;

public class ResourceAlreadyExistException extends RuntimeException{
    public ResourceAlreadyExistException(String msg){
        super(msg);
    }
}
