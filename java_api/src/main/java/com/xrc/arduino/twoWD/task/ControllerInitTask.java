package com.xrc.arduino.twoWD.task;

import com.xrc.arduino.twoWD.Controller;
import com.xrc.arduino.twoWD.ControllerListener;
import com.xrc.arduino.twoWD.impl.Arduino2WDController;
import com.xrc.arduino.twoWD.impl.ControllerLogger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ControllerInitTask {

    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    private final Logger logger = LogManager.getLogger();

    private final Arduino2WDController controller;

    public ControllerInitTask(Arduino2WDController controller) {
        this.controller = controller;
    }

    public void execute() throws Exception {

        try {
            controller.subscribe(new ControllerLogger());

            CountDownLatch startLatch = new CountDownLatch(1);
            ControllerListener startListener = new ControllerListener() {
                @Override
                public void onStart() {
                    startLatch.countDown();
                }
            };
            controller.subscribe(startListener);

            controller.getConnection().initialize();
            controller.initialize();

            boolean success = startLatch.await(TIMEOUT.getSeconds(), TimeUnit.SECONDS);
            controller.unsubscribe(startListener);

            if (!success) {
                String errorMsg = "Controller failed to start in " + TIMEOUT;
                throw new TimeoutException(errorMsg);
            }

            logger.info("Successfully initialized controller.");

        } catch (Exception e) {
            logger.error("Failed to initialize the controller.", e);
            throw e;
        }
    }

    public Controller getController() {
        return controller;
    }
}
