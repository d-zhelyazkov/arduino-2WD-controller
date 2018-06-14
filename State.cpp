#include "State.h"
#include "StandByState.h"
#include "Common.h"


#define DELAY_UNSUCCESSFUL 1000


void StateContext::serialEvent() {
    if (!state) {
        Serial.println("State context not set!");
        return;
    }

    bool success = (*state).serialEvent();
    if (!success) {
        Serial.println("SERIAL_DATA_INVALID");

        delay(DELAY_UNSUCCESSFUL);
    }
}

void StateContext::reset() {
    setState(new StandByState(*this));
}

void StateContext::setState(State* state) {
    StateContext& dis = *this;

    deleteObject(dis.state);
    dis.state = state;
    (*state).init();
}