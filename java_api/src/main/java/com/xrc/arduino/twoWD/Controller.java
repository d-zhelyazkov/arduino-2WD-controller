package com.xrc.arduino.twoWD;

public interface Controller {

    void initialize() throws Exception;

    void move(MoveCommand command);

    void turn(TurnCommand turnCommand);

    MotionState getState();

    void subscribe(ControllerListener listener);

    enum MotionState {
        STOPPED,
        MOVING_FORWARD,
        MOVING_BACKWARD,
        TURNING_LEFT,
        TURNING_RIGHT
    }
}
