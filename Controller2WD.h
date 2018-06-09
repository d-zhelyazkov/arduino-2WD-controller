#pragma once

#include <MotorWithEncoder.h>
#include <ArduinoMotorShield.h>

enum MoveDirection{FORWARD, BACKWARD};
enum TurnDirection{LEFT,RIGHT};

class Controller2WD
{
    MotorWithEncoder& leftMotor;
    MotorWithEncoder& rightMotor;
    uint16_t encoderResolution;

public:
    Controller2WD(MotorWithEncoder& leftMotor, MotorWithEncoder& rightMotor, uint16_t encoderResolution)
        : leftMotor(leftMotor), rightMotor(rightMotor), encoderResolution(encoderResolution) {}

    bool move(MoveDirection direction, float wheelRotations);
    bool turn(TurnDirection direction, float wheelRotations);

private:
    uint16_t wheelRotationsToEncoderTicks(float wheelRotations);
    void waitUntilStill();
};

