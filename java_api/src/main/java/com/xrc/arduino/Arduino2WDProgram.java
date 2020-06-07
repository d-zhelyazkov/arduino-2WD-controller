package com.xrc.arduino;

import com.xrc.arduino.serial.ConnectionFactory;
import com.xrc.arduino.serial.SerialConnection;
import com.xrc.arduino.twoWD.Controller;
import com.xrc.arduino.twoWD.MotionCommand;
import com.xrc.arduino.twoWD.impl.Arduino2WDController;
import com.xrc.arduino.twoWD.impl.TurnCommand;
import com.xrc.arduino.twoWD.task.ControllerInitTask;
import com.xrc.arduino.twoWD.task.MotionCommandTask;

import java.time.Duration;

public class Arduino2WDProgram {

    private static final Duration DURATION_10S = Duration.ofSeconds(10);

    private static final MotionCommand DUMMY_COMMAND = new TurnCommand.Builder()
            .value(90)
            .build();

    public static void main(String[] args) throws Exception {


        try (SerialConnection connection =
                     ConnectionFactory.getInstance().getConnection()) {
            Arduino2WDController twoWDController =
                    new Arduino2WDController(connection);

            ControllerInitTask initTask = new ControllerInitTask(twoWDController);
            initTask.execute();

            //noinspection InfiniteLoopStatement
            while (true) {
                new MotionCommandTask(twoWDController, DUMMY_COMMAND, DURATION_10S)
                        .execute();

            }
        }
    }

}
