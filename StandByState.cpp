#include "StandByState.h"
#include "ActionState.h"


void StandByState::init() {
    Serial.println("READY");
}

bool StandByState::serialEvent() {

    String action = Serial.readString();
    if (action.equals("MOVE_FORWARD")) {
        context.setState(
            new ActionPreState(context, action));
    }
    else {
        Serial.println("UNKNOWN_ACTION");
        return false;
    }

    return true;
}
