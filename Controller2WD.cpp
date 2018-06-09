#include "Controller2WD.h"

#define STILL_CHECK_INTERVAL 100

bool Controller2WD::move(MoveDirection direction, float wheelRotations)
{
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

    waitUntilStill();
    return true;
}

bool Controller2WD::turn(TurnDirection direction, float wheelRotations)
{
    Serial.print("Will turn rotations "); Serial.println(wheelRotations);
    uint16_t encoderTicks = wheelRotationsToEncoderTicks(wheelRotations);
    Serial.print("Ticks: "); Serial.println(encoderTicks);
    if (encoderTicks == 0)
        return false;

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

    waitUntilStill();
    return true;
}

uint16_t Controller2WD::wheelRotationsToEncoderTicks(float wheelRotations)
{
    if (wheelRotations < 0) {
        return 0;
    }

    return (uint16_t)(wheelRotations * encoderResolution);
}

void Controller2WD::waitUntilStill()
{
    while (
        (leftMotor.getState() != MotorState::STOPPED)
        || (rightMotor.getState() != MotorState::STOPPED)) {

        delay(STILL_CHECK_INTERVAL);
    }
}
