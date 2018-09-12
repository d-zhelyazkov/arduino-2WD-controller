package com.xrc.arduino.twoWD.command.suppliers;

import com.xrc.arduino.twoWD.MotionCommand;
import com.xrc.arduino.twoWD.impl.TurnCommand;
import com.xrc.util.function.RandomEnumSupplier;

import java.util.function.Supplier;

public class RandomTurnCommandSupplier implements Supplier<MotionCommand> {

    private final Supplier<Float> degreesSupplier;

    private final Supplier<Float> rotationsSupplier;

    private final Supplier<Float> unitsSupplier;

    private final Supplier<TurnCommand.Metric> metricSupplier;

    private final Supplier<TurnCommand.Direction> directionSupplier;

    public RandomTurnCommandSupplier(
            Supplier<Float> degreesSupplier,
            Supplier<Float> rotationsSupplier,
            Supplier<Float> unitsSupplier) {

        this(degreesSupplier, rotationsSupplier, unitsSupplier,
                new RandomEnumSupplier<>(TurnCommand.Metric.class, true),
                new RandomEnumSupplier<>(TurnCommand.Direction.class, true));

    }

    public RandomTurnCommandSupplier(
            Supplier<Float> degreesSupplier,
            Supplier<Float> rotationsSupplier,
            Supplier<Float> unitsSupplier,
            Supplier<TurnCommand.Metric> metricSupplier,
            Supplier<TurnCommand.Direction> directionSupplier) {

        this.metricSupplier = metricSupplier;
        this.directionSupplier = directionSupplier;
        this.degreesSupplier = degreesSupplier;
        this.rotationsSupplier = rotationsSupplier;
        this.unitsSupplier = unitsSupplier;
    }

    @Override
    public TurnCommand get() {

        TurnCommand.Metric metric = metricSupplier.get();
        float value = getValue(metric);
        TurnCommand.Direction direction = directionSupplier.get();

        return new TurnCommand.Builder()
                .value(value)
                .metric(metric)
                .direction(direction)
                .build();
    }

    private float getValue(TurnCommand.Metric metric) {

        if (metric == null)
            metric = TurnCommand.DEFAULT_METRIC;

        switch (metric) {
        case UNITS:
            return unitsSupplier.get();
        case WHEEL_ROTATIONS:
            return rotationsSupplier.get();
        case DEGREES:
            return degreesSupplier.get();
        default:
            return 0;
        }
    }
}
