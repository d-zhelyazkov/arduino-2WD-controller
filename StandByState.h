#pragma once

#include "State.h"


class StandByState : public State {

public:
    StandByState(StateContext& context) :
        State(context) {}

    bool serialEvent();

    String getName() {
        return "STANDING_BY";
    }

private:

    bool handleMotionRequest(Motion motion);

};