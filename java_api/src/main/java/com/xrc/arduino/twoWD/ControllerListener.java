package com.xrc.arduino.twoWD;

public interface ControllerListener {
    void onStart();

    void stateReceived(Controller.MotionState state);

    void onInvalidRequest();

    void messageReceived(String message);
}
