#pragma once
#include <WString.h>

enum Motion { NULL_MOTION, MOVE, TURN};
Motion motionFromStr(String& motion);

enum MotionDirection { NULL_DIR, FORWARD, BACKWARD , LEFT, RIGHT};
MotionDirection motionDirFromStr(String& direction);
MotionDirection switchMotionDirection(MotionDirection direction);

enum Metric { NULL_METRIC, WHEEL_ROTATIONS, UNITS, DEGREES};
Metric metricFromStr(String& metric);

enum ControllerState { NULL_STATE, STILL, MOOVING_FORWARD, MOOVING_BACKWARD, TURNING_LEFT, TURNING_RIGHT};
String controllerStateToStr(ControllerState state);