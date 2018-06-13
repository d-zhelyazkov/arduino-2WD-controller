#pragma once

#include "MotorWithEncoder.h"
#include "ArduinoMotorShield.h"
#include "Observer.h"

enum MoveDirection{FORWARD, BACKWARD};
enum TurnDirection{LEFT,RIGHT};

class Controller2WD : 
    private Observer,
    public Observable 
{

    MotorWithEncoder& leftMotor;
    MotorWithEncoder& rightMotor;
    uint16_t encoderResolution;

public:
    
    //encoderResolution - encoder ticks per one full wheel rotation
    Controller2WD(MotorWithEncoder& leftMotor, MotorWithEncoder& rightMotor, uint16_t encoderResolution)
        : leftMotor(leftMotor), rightMotor(rightMotor), encoderResolution(encoderResolution) {
    
        leftMotor.registerObserver(*this);
        rightMotor.registerObserver(*this);
    }

    //starts moving in FORWARD/BACKWARD direction
    //will asynchronously stop after the specified wheel rotations; observers are notified when stopped
    //wheelRotations - how many wheel rotations to do; decimal value; invalid if zero-negative value provided
    //returns true if successfully started moving
    bool move(MoveDirection direction, float wheelRotations);

    //starts turning in LEFT/RIGHT direction
    //will asynchronously stop after the specified wheel rotations; observers are notified when stopped
    //wheelRotations - how many wheel rotations to do; decimal value; invalid if zero-negative value provided
    //returns true if successfully started turning
    bool turn(TurnDirection direction, float wheelRotations);

    bool isMoving();

private:
    uint16_t wheelRotationsToEncoderTicks(float wheelRotations);

    //called when the motors change state; will notify observers when the two motors stop
    void update(Observable& updatedMotor);
};

