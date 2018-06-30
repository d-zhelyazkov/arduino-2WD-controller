package com.xrc.arduino.twoWD.impl;

import com.xrc.arduino.serial.SerialConnection;
import com.xrc.arduino.serial.SerialListener;
import com.xrc.arduino.twoWD.Controller;
import com.xrc.arduino.twoWD.ControllerListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;

public class Arduino2WDController
        implements Controller, SerialListener {

    private static final String MOVE_FORWARD_CMD = "MOVE_FORWARD";

    private static final String MOVE_BACKWARD_CMD = "MOVE_BACKWARD";

    private static final String TURN_LEFT_CMD = "TURN_LEFT";

    private static final String TURN_RIGHT_CMD = "TURN_RIGHT";

    private static final String PROGRAM_STARTED_NOTIFICATION = "PROGRAM_STARTED";

    private static final String READY_NOTIFICATION = "READY";

    private static final String INVALID_CMD = "INVALID";

    private static final String ROTATIONS_REQUEST = "AWAITING_ROTATIONS";

    private final Collection<ControllerListener> listeners = new ArrayList<>();

    private final SerialConnection serialConnection;

    private MotionState state;

    private float requestedWheelRotations;

    public Arduino2WDController(SerialConnection serialConnection) {
        this.serialConnection = serialConnection;
    }

    @Override
    public void initialize() throws Exception {

        serialConnection.subscribe(this);
    }

    @Override
    public void move(MoveDirection direction, float wheelRotations) {
        requestedWheelRotations = wheelRotations;

        try {
            Writer outputWriter = serialConnection.getOutputWriter();
            switch (direction) {
            case FORWARD:
                outputWriter.write(MOVE_FORWARD_CMD);
                break;
            case BACKWARD:
                outputWriter.write(MOVE_BACKWARD_CMD);
                break;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void turn(TurnDirection direction, float wheelRotations) {
        requestedWheelRotations = wheelRotations;

        try {
            Writer outputWriter = serialConnection.getOutputWriter();
            switch (direction) {
            case LEFT:
                outputWriter.write(TURN_LEFT_CMD);
                break;
            case RIGHT:
                outputWriter.write(TURN_RIGHT_CMD);
                break;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onSerialInput() {
        try {
            BufferedReader inputReader = serialConnection.getInputReader();
            if (!inputReader.ready())
                return;

            String line = inputReader.readLine();
            switch (line) {
            case PROGRAM_STARTED_NOTIFICATION:
                state = MotionState.STANDBY;
                listeners.forEach(ControllerListener::onInit);
                break;
            case READY_NOTIFICATION:
                state = MotionState.STANDBY;
                listeners.forEach(ControllerListener::onReady);
                break;
            case INVALID_CMD:
                listeners.forEach(ControllerListener::onInvalidCommand);
                break;
            case ROTATIONS_REQUEST:
                Writer outputWriter = serialConnection.getOutputWriter();
                outputWriter.write(Float.toString(requestedWheelRotations));
                break;
            default:
                state = MotionState.valueOf(line);
                listeners.forEach(ControllerListener::onMove);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void subscribe(ControllerListener listener) {
        listeners.add(listener);
    }

    @Override
    public MotionState getState() {
        return state;
    }
}


