package com.xrc.arduino.twoWD.task;

import com.xrc.arduino.twoWD.ControllerListener;

import java.util.concurrent.CountDownLatch;

public class MotionCommandListener implements ControllerListener {

    protected final CountDownLatch latch;

    protected Exception exception = null;

    public MotionCommandListener(CountDownLatch latch) {

        this.latch = latch;
    }

    @Override
    public void onStart() {
        String error = "Controller restarted unexpectedly.";
        exception = new UnexpectedRestartException(error);

        latch.countDown();
    }

    public Exception getException() {
        return exception;
    }
}