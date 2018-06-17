#include "StandByState.h"
#include "ActionState.h"


#define STANDBY_SIGNAL "READY" 


void StandByState::init() {
    Serial.println(STANDBY_SIGNAL);
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
