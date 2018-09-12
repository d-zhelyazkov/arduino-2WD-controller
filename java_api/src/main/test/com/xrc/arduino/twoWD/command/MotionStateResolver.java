package com.xrc.arduino.twoWD.command;

import com.xrc.arduino.serial.Commons;
import com.xrc.arduino.twoWD.Controller;
import com.xrc.arduino.twoWD.MotionCommand;
import com.xrc.arduino.twoWD.impl.MoveCommand;
import com.xrc.arduino.twoWD.impl.TurnCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MotionStateResolver {

    private final Logger logger = LogManager.getLogger();

    public Controller.MotionState getExpectedState(MotionCommand command) {
        Controller.MotionState expectedState;
        if (command instanceof TurnCommand) {
            expectedState = getExpectedState((TurnCommand) command);
        } else if (command instanceof MoveCommand) {
            expectedState = getExpectedState((MoveCommand) command);
        } else {
            expectedState = Controller.MotionState.STOPPED;
        }

        logger.debug(String.format(
                "Expected state for command %s is %s.", command, expectedState));
        return expectedState;
    }

    public Controller.MotionState getExpectedState(TurnCommand command) {
        TurnCommand.Metric metric = command.getMetric();
        float value = command.getValue();
        if (metric == TurnCommand.Metric.DEGREES) {
            value = normalizeDegrees(value);
        }
        if (isLowValue(value, metric)) {
            return Controller.MotionState.STOPPED;
        }

        TurnCommand.Direction direction = command.getDirection();
        if (value < 0) {
            direction = TurnCommand.Direction.getOpposite(direction);
        }

        switch (direction) {
        case LEFT:
            return Controller.MotionState.TURNING_LEFT;
        case RIGHT:
            return Controller.MotionState.TURNING_RIGHT;
        default:
            return Controller.MotionState.STOPPED;
        }
    }

    public Controller.MotionState getExpectedState(MoveCommand command) {
        float value = command.getValue();
        if (isLowValue(value, command.getMetric())) {
            return Controller.MotionState.STOPPED;
        }

        MoveCommand.Direction direction = command.getDirection();
        if (value < 0) {
            direction = MoveCommand.Direction.getOpposite(direction);
        }

        switch (direction) {

        case FORWARD:
            return Controller.MotionState.MOVING_FORWARD;
        case BACKWARD:
            return Controller.MotionState.MOVING_BACKWARD;
        default:
            return Controller.MotionState.STOPPED;
        }
    }

    private boolean isLowValue(float value, TurnCommand.Metric metric) {
        value = preprocessValue(value);
        switch (metric) {
        case UNITS:
            return (value < Constants.MIN_UNITS);
        case WHEEL_ROTATIONS:
            return (value < Constants.MIN_WHEEL_ROTATIONS);
        case DEGREES:
            return (value < Constants.MIN_DEGREES);
        default:
            return false;
        }
    }

    private boolean isLowValue(float value, MoveCommand.Metric metric) {
        value = preprocessValue(value);
        switch (metric) {
        case UNITS:
            return (value < Constants.MIN_UNITS);
        case WHEEL_ROTATIONS:
            return (value < Constants.MIN_WHEEL_ROTATIONS);
        default:
            return false;
        }
    }

    private float preprocessValue(float value) {
        String valueStr = Commons.FLOAT_PARSE_FORMAT.format(value);
        value = Float.parseFloat(valueStr);

        value = Math.abs(value);

        return value;
    }

    private float normalizeDegrees(float degrees) {
        float normalizedDegrees = degrees % 360;                   //[-360:360]
        if (normalizedDegrees < -180) {
            normalizedDegrees += 360;
        } else if (normalizedDegrees > 180) {
            normalizedDegrees -= 360;
        }                                               //[-180;180]

        logger.debug(String.format("Degrees %f normalized to %f", degrees, normalizedDegrees));
        return normalizedDegrees;
    }
}
