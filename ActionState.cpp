#include "ActionState.h"
#include "StandByState.h"
#include "Controller2WD.h"
#include "ExStream.h"

bool ActionState::init() {

    Controller2WD& controller = context.getController();
    controller.subscribe(*this);
    //Serial.println("Subscribed.");

    //ExSerial.printf("ActionState init - Motion: %d; val: %s; metric: %d; dir: %d\n", motion, String(value).c_str(), metric, direction);
    switch (motion)
    {
    case MOVE:
        return controller.move(value, metric, direction);
    case TURN:
        return controller.turn(value, metric, direction);
    default:
        return false;
    }
}

void ActionState::update(Observable& observable) {
    observable.unsubscribe(*this);

    context.setState(new StandByState(context));
}

String ActionState::getName()
{
    Controller2WD& controller = context.getController();
    ControllerState state = controller.getState();
    return controllerStateToStr(state);
}
