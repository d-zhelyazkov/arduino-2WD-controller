package com.xrc.arduino.twoWD;

import com.xrc.util.observer.Observable;

public interface Controller extends Observable<ControllerListener> {

    void initialize() throws Exception;

    void sendMotionCommand(MotionCommand command);

    void requestState();

    enum MotionState {
        STOPPED,
        MOVING_FORWARD,
        MOVING_BACKWARD,
        TURNING_LEFT,
        TURNING_RIGHT
    }
}
