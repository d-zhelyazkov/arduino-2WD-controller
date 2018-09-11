package com.xrc.arduino.twoWD;

public interface Controller {

    void initialize() throws Exception;

    void sendMotionCommand(MotionCommand command);

    void requestState();

    void subscribe(ControllerListener listener);

    enum MotionState {
        STOPPED,
        MOVING_FORWARD,
        MOVING_BACKWARD,
        TURNING_LEFT,
        TURNING_RIGHT
    }
}
