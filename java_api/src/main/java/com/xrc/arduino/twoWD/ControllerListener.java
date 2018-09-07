package com.xrc.arduino.twoWD;

public interface ControllerListener {
    void onStart();

    void onStateChange();

    void onInvalidCommand();

    void onMessage(String message);
}
