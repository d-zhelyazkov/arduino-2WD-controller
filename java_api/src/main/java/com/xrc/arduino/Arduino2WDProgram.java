package com.xrc.arduino;

import com.xrc.arduino.serial.ConnectionFactory;
import com.xrc.arduino.serial.SerialConnection;
import com.xrc.arduino.twoWD.Controller;
import com.xrc.arduino.twoWD.ControllerListener;
import com.xrc.arduino.twoWD.MotionCommand;
import com.xrc.arduino.twoWD.impl.Arduino2WDController;
import com.xrc.arduino.twoWD.impl.ControllerLogger;
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
                twoWDController.subscribe(new ControllerLogger());

                twoWDController.subscribe(new ControllerListener() {
                    @Override
                    public void onStart() {
                        twoWDController.requestState();
                    }

                    @Override
                    public void stateReceived(Controller.MotionState state) {
                        twoWDController.sendMotionCommand(DUMMY_COMMAND);
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
