#include "Controller2WD.h"


bool Controller2WD::move(MoveDirection direction, float wheelRotations)
{
    if (isMoving())
        return false;

    uint16_t encoderTicks = wheelRotationsToEncoderTicks(wheelRotations);
    if (encoderTicks == 0)
        return false;

    switch (direction)
    {
    case FORWARD:
        leftMotor.move(ROT_ANTI_CLOCK, encoderTicks);
        rightMotor.move(ROT_CLOCK, encoderTicks);
        break;
    case BACKWARD:
        leftMotor.move(ROT_CLOCK, encoderTicks);
        rightMotor.move(ROT_ANTI_CLOCK, encoderTicks);
        break;
    }

    return true;
}

bool Controller2WD::turn(TurnDirection direction, float wheelRotations)
{
    if (isMoving())
        return false;

    uint16_t encoderTicks = wheelRotationsToEncoderTicks(wheelRotations);
    if (encoderTicks == 0)
        return false;

    //Serial.print("Will turn rotations "); Serial.println(wheelRotations);
    //Serial.print("Ticks: "); Serial.println(encoderTicks);

    switch (direction)
    {
    case LEFT:
        leftMotor.move(ROT_CLOCK, encoderTicks);
        rightMotor.move(ROT_CLOCK, encoderTicks);
        break;
    case RIGHT:
        leftMotor.move(ROT_ANTI_CLOCK, encoderTicks);
        rightMotor.move(ROT_ANTI_CLOCK, encoderTicks);
        break;
    }

    return true;
}

uint16_t Controller2WD::wheelRotationsToEncoderTicks(float wheelRotations)
{
    if (wheelRotations < 0) {
        return 0;
    }

    return (uint16_t)(wheelRotations * encoderResolution);
}

bool Controller2WD::isMoving()
{
    return (
        (leftMotor.getState() != MotorState::STOPPED)
        || (rightMotor.getState() != MotorState::STOPPED));
}

void Controller2WD::update(Observable& updatedMotor)
{
    if (isMoving())
        return;

    notifyObservers();
}
