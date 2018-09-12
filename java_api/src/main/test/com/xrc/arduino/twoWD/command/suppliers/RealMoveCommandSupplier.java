package com.xrc.arduino.twoWD.command.suppliers;

import com.xrc.arduino.twoWD.command.Constants;
import com.xrc.util.function.RandomFloatsSupplier;

public class RealMoveCommandSupplier extends RandomMoveCommandSupplier {

    public RealMoveCommandSupplier() {
        super(
                new RandomFloatsSupplier(Constants.MAX_WHEEL_ROTATIONS),
                new RandomFloatsSupplier(Constants.MAX_UNITS));
    }
}
