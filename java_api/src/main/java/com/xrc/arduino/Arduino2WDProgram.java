package com.xrc.arduino;

import com.xrc.arduino.serial.ConnectionFactory;
import com.xrc.arduino.serial.SerialConnection;
import com.xrc.arduino.twoWD.Controller;
import com.xrc.arduino.twoWD.ControllerFactory;
import com.xrc.arduino.twoWD.ControllerListener;
import com.xrc.arduino.twoWD.TurnCommand;

public class Arduino2WDProgram {

    private static final TurnCommand DUMMY_COMMAND = new TurnCommand.Builder()
            .value(90)
            .build();

    public static void main(String[] args) {

        Thread t = new Thread(() -> {

            try {
                SerialConnection serialConnection = ConnectionFactory.getInstance().getConnection();
                Controller twoWDController = ControllerFactory.getInstance().getController(serialConnection);
                twoWDController.subscribe(new ControllerListener() {
                    @Override
                    public void onStart() {
                        System.out.println("program started");
                        twoWDController.turn(DUMMY_COMMAND);
                    }

                    @Override
                    public void onStateChange() {
                        System.out.println(twoWDController.getState());
                        twoWDController.turn(DUMMY_COMMAND);
                    }

                    @Override
                    public void onInvalidCommand() {
                        System.out.println("invalid request");
                    }

                    @Override
                    public void onMessage(String message) {
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
