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

class ActionPreState : public State {

    String command;

public:
    static bool canHandle(String& command);

    ActionPreState(StateContext& context, String& command) :
        State(context), command(command) {}

    void init();

    bool serialEvent();
};