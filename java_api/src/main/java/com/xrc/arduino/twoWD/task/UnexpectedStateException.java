package com.xrc.arduino.twoWD.task;

public class UnexpectedStateException extends RuntimeException {
    public UnexpectedStateException(String message) {
        super(message);
    }
}
