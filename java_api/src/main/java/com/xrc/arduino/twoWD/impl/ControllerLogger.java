package com.xrc.arduino.twoWD.impl;

import com.xrc.arduino.twoWD.Controller;
import com.xrc.arduino.twoWD.ControllerListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ControllerLogger implements ControllerListener {

    private final Logger logger = LogManager.getLogger();

    @Override
    public void onStart() {
        logger.info("Controller started.");
    }

    @Override
    public void stateReceived(Controller.MotionState state) {
        logger.info("Controller state: " + state);
    }

    @Override
    public void onInvalidRequest() {
        logger.warn("Invalid request sent to the controller.");
    }

    @Override
    public void messageReceived(String message) {
        logger.info("CONTROLLER: " + message);
    }
}
