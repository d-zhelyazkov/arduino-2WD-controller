package com.xrc.arduino;

import com.xrc.arduino.serial.ConnectionFactory;
import com.xrc.arduino.serial.SerialConnection;
import com.xrc.arduino.twoWD.Controller;
import com.xrc.arduino.twoWD.ControllerFactory;
import com.xrc.arduino.twoWD.ControllerListener;

public class Arduino2WDProgram {

    public static void main(String[] args) {

        Thread t = new Thread(() -> {

            try {
                SerialConnection serialConnection = ConnectionFactory.getInstance().getConnection();
                Controller twoWDController = ControllerFactory.getInstance().getController(serialConnection);
                twoWDController.subscribe(new ControllerListener() {
                    @Override
                    public void onInit() {
                        System.out.println("program started");
                    }

                    @Override
                    public void onReady() {
                        System.out.println("ready");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        twoWDController.move(Controller.MoveDirection.FORWARD, 0.5f);
                    }

                    @Override
                    public void onMove() {
                        System.out.println(twoWDController.getState());
                        twoWDController.turn(Controller.TurnDirection.RIGHT, 0.5f);
                    }

                    @Override
                    public void onInvalidCommand() {
                        System.out.println("invalid command");
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
