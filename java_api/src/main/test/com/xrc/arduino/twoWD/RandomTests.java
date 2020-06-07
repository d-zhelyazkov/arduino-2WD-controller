package com.xrc.arduino.twoWD;

import com.xrc.arduino.twoWD.command.suppliers.FastMoveCommandSupplier;
import com.xrc.arduino.twoWD.command.suppliers.FastTurnCommandSupplier;
import com.xrc.util.function.RandomSupplierComposite;
import org.junit.jupiter.api.RepeatedTest;

import java.util.function.Supplier;

class RandomTests extends ControllerTestBase {

    private final Supplier<MotionCommand> randCommandSupplier =
            new RandomSupplierComposite<>(
                    new FastMoveCommandSupplier(),
                    new FastTurnCommandSupplier()
//                    new RandomTurnCommandSupplier(
//                            new RandomFloatsSupplier(Constants.MAX_DEGREES),
//                            null, null,
//                            () -> TurnCommand.Metric.DEGREES,
//                            new RandomEnumSupplier<>(TurnCommand.Direction.class)
//                    )
            );

    @RepeatedTest(value = 10)
    void testRandomCommand() throws Exception {

        MotionCommand command = randCommandSupplier.get();
        testCommand(command);

    }
}
