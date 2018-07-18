#include "ControllerConstants.h"

Motion motionFromStr(String & motion)
{
    if (motion.equalsIgnoreCase("MOVE")) {
        return Motion::MOVE;
    }
    else if (motion.equalsIgnoreCase("TURN")) {
        return Motion::TURN;
    }
    else {
        return Motion::NULL_MOTION;
    }
}

MotionDirection motionDirFromStr(String & direction)
{
    if (direction.equalsIgnoreCase("FORWARD")
        || direction.equalsIgnoreCase("FWRD")
    ) {
        return MotionDirection::FORWARD;
    }
    else if (direction.equalsIgnoreCase("BACKWARD")
        || direction.equalsIgnoreCase("BACK")
        || direction.equalsIgnoreCase("BCKWRD")
    ) {
        return MotionDirection::BACKWARD;
    } 
    else if (direction.equalsIgnoreCase("LEFT")) {
        return MotionDirection::LEFT;
    }
    else if (direction.equalsIgnoreCase("RIGHT")) {
        return MotionDirection::RIGHT;
    }
    else {
        return MotionDirection::NULL_DIR;
    }
}

MotionDirection switchMotionDirection(MotionDirection direction)
{
    switch (direction)
    {
    case FORWARD:
        return MotionDirection::BACKWARD;
    case BACKWARD:
        return MotionDirection::FORWARD;
    case LEFT:
        return MotionDirection::RIGHT;
    case RIGHT:
        return MotionDirection::LEFT;
    default:
        return direction;
    }
}

Metric metricFromStr(String & metric)
{
    if (metric.equalsIgnoreCase("UNITS")
        || metric.equalsIgnoreCase("UNIT")
    ) {
        return Metric::UNITS;
    }
    else if (metric.equalsIgnoreCase("WHEEL_ROTATIONS")
        || metric.equalsIgnoreCase("ROTATIONS")
        || metric.equalsIgnoreCase("ROT")
    ) {
        return Metric::WHEEL_ROTATIONS;
    } 
    else if (metric.equalsIgnoreCase("DEGREES")
        || metric.equalsIgnoreCase("DEG")
    ) {
        return Metric::DEGREES;
    }
    else {
        return Metric::NULL_METRIC;
    }
}

String controllerStateToStr(ControllerState state)
{
    switch (state)
    {
    case STILL:
        return "STOPPED";
    case MOOVING_FORWARD:
        return "MOOVING_FORWARD";
    case MOOVING_BACKWARD:
        return "MOOVING_BACKWARD";
    case TURNING_LEFT:
        return "TURNING_LEFT";
    case TURNING_RIGHT:
        return "TURNING_RIGHT";
    default:
        return "UNDEFINED";
    }

}
