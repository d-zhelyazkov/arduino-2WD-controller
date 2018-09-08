package com.xrc.arduino.twoWD.impl;

import com.xrc.arduino.serial.SerialConnection;
import com.xrc.arduino.serial.SerialListener;
import com.xrc.arduino.twoWD.Controller;
import com.xrc.arduino.twoWD.ControllerListener;
import com.xrc.arduino.twoWD.MoveCommand;
import com.xrc.arduino.twoWD.TurnCommand;
import com.xrc.command.CommandLine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;

public class Arduino2WDController
        implements Controller, SerialListener {

    private static final String PROGRAM_STARTED_NOTIFICATION = "PROGRAM_STARTED";

    private static final String INVALID_REQUEST = "INVALID";

    private static final String STATE_REQUEST = "GET_STATE";

    private final Collection<ControllerListener> listeners = new ArrayList<>();

    private final SerialConnection serialConnection;

    public Arduino2WDController(SerialConnection serialConnection) {
        this.serialConnection = serialConnection;
    }

    @Override
    public void initialize() throws Exception {

        serialConnection.subscribe(this);
    }

    @Override
    public void move(MoveCommand command) {
        executeCommand(command);
    }

    @Override
    public void turn(TurnCommand command) {
        executeCommand(command);
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
                listeners.forEach(ControllerListener::onStart);
                break;
            case INVALID_REQUEST:
                listeners.forEach(ControllerListener::onInvalidRequest);
                break;
            default:
                try {
                    MotionState state = MotionState.valueOf(line);
                    listeners.forEach(controllerListener -> controllerListener.stateReceived(state));
                } catch (IllegalArgumentException ignored) {
                    listeners.forEach(listener -> listener.messageReceived(line));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void subscribe(ControllerListener listener) {
        listeners.add(listener);
    }

    @Override
    public void requestState() {
        sendRequest(STATE_REQUEST);
    }

    private void executeCommand(CommandLine command) {
        String commandLine = command.getLine();
        sendRequest(commandLine);
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


