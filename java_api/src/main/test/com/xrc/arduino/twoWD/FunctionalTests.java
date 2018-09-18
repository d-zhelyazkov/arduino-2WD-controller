package com.xrc.arduino.twoWD;

import com.xrc.arduino.twoWD.impl.TurnCommand;
import org.junit.jupiter.api.Test;

class FunctionalTests extends ControllerTestBase {

    private static final MotionCommand DUMMY_COMMAND = new TurnCommand.Builder()
            .value(90)
            .build();


    @Test
    void test() throws Exception {
        testCommand(DUMMY_COMMAND);
    }


}
