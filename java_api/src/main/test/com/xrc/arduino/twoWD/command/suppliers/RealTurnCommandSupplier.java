package com.xrc.arduino.twoWD.command.suppliers;

import com.xrc.arduino.twoWD.command.Constants;
import com.xrc.util.function.RandomFloatsSupplier;

public class RealTurnCommandSupplier extends RandomTurnCommandSupplier {

    public RealTurnCommandSupplier() {
        super(
                new RandomFloatsSupplier(Constants.MAX_DEGREES),
                new RandomFloatsSupplier(Constants.MAX_WHEEL_ROTATIONS),
                new RandomFloatsSupplier(Constants.MAX_UNITS));
    }
}
