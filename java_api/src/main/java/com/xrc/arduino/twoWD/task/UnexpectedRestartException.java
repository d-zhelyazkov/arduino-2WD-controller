package com.xrc.arduino.twoWD.task;

public class UnexpectedRestartException extends RuntimeException {
    public UnexpectedRestartException(String message) {
        super(message);
    }
}
