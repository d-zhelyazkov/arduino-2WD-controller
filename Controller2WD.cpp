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
        notifyObservers();
        break;
    case BACKWARD:
        DEBUG(ExSerial.printf("Will move backward for %d ticks.\n", encoderTicks);)
        leftMotor.move(ROT_CLOCK, encoderTicks);
        rightMotor.move(ROT_ANTI_CLOCK, encoderTicks);
        notifyObservers();
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
        notifyObservers();
        break;
    case RIGHT:
        DEBUG(ExSerial.printf("Will turn right for %d ticks.\n", encoderTicks));
        leftMotor.move(ROT_ANTI_CLOCK, encoderTicks);
        rightMotor.move(ROT_ANTI_CLOCK, encoderTicks);
        notifyObservers();
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
    return (getState() != ControllerState::STILL);
}

ControllerState Controller2WD::getState()
{
    MotorState leftState = leftMotor.getState();
    MotorState rightState = rightMotor.getState();

    switch (leftState)
    {
    case MotorState::STOPPED:
        switch (rightState)
        {
        case MotorState::STOPPED:
            return ControllerState::STILL;
        case MotorState::ROT_CLOCK:
            return ControllerState::TURNING_LEFT;
        case MotorState::ROT_ANTI_CLOCK:
            return ControllerState::TURNING_RIGHT;
        }
    case MotorState::ROT_CLOCK:
        switch (rightState)
        {
        case MotorState::STOPPED:
        case MotorState::ROT_CLOCK:
            return ControllerState::TURNING_LEFT;
        case MotorState::ROT_ANTI_CLOCK:
            return ControllerState::MOVING_BACKWARD;
        }

    case MotorState::ROT_ANTI_CLOCK:
        switch (rightState)
        {
        case MotorState::STOPPED:
        case MotorState::ROT_ANTI_CLOCK:
            return ControllerState::TURNING_RIGHT;
        case MotorState::ROT_CLOCK:
            return ControllerState::MOVING_FORWARD;
        }
    }

    DEBUG(
    ExSerial.println("Failed to determine the controller state.");
    ExSerial.printf("Left state: %d; right state: %d\n", leftState, rightState);
    );
    return ControllerState();
}

void Controller2WD::update(Observable& updatedMotor)
{
    if (isMoving())
        return;

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
        float wheelRotations = robotRotations * steerRatio;
        DEBUG(
        String degreesStr(degrees);
        ExSerial.printf("Degrees %s normalized to %s\n", String(value).c_str(), degreesStr.c_str());
        ExSerial.printf("Degrees %s resolved to %s rotations\n", degreesStr.c_str(), String(wheelRotations).c_str());
        );
        return wheelRotations;
    }
    case UNITS: {
        float wheelRotations = value / wheelPerimeter;
        DEBUG(ExSerial.printf("Units %s resolved to %s rotations\n", String(value).c_str(), String(wheelRotations).c_str()));
        return wheelRotations;
    }
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
