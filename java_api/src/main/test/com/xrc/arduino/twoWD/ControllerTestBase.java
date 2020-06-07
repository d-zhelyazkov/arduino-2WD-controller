package com.xrc.arduino.twoWD;

import com.xrc.arduino.serial.ConnectionFactory;
import com.xrc.arduino.serial.SerialConnection;
import com.xrc.arduino.twoWD.command.ExecutionTimeResolver;
import com.xrc.arduino.twoWD.command.MotionStateResolver;
import com.xrc.arduino.twoWD.impl.Arduino2WDController;
import com.xrc.arduino.twoWD.task.ControllerInitTask;
import com.xrc.arduino.twoWD.task.InvalidCommandException;
import com.xrc.arduino.twoWD.task.MotionCommandTask;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

import java.time.Duration;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ControllerTestBase {

    private final MotionStateResolver stateResolver = new MotionStateResolver();
    private final ExecutionTimeResolver timeResolver = new ExecutionTimeResolver();

    private Arduino2WDController controller;

    @BeforeAll
    void initialize() throws Exception {
        SerialConnection connection =
                ConnectionFactory.getInstance().getConnection();
        controller = new Arduino2WDController(connection);

        ControllerInitTask initTask = new ControllerInitTask(controller);
        initTask.execute();
    }

    void testCommand(MotionCommand command) throws Exception {
        Controller.MotionState expectedState = stateResolver.getExpectedState(command);
        Duration timeout = timeResolver.getCommandTimeout(command);
        FirstStateListener firstStateListener = new FirstStateListener();
        controller.subscribe(firstStateListener);

        try {
            MotionCommandTask commandTask =
                    new MotionCommandTask(controller, command, timeout);
            commandTask.execute();

        } catch (InvalidCommandException e) {
            if (expectedState != Controller.MotionState.STOPPED) {
                throw e;
            }
        } finally {
            controller.unsubscribe(firstStateListener);
        }

        Assertions.assertEquals(expectedState, firstStateListener.firstState, "Invalid motion state.");
    }

    @AfterAll
    void dispose() throws Exception {
        controller.getConnection().close();
    }

    private class FirstStateListener implements ControllerListener {

        private Controller.MotionState firstState = Controller.MotionState.STOPPED;

        @Override
        public void stateReceived(Controller.MotionState state) {
            firstState = state;
            controller.unsubscribe(this);
        }
    }
}