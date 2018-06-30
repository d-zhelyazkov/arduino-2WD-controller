package com.xrc.arduino.twoWD;

public interface ControllerListener {
    void onInit();

    void onReady();

    void onMove();

    void onInvalidCommand();
}
