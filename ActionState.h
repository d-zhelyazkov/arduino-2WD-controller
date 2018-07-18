#pragma once

#include "State.h"
#include "Observer.h"


class ActionState :
    public State,
    private Observer {

    Motion motion;
    float value;
    Metric metric;
    MotionDirection direction;

public:
    ActionState(StateContext& context,
        Motion motion, float value, Metric metric, MotionDirection direction) :

        State(context),
        motion(motion), value(value), metric(metric), direction(direction){}


    virtual bool init();

    void update(Observable& observable);

    String getName();
};
