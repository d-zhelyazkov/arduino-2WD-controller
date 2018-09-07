package com.xrc.arduino.twoWD;

import com.xrc.command.CommandLine;
import com.xrc.command.CommandLineArgument;
import com.xrc.command.CommandLineBuilder;
import com.xrc.util.StringUtils;

public class TurnCommand implements CommandLine {
    private static final String COMMAND = "TURN";

    private final float value;

    private final Metric metric;

    private final Direction direction;

    private TurnCommand(float value, Metric metric, Direction direction) {
        this.value = value;
        this.metric = metric;
        this.direction = direction;
    }

    public String toString() {
        return StringUtils.join(" ", COMMAND, value, metric, direction);
    }

    @Override
    public String getLine() {
        return new CommandLineBuilder()
                .arguments(COMMAND, value, metric, direction)
                .buildCommandLine();
    }

    enum Direction implements CommandLineArgument {
        LEFT("LEFT"),
        RIGHT("RIGHT");

        private final String commandLineArgument;

        Direction(String commandLineArgument) {
            this.commandLineArgument = commandLineArgument;
        }

        @Override
        public String getArgument() {
            return commandLineArgument;
        }
    }

    enum Metric implements CommandLineArgument {
        UNITS("UNIT"),
        WHEEL_ROTATIONS("ROT"),
        DEGREES("DEG");

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

        public TurnCommand.Builder value(float value) {
            this.value = value;
            return this;
        }

        public TurnCommand.Builder metric(Metric metric) {
            this.metric = metric;
            return this;
        }

        public TurnCommand.Builder direction(Direction direction) {
            this.direction = direction;
            return this;
        }

        public TurnCommand build() {
            return new TurnCommand(value, metric, direction);
        }

    }
}
