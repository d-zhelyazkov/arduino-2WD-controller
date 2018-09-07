#include "Controller2WD.h"

#include <math.h>

#include "ExStream.h"
#include "Debug.h"


bool Controller2WD::move(float value, Metric metric, MotionDirection direction)
{
    if (isMoving()) {
        DEBUG(ExSerial.println("Can't move since the controller is already mooving."));
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
        DEBUG(ExSerial.println("Can't resolve move metric " + metricToStr(metric)));
        return false;
    }

    float wheelRotations = resolveWheelRotations(value, metric);

    if (direction == MotionDirection::NULL_DIR) {
        direction = MotionDirection::FORWARD;
    }
    direction = resolveMotionDirection(direction, wheelRotations);

    uint16_t encoderTicks = resolveEncoderTicks(wheelRotations);
    if (encoderTicks == 0) {
        DEBUG(ExSerial.println("Can't move because encoder ticks evaluated to 0."));
        return false;
    }

    switch (direction) {
    case FORWARD:
        DEBUG(ExSerial.printf("Will move forward for %d ticks.\n", encoderTicks);)
        leftMotor.move(ROT_ANTI_CLOCK, encoderTicks);
        rightMotor.move(ROT_CLOCK, encoderTicks);
        setState(ControllerState::MOOVING_FORWARD);
        break;
    case BACKWARD:
        DEBUG(ExSerial.printf("Will move backward for %d ticks.\n", encoderTicks);)
        leftMotor.move(ROT_CLOCK, encoderTicks);
        rightMotor.move(ROT_ANTI_CLOCK, encoderTicks);
        setState(ControllerState::MOOVING_BACKWARD);
        break;
    default:
        DEBUG(ExSerial.println("Can't resolve move direction " + motionDirToStr(direction)));
        return false;
    }

    return true;
}

bool Controller2WD::turn(float value, Metric metric, MotionDirection direction)
{
    if (isMoving()) {
        DEBUG(ExSerial.println("Can't turn since the controller is already mooving."));
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
        DEBUG(ExSerial.println("Can't resolve turn metric " + metricToStr(metric)));
        return false;
    }

    float wheelRotations = resolveWheelRotations(value, metric);

    if (direction == MotionDirection::NULL_DIR) {
        direction = MotionDirection::LEFT;
    }
    direction = resolveMotionDirection(direction, wheelRotations);

    uint16_t encoderTicks = resolveEncoderTicks(wheelRotations);
    if (encoderTicks == 0) {
        DEBUG(ExSerial.println("Can't turn because encoder ticks evaluated to 0."));
        return false;
    }

    switch (direction) {
    case LEFT:
        DEBUG(ExSerial.printf("Will turn left for %d ticks.\n", encoderTicks));
        leftMotor.move(ROT_CLOCK, encoderTicks);
        rightMotor.move(ROT_CLOCK, encoderTicks);
        setState(ControllerState::TURNING_LEFT);
        break;
    case RIGHT:
        DEBUG(ExSerial.printf("Will turn right for %d ticks.\n", encoderTicks));
        leftMotor.move(ROT_ANTI_CLOCK, encoderTicks);
        rightMotor.move(ROT_ANTI_CLOCK, encoderTicks);
        setState(ControllerState::TURNING_RIGHT);
        break;
    default:
        DEBUG(ExSerial.println("Can't resolve turn direction " + motionDirToStr(direction)));
        return false;
    }

    return true;
}

bool Controller2WD::start(Motion motion, float value, Metric metric, MotionDirection direction)
{
    switch (motion)
    {
    case MOVE:
        return move(value, metric, direction);
    case TURN:
        return turn(value, metric, direction);
    default:
        DEBUG(ExSerial.println("Can't resolve motion " + motionToStr(motion)));
        return false;
    }
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

    setState(ControllerState::STILL);
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
