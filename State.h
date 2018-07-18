#pragma once

#include "Controller2WD.h"


class StateContext;

class State {

protected:
    StateContext& context;

public:
    State(StateContext& context) :
        context(context) {}

    virtual bool serialEvent() { return false; }

    virtual bool init() { return true; }

    virtual String getName() = 0;
};

class StateContext {

    State* state = nullptr;

    Controller2WD& controller2WD;

public:
    StateContext(Controller2WD& controller2WD) : controller2WD(controller2WD) {}

    void serialEvent();

    void reset();

    bool setState(State* state);

    Controller2WD& getController() {
        return controller2WD;
    }
};
