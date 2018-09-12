package com.xrc.arduino.twoWD;

import com.xrc.util.observer.Observer;

public interface ControllerListener extends Observer {
    default void onStart() {
    }

    default void stateReceived(Controller.MotionState state) {
    }

    default void onInvalidRequest() {
    }

    default void messageReceived(String message) {
    }
}
