#pragma once

#include "State.h"


class StandByState : public State {

public:
    StandByState(StateContext& context) :
        State(context) {}

    void init();

    bool serialEvent();

};