package com.xrc.arduino.twoWD;

import com.xrc.arduino.twoWD.command.Constants;
import com.xrc.arduino.twoWD.impl.MoveCommand;
import com.xrc.arduino.twoWD.impl.TurnCommand;
import org.junit.jupiter.api.Test;

class MinimumTests extends ControllerTestBase {

    @Test
    void testMinUnits() throws Exception {
        MoveCommand command = new MoveCommand.Builder()
                .value(Constants.MIN_UNITS)
                .metric(MoveCommand.Metric.UNITS)
                .build();

        testCommand(command);
    }

    @Test
    void testMinRotations() throws Exception {
        MoveCommand command = new MoveCommand.Builder()
                .value(Constants.MIN_WHEEL_ROTATIONS)
                .metric(MoveCommand.Metric.WHEEL_ROTATIONS)
                .build();

        testCommand(command);
    }

    @Test
    void testMinDegrees() throws Exception {
        TurnCommand command = new TurnCommand.Builder()
                .value(Constants.MIN_DEGREES)
                .metric(TurnCommand.Metric.DEGREES)
                .build();

        testCommand(command);
    }
}
