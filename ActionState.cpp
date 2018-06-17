#include "ActionState.h"
#include "StandByState.h"
#include "Controller2WD.h"


#define MOVE_FORWARD_CMD "MOVE_FORWARD"
#define MOVE_BACKWARD_CMD "MOVE_BACKWARD"
#define TURN_LEFT_CMD "TURN_LEFT"
#define TURN_RIGHT_CMD "TURN_RIGHT"


class MoveForwardState : public ActionState {
public:
    MoveForwardState(StateContext& context, float wheelRotations) :
        ActionState(context, wheelRotations) {}
protected:
    bool doAction() {
        return context.getController().move(FORWARD, wheelRotations);
    }
};
class MoveBackwardState : public ActionState {
public:
    MoveBackwardState(StateContext& context, float wheelRotations) :
        ActionState(context, wheelRotations) {}
protected:
    bool doAction() {
        return context.getController().move(BACKWARD, wheelRotations);
    }
};
class TurnLeftState : public ActionState {
public:
    TurnLeftState(StateContext& context, float wheelRotations) :
        ActionState(context, wheelRotations) {}
protected:
    bool doAction() {
        return context.getController().turn(LEFT, wheelRotations);
    }
};
class TurnRightState : public ActionState {
public:
    TurnRightState(StateContext& context, float wheelRotations) :
        ActionState(context, wheelRotations) {}
protected:
    bool doAction() {
        return context.getController().turn(RIGHT, wheelRotations);
    }
};


void ActionState::init() {

    Controller2WD& controller = context.getController();
    controller.subscribe(*this);
    //Serial.println("Subscribed.");

    doAction();
    Serial.println("SUCCESSFUL_START");
}

void ActionState::update(Observable& observable) {
    observable.unsubscribe(*this);

    context.setState(new StandByState(context));
}

bool ActionPreState::canHandle(String & command)
{
    return (
        command.equals(MOVE_FORWARD_CMD)
        || command.equals(MOVE_BACKWARD_CMD)
        || command.equals(TURN_LEFT_CMD)
        || command.equals(TURN_RIGHT_CMD));
}

void ActionPreState::init()
{
    Serial.println("AWAITING_ROTATIONS");
}

bool ActionPreState::serialEvent()
{
    float wheelRotations = Serial.parseFloat();
    //Serial.print("Wheel rotations parsed: "); Serial.println(wheelRotations);
    if (wheelRotations <= 0.0f)
        return false;

    if (command.equals(MOVE_FORWARD_CMD)) {
        context.setState(
            new MoveForwardState(context, wheelRotations));
    } else if(command.equals(MOVE_BACKWARD_CMD)) {
        context.setState(
            new MoveBackwardState(context, wheelRotations));
    }
    else if (command.equals(TURN_LEFT_CMD)) {
        context.setState(
            new TurnLeftState(context, wheelRotations));
    }
    else if (command.equals(TURN_RIGHT_CMD)) {
        context.setState(
            new TurnRightState(context, wheelRotations));
    }
    else {
        Serial.println("Unknown command received.");
        return false;
    }

    return true;
}
