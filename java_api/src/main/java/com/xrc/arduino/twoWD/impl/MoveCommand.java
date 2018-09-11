package com.xrc.arduino.twoWD.impl;

import com.xrc.arduino.serial.Commons;
import com.xrc.arduino.twoWD.MotionCommand;
import com.xrc.command.CommandLineArgument;
import com.xrc.command.CommandLineBuilder;
import com.xrc.util.StringUtils;

public class MoveCommand implements MotionCommand {

    public static final Direction DEFAULT_DIRECTION = Direction.FORWARD;

    public static final Metric DEFAULT_METRIC = Metric.UNITS;

    private static final String COMMAND = "MOVE";

    private final float value;

    private final Metric metric;

    private final Direction direction;

    private MoveCommand(float value, Metric metric, Direction direction) {
        this.value = value;
        this.metric = (metric != null) ? metric : DEFAULT_METRIC;
        this.direction = (direction != null) ? direction : DEFAULT_DIRECTION;
    }

    @Override
    public String getLine() {
        CommandLineBuilder commandBuilder = new CommandLineBuilder()
                .argument(COMMAND)
                .argument(Commons.FLOAT_PARSE_FORMAT.format(value));

        if (metric != DEFAULT_METRIC) {
            commandBuilder.argument(metric);
        }
        if (direction != DEFAULT_DIRECTION) {
            commandBuilder.argument(direction);
        }

        return commandBuilder.buildCommandLine();
    }

    public float getValue() {
        return value;
    }

    public Metric getMetric() {
        return metric;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        String parameterStr = StringUtils.join(" ", value, metric, direction);
        return String.format("MoveCommand[%s]", parameterStr);
    }

    public enum Direction implements CommandLineArgument {
        FORWARD("FWRD"),
        BACKWARD("BCKWRD");

        private final String commandLineArgument;

        Direction(String commandLineArgument) {
            this.commandLineArgument = commandLineArgument;
        }

        @Override
        public String getArgument() {
            return commandLineArgument;
        }

        public static Direction getOpposite(Direction direction) {
            switch (direction) {
            case FORWARD:
                return BACKWARD;
            case BACKWARD:
                return FORWARD;
            default:
                return direction;
            }
        }
    }

    public enum Metric implements CommandLineArgument {
        UNITS("UNIT"),
        WHEEL_ROTATIONS("ROT");

        private final String commandLineArgument;

        Metric(String commandLineArgument) {
            this.commandLineArgument = commandLineArgument;
        }

        @Override
        public String getArgument() {
            return commandLineArgument;
        }
    }

    public static class Builder {
        private float value = 0.0f;

        private Metric metric = null;

        private Direction direction = null;

        public Builder value(float value) {
            this.value = value;
            return this;
        }

        public Builder metric(Metric metric) {
            this.metric = metric;
            return this;
        }

        public Builder direction(Direction direction) {
            this.direction = direction;
            return this;
        }

        public MoveCommand build() {
            return new MoveCommand(value, metric, direction);
        }
    }
}
