package com.xrc.arduino.twoWD.impl;

import com.xrc.arduino.serial.SerialConnection;
import com.xrc.arduino.serial.SerialListener;
import com.xrc.arduino.twoWD.Controller;
import com.xrc.arduino.twoWD.ControllerListener;
import com.xrc.arduino.twoWD.MotionCommand;
import com.xrc.util.observer.AsyncObservableBase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;

public class Arduino2WDController
        extends AsyncObservableBase<ControllerListener>
        implements Controller, SerialListener {

    private static final String PROGRAM_STARTED_NOTIFICATION = "PROGRAM_STARTED";

    private static final String INVALID_REQUEST = "INVALID";

    private static final String STATE_REQUEST = "GET_STATE";

    private final SerialConnection serialConnection;

    public Arduino2WDController(SerialConnection serialConnection) {
        this.serialConnection = serialConnection;
    }

    @Override
    public void initialize() throws Exception {

        serialConnection.subscribe(this);
    }

    @Override
    public void sendMotionCommand(MotionCommand command) {
        String commandLine = command.getLine();
        sendRequest(commandLine);
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
                super.notifyObservers(ControllerListener::onStart);
                break;
            case INVALID_REQUEST:
                super.notifyObservers(ControllerListener::onInvalidRequest);
                break;
            default:
                try {
                    MotionState state = MotionState.valueOf(line);
                    super.notifyObservers(listener -> listener.stateReceived(state));
                } catch (IllegalArgumentException ignored) {
                    super.notifyObservers(listener -> listener.messageReceived(line));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void requestState() {
        sendRequest(STATE_REQUEST);
    }

    public SerialConnection getConnection() {
        return serialConnection;
    }

    private void sendRequest(String request) {
        try {
            Writer outputWriter = serialConnection.getOutputWriter();
            outputWriter.write(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}


