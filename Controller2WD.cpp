#include <math.h>
#include "Controller2WD.h"
#include "ExStream.h"

bool Controller2WD::move(float value, Metric metric, MotionDirection direction)
{
    if (isMoving()) {
        return false;
    }

    switch (metric) {
    case UNITS:
    case WHEEL_ROTATIONS:
        break;
    case NULL_METRIC:
        metric = UNITS;
        break;
    default:
        return false;
    }

    float wheelRotations = resolveWheelRotations(value, metric);

    if (direction == MotionDirection::NULL_DIR) {
        direction = MotionDirection::FORWARD;
    }
    direction = resolveMotionDirection(direction, wheelRotations);

    uint16_t encoderTicks = resolveEncoderTicks(wheelRotations);
    if (encoderTicks == 0) {
        return false;
    }

    switch (direction) {
    case FORWARD:
        leftMotor.move(ROT_ANTI_CLOCK, encoderTicks);
        rightMotor.move(ROT_CLOCK, encoderTicks);
        state = ControllerState::MOOVING_FORWARD;
        break;
    case BACKWARD:
        leftMotor.move(ROT_CLOCK, encoderTicks);
        rightMotor.move(ROT_ANTI_CLOCK, encoderTicks);
        state = ControllerState::MOOVING_BACKWARD;
        break;
    default:
        return false;
    }

    return true;
}

bool Controller2WD::turn(float value, Metric metric, MotionDirection direction)
{
    if (isMoving()) {
        return false;
    }

    switch (metric) {
    case WHEEL_ROTATIONS:
    case UNITS:
    case DEGREES:
        break;
    case NULL_METRIC:
        metric = DEGREES;
        break;
    default:
        return false;
    }

    float wheelRotations = resolveWheelRotations(value, metric);

    if (direction == MotionDirection::NULL_DIR) {
        direction = MotionDirection::LEFT;
    }
    direction = resolveMotionDirection(direction, wheelRotations);

    uint16_t encoderTicks = resolveEncoderTicks(wheelRotations);
    if (encoderTicks == 0) {
        return false;
    }

    //Serial.print("Will turn rotations "); Serial.println(wheelRotations);
    //Serial.print("Ticks: "); Serial.println(encoderTicks);

    switch (direction) {
    case LEFT:
        leftMotor.move(ROT_CLOCK, encoderTicks);
        rightMotor.move(ROT_CLOCK, encoderTicks);
        state = ControllerState::TURNING_LEFT;
        break;
    case RIGHT:
        leftMotor.move(ROT_ANTI_CLOCK, encoderTicks);
        rightMotor.move(ROT_ANTI_CLOCK, encoderTicks);
        state = ControllerState::TURNING_RIGHT;
        break;
    default:
        return false;
    }

    return true;
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

    state = ControllerState::STILL;
    notifyObservers();
}

float Controller2WD::resolveWheelRotations(float value, Metric metric)
{
    switch (metric) {
    case WHEEL_ROTATIONS:
        return value;
    case DEGREES: {
        float degrees = normalizeDegrees(value);
        float robotRotations = degrees / 360;
        return robotRotations * steerRatio;
    }
    case UNITS:
        return value / wheelPerimeter;
    default:
        return 0.0f;
    }
}

MotionDirection Controller2WD::resolveMotionDirection(MotionDirection direction, float wheelRotations)
{
    return (wheelRotations >= 0) ? direction : switchMotionDirection(direction);
}

uint16_t Controller2WD::resolveEncoderTicks(float wheelRotations)
{
    if (wheelRotations < 0) {
        wheelRotations = -wheelRotations;
    }

    return (uint16_t)(wheelRotations * encoderResolution);
}

float Controller2WD::normalizeDegrees(float degrees)
{
    degrees = fmod(degrees, 360);                   //[-360:360]
    if (degrees < -180) {
        degrees += 360;
    }
    else if (degrees > 180) {
        degrees -= 360;
    }                                               //[-180;180]
    return degrees;
}
