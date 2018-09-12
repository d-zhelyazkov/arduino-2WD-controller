package com.xrc.arduino.twoWD.command;

import com.xrc.arduino.twoWD.MotionCommand;
import com.xrc.arduino.twoWD.impl.MoveCommand;
import com.xrc.arduino.twoWD.impl.TurnCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;

public class ExecutionTimeResolver {

    private static final Duration DURATION_10S = Duration.ofSeconds(10);

    private static final Duration DURATION_30S = Duration.ofSeconds(30);

    private final Logger logger = LogManager.getLogger();

    public Duration getCommandTimeout(MotionCommand command) {
        Duration timeout;
        if (command instanceof TurnCommand) {
            timeout = getCommandTimeout((TurnCommand) command);
        } else if (command instanceof MoveCommand) {
            timeout = getCommandTimeout((MoveCommand) command);
        } else {
            timeout = Duration.ZERO;
        }

        logger.debug(String.format(
                "Timeout for command %s is %s.", command, timeout));
        return timeout;
    }

    public Duration getCommandTimeout(TurnCommand command) {
        float value = command.getValue();
        switch (command.getMetric()) {
        case UNITS:
            return (value < Constants.AVG_UNITS) ? DURATION_10S : DURATION_30S;
        case WHEEL_ROTATIONS:
            return (value < Constants.AVG_WHEEL_ROTATIONS) ? DURATION_10S : DURATION_30S;
        case DEGREES:
            return DURATION_10S;
        default:
            return Duration.ZERO;
        }
    }

    public Duration getCommandTimeout(MoveCommand command) {
        float value = command.getValue();
        switch (command.getMetric()) {
        case UNITS:
            return (value < Constants.AVG_UNITS) ? DURATION_10S : DURATION_30S;
        case WHEEL_ROTATIONS:
            return (value < Constants.AVG_WHEEL_ROTATIONS) ? DURATION_10S : DURATION_30S;
        default:
            return Duration.ZERO;
        }
    }
}
