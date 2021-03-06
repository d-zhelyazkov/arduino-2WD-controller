#pragma once
#include <WString.h>

enum Motion { NULL_MOTION, MOVE, TURN};
Motion motionFromStr(String& motion);
String motionToStr(Motion motion);

enum MotionDirection { NULL_DIR, FORWARD, BACKWARD , LEFT, RIGHT};
MotionDirection motionDirFromStr(String& direction);
String motionDirToStr(MotionDirection direction);
MotionDirection switchMotionDirection(MotionDirection direction);

enum Metric { NULL_METRIC, WHEEL_ROTATIONS, UNITS, DEGREES};
Metric metricFromStr(String& metric);
String metricToStr(Metric metric);

enum ControllerState { NULL_STATE, STILL, MOVING_FORWARD, MOVING_BACKWARD, TURNING_LEFT, TURNING_RIGHT};
String controllerStateToStr(ControllerState state);