package com.xrc.arduino.twoWD.command.suppliers;

import com.xrc.arduino.twoWD.command.Constants;
import com.xrc.util.function.RandomFloatsSupplier;

public class FastTurnCommandSupplier extends RandomTurnCommandSupplier {

    public FastTurnCommandSupplier() {
        super(
                new RandomFloatsSupplier(Constants.MAX_DEGREES),
                new RandomFloatsSupplier(Constants.AVG_WHEEL_ROTATIONS),
                new RandomFloatsSupplier(Constants.AVG_UNITS));
    }
}
