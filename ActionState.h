#pragma once

#include "State.h"

#include "Observer.h"


class ActionState :
    public State,
    private Observer {

protected:
    float wheelRotations;

public:
    ActionState(StateContext& context, float wheelRotations) :
        State(context),
        wheelRotations(wheelRotations) {}

    void init();

    void update(Observable& observable);

protected:
    virtual bool doAction() = 0;
};

class MoveForwardState : public ActionState {

public:
    MoveForwardState(StateContext& context, float wheelRotations) :
        ActionState(context, wheelRotations) {}

    void init();

protected:
    bool doAction();

};

class ActionPreState : public State {

    String command;

public:
    ActionPreState(StateContext& context, String& command) :
        State(context), command(command) {}

    void init();

    bool serialEvent();
};