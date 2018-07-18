#include "State.h"
#include "StandByState.h"
#include "Common.h"
#include "ExStream.h"


#define INVALID "INVALID"

#define CLEAR_WAIT 100


void StateContext::serialEvent() {
    if (!state) {
        ExSerial.println("State context not set!");
        return;
    }

    bool success = (*state).serialEvent();
    if (!success) {
        //clear, because the data (probably all of it) is not relevant to the current state
        //delay, because serial is slow and may not delete all data
        delay(CLEAR_WAIT);
        ExSerial.clear();
        ExSerial.println(INVALID);
    }

}

void StateContext::reset() {
    setState(new StandByState(*this));
}

bool StateContext::setState(State* statePtr) {
    StateContext& dis = *this;

    State& state = *statePtr;
    bool result = state.init();
    if (result == false) {
        return false;
    }

    deleteObject(dis.state);
    dis.state = statePtr;
    ExSerial.println(state.getName());

    return true;
}