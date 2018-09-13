package com.xrc.arduino;

import com.xrc.arduino.twoWD.Controller;
import com.xrc.arduino.twoWD.ControllerListener;
import com.xrc.arduino.twoWD.MotionCommand;
import com.xrc.arduino.twoWD.impl.TurnCommand;
import com.xrc.arduino.twoWD.task.ControllerInitTask;
import com.xrc.arduino.twoWD.task.MotionCommandTask;

import java.time.Duration;

public class Arduino2WDProgram {

    private static final Duration DURATION_10S = Duration.ofSeconds(10);

    private static final MotionCommand DUMMY_COMMAND = new TurnCommand.Builder()
            .value(90)
            .build();

    private static Controller twoWDController;

    public static void main(String[] args) {

        Thread t = new Thread(() -> {
            try {
                ControllerInitTask initTask = new ControllerInitTask();
                initTask.execute();
                twoWDController = initTask.getController();
                twoWDController.subscribe(new ControllerListener() {

                    @Override
                    public void stateReceived(Controller.MotionState state) {
                        try {
                            if (state != Controller.MotionState.STOPPED) {
                                return;
                            }

                            executeMotionCommand();
                        } catch (Exception ignored) {
                        }
                    }
                });

                executeMotionCommand();

            } catch (Exception e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        });
        t.run();
    }

    private static void executeMotionCommand() throws Exception {
        MotionCommandTask commandTask =
                new MotionCommandTask(twoWDController, DUMMY_COMMAND, DURATION_10S);

        commandTask.execute();
    }
}
