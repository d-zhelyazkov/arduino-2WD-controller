package com.xrc.arduino.twoWD.command.suppliers;

import com.xrc.arduino.twoWD.command.Constants;
import com.xrc.util.function.RandomFloatsSupplier;

public class FastMoveCommandSupplier extends RandomMoveCommandSupplier {

    public FastMoveCommandSupplier() {
        super(
                new RandomFloatsSupplier(Constants.AVG_WHEEL_ROTATIONS),
                new RandomFloatsSupplier(Constants.AVG_UNITS));
    }
}
