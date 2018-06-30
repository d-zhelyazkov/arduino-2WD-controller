package com.xrc.arduino.twoWD;

public interface Controller {

    void initialize() throws Exception;

    void move(MoveDirection direction, float wheelRotations);

    void turn(TurnDirection direction, float wheelRotations);

    MotionState getState();

    void subscribe(ControllerListener listener);

    enum MoveDirection {
        FORWARD,
        BACKWARD
    }

    enum TurnDirection {
        LEFT,
        RIGHT
    }

    enum MotionState {
        STANDBY,
        MOVING_FORWARD,
        MOVING_BACKWARD,
        TURNING_LEFT,
        TURNING_RIGHT
    }
}
