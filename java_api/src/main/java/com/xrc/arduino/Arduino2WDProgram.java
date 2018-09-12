package com.xrc.arduino;

import com.xrc.arduino.serial.ConnectionFactory;
import com.xrc.arduino.serial.SerialConnection;
import com.xrc.arduino.twoWD.Controller;
import com.xrc.arduino.twoWD.ControllerListener;
import com.xrc.arduino.twoWD.MotionCommand;
import com.xrc.arduino.twoWD.impl.Arduino2WDController;
import com.xrc.arduino.twoWD.impl.TurnCommand;

public class Arduino2WDProgram {

    private static final MotionCommand DUMMY_COMMAND = new TurnCommand.Builder()
            .value(90)
            .build();

    public static void main(String[] args) {

        Thread t = new Thread(() -> {

            ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
            try {
                SerialConnection serialConnection = connectionFactory.getConnection();
                Controller twoWDController = new Arduino2WDController(serialConnection);
                twoWDController.subscribe(new ControllerListener() {
                    @Override
                    public void onStart() {
                        System.out.println("program started");
                        twoWDController.requestState();
                    }

                    @Override
                    public void stateReceived(Controller.MotionState state) {
                        System.out.println("State received: " + state);
                        twoWDController.sendMotionCommand(DUMMY_COMMAND);
                    }

                    @Override
                    public void onInvalidRequest() {
                        System.out.println("invalid request");
                    }

                    @Override
                    public void messageReceived(String message) {
                        System.out.println("New message: " + message);
                    }
                });

                serialConnection.initialize();
                twoWDController.initialize();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t.run();
    }
}
