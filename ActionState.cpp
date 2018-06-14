#include "ActionState.h"
#include "StandByState.h"
#include "Controller2WD.h"


void ActionState::init() {

    Controller2WD& controller = context.getController();
    controller.subscribe(*this);
    //Serial.println("Subscribed.");

    bool success = doAction();
    if (!success) {
        controller.unsubscribe(*this);

        Serial.println("FAILED_START");

        context.setState(new StandByState(context));
        return;
    }

    Serial.println("SUCCESSFUL_START");
}

void MoveForwardState::init()
{
    Serial.println("MOOVING_FORWARD");

    ActionState::init();
}

bool MoveForwardState::doAction() {
    return context.getController()
        .move(FORWARD, wheelRotations);
}

void ActionState::update(Observable& observable) {
    observable.unsubscribe(*this);

    context.setState(new StandByState(context));
}

void ActionPreState::init()
{
    Serial.println("AWAITING_ROTATIONS");
}

bool ActionPreState::serialEvent()
{
    float wheelRotations = Serial.parseFloat();
    Serial.print("Wheel rotations parsed: "); Serial.println(wheelRotations);

    if (command.equals("MOVE_FORWARD")) {
        context.setState(
            new MoveForwardState(context, wheelRotations));
    }
    else {
        Serial.println("Unknown command received.");
        return false;
    }

    return true;
}
