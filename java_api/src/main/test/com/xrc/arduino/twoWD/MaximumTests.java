package com.xrc.arduino.twoWD;

import com.xrc.arduino.twoWD.command.Constants;
import com.xrc.arduino.twoWD.impl.MoveCommand;
import com.xrc.arduino.twoWD.impl.TurnCommand;
import org.junit.jupiter.api.Test;

class MaximumTests extends ControllerTestBase {

    @Test
    void testMaxDegrees() throws Exception {
        TurnCommand command = new TurnCommand.Builder()
                .value(Constants.MAX_DEGREES)
                .metric(TurnCommand.Metric.DEGREES)
                .build();

        testCommand(command);
    }

    @Test
    void testMaxUnits() throws Exception {
        MoveCommand command = new MoveCommand.Builder()
                .value(Constants.MAX_UNITS)
                .metric(MoveCommand.Metric.UNITS)
                .build();

        testCommand(command);
    }

    @Test
    void testMaxRotations() throws Exception {
        TurnCommand command = new TurnCommand.Builder()
                .value(Constants.MAX_WHEEL_ROTATIONS)
                .metric(TurnCommand.Metric.WHEEL_ROTATIONS)
                .build();

        testCommand(command);
    }
}
