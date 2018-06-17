#include "StandByState.h"
#include "ActionState.h"


void StandByState::init() {
    Serial.println("READY");
}

bool StandByState::serialEvent() {

    String action = Serial.readString();
    if (ActionPreState::canHandle(action)) {
        context.setState(
            new ActionPreState(context, action));
    }
    else {
        return false;
    }

    return true;
}
