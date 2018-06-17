#include "State.h"
#include "StandByState.h"
#include "Common.h"
#include "ExStream.h"


#define INVALID_SERIAL_DATA "INVALID"

#define CLEAR_WAIT 100


void StateContext::serialEvent() {
    if (!state) {
        Serial.println("State context not set!");
        return;
    }

    bool success = (*state).serialEvent();
    if (!success) {
        //clear, because the data (probably all of it) is not relevant to the current state
        //delay, because serial is slow and may not delete all data
        delay(CLEAR_WAIT);
        ExStream::serial().clear();
        Serial.println(INVALID_SERIAL_DATA);
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