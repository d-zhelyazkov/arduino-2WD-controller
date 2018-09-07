package com.xrc.arduino.twoWD;

import com.xrc.command.CommandLine;
import com.xrc.command.CommandLineArgument;
import com.xrc.command.CommandLineBuilder;

public class MoveCommand implements CommandLine {
    private static final String COMMAND = "MOVE";

    private final float value;

    private final Metric metric;

    private final Direction direction;

    private MoveCommand(float value, Metric metric, Direction direction) {
        this.value = value;
        this.metric = metric;
        this.direction = direction;
    }

    @Override
    public String getLine() {
        return new CommandLineBuilder()
                .arguments(COMMAND, value, metric, direction)
                .buildCommandLine();
    }

    enum Direction implements CommandLineArgument {
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
    }

    enum Metric implements CommandLineArgument {
        UNITS("UNIT"),
        WHEEL_ROTATIONS("ROT");

        private final String commandLineArgument;

        Metric(String commandLineArgument) {
            this.commandLineArgument = commandLineArgument;
        }

        @Override
        public String getArgument() {
            return null;
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
