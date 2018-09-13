package com.xrc.arduino.twoWD.task;

import com.xrc.arduino.twoWD.Controller;
import com.xrc.arduino.twoWD.MotionCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MotionCommandTask {

    private static final Duration START_TIMEOUT = Duration.ofSeconds(10);

    private final Logger logger = LogManager.getLogger();

    private final MotionCommand command;

    private final Controller controller;

    private final Duration timeout;

    private Instant startTime;

    private Instant endTime;

    public MotionCommandTask(
            Controller controller, MotionCommand command, Duration timeout) {

        this.command = command;
        this.controller = controller;
        this.timeout = timeout;
    }

    public void execute() throws Exception {
        try {
            logger.info("Will execute command " + command);

            innerExecute();

            logger.info(String.format(
                    "Successfully executed command %s. Task status: %s.", command, getStatusStr()));
        } catch (Exception e) {
            logger.error("Failed to execute command " + command, e);
            throw e;
        }

    }

    private void innerExecute() throws Exception {
        start();

        waitCompletion();
    }

    private void start() throws Exception {
        if (isStarted()) {
            throw new Exception("Command already started.");
        }

        CountDownLatch startLatch = new CountDownLatch(1);
        MotionCommandListener startListener = new MotionCommandListener(startLatch) {

            @Override
            public void stateReceived(Controller.MotionState state) {
                if (state == Controller.MotionState.STOPPED) {
                    String error = "Expected motion but instead the controller stopped.";
                    exception = new UnexpectedStateException(error);
                }

                latch.countDown();
            }

            @Override
            public void onInvalidRequest() {
                String error = "Invalid command sent to the controller!";
                exception = new InvalidCommandException(error);
                latch.countDown();
            }
        };
        controller.subscribe(startListener);

        logger.debug(String.format("Sending command '%s'.", command.getLine()));
        Instant sendTime = Instant.now();
        controller.sendMotionCommand(command);

        boolean success = startLatch.await(START_TIMEOUT.getSeconds(), TimeUnit.SECONDS);
        controller.unsubscribe(startListener);

        if (!success) {
            String errorMsg = "Controller failed to start command in " + START_TIMEOUT;
            throw new TimeoutException(errorMsg);
        }

        Exception exception = startListener.getException();
        if (exception != null) {
            throw exception;
        }

        startTime = sendTime;
    }

    private void waitCompletion() throws Exception {

        CountDownLatch completionLatch = new CountDownLatch(1);
        MotionCommandListener stopListener = new MotionCommandListener(completionLatch) {

            @Override
            public void stateReceived(Controller.MotionState state) {
                if (state != Controller.MotionState.STOPPED) {
                    String error =
                            "Expected the controller to stop. Instead the controller started: motion State - " + state;
                    exception = new UnexpectedStateException(error);
                }

                latch.countDown();
            }
        };
        controller.subscribe(stopListener);

        boolean success = completionLatch.await(timeout.getSeconds(), TimeUnit.SECONDS);
        controller.unsubscribe(stopListener);

        if (!success) {
            String errorMsg = "Controller failed to complete command in " + timeout;
            throw new TimeoutException(errorMsg);
        }

        Exception exception = stopListener.getException();
        if (exception != null) {
            throw exception;
        }

        endTime = Instant.now();
    }

    public Status getStatus() {
        if (startTime == null) {
            return Status.NOT_STARTED;
        } else if (endTime == null) {
            return Status.STARTED;
        } else {
            return Status.COMPLETED;
        }
    }

    public Duration getExecutionTime() {

        if ((startTime == null) || (endTime == null)) {
            return Duration.ZERO;
        }

        return Duration.between(startTime, endTime);
    }

    public String getStatusStr() {
        return "MotionCommandTask{" +
                "status = " + getStatus() +
                ", execution time = " + getExecutionTime() +
                ", startTime = " + startTime +
                ", endTime = " + endTime +
                '}';
    }

    private boolean isStarted() {
        return (startTime != null);
    }

    public enum Status {
        NOT_STARTED,
        STARTED,
        COMPLETED
    }

}
