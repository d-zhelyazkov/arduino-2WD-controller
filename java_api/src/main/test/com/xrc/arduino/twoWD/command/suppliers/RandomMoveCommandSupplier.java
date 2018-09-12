package com.xrc.arduino.twoWD.command.suppliers;

import com.xrc.arduino.twoWD.MotionCommand;
import com.xrc.arduino.twoWD.impl.MoveCommand;
import com.xrc.util.function.RandomEnumSupplier;

import java.util.function.Supplier;

public class RandomMoveCommandSupplier implements Supplier<MotionCommand> {

    private final Supplier<MoveCommand.Metric> metricSupplier =
            new RandomEnumSupplier<>(MoveCommand.Metric.class, true);

    private final Supplier<MoveCommand.Direction> directionSupplier =
            new RandomEnumSupplier<>(MoveCommand.Direction.class, true);

    private final Supplier<Float> rotationsSupplier;

    private final Supplier<Float> unitsSupplier;

    public RandomMoveCommandSupplier(
            Supplier<Float> rotationsSupplier, Supplier<Float> unitsSupplier) {

        this.rotationsSupplier = rotationsSupplier;
        this.unitsSupplier = unitsSupplier;
    }

    @Override
    public MoveCommand get() {
        MoveCommand.Metric metric = metricSupplier.get();
        float value = getValue(metric);
        MoveCommand.Direction direction = directionSupplier.get();

        return new MoveCommand.Builder()
                .value(value)
                .metric(metric)
                .direction(direction)
                .build();

    }

    private float getValue(MoveCommand.Metric metric) {
        if (metric == null)
            metric = MoveCommand.DEFAULT_METRIC;

        switch (metric) {
        case WHEEL_ROTATIONS:
            return rotationsSupplier.get();
        case UNITS:
            return unitsSupplier.get();
        default:
            return 0;
        }

    }
}
