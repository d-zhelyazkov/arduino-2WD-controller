package com.xrc.arduino.twoWD;

import com.xrc.arduino.serial.SerialConnection;
import com.xrc.arduino.twoWD.impl.Arduino2WDController;

public class ControllerFactory {
    private static ControllerFactory instance = new ControllerFactory();

    public static ControllerFactory getInstance() {
        return instance;
    }

    private ControllerFactory() {
    }

    public Controller getController(SerialConnection connection) {
        return new Arduino2WDController(connection);
    }
}
