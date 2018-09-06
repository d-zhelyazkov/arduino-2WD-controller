#pragma once

#include "MotorWithEncoder.h"
#include "ArduinoMotorShield.h"
#include "Observer.h"
#include "ControllerConstants.h"


class Controller2WD : 
    private Observer,
    public Observable 
{

    MotorWithEncoder& leftMotor;
    MotorWithEncoder& rightMotor;

    uint16_t encoderResolution;
    float axleTrack;
    float wheelDiameter;

    float steerRatio;
    float wheelPerimeter;

    ControllerState state = ControllerState::STILL;

public:
    
    //encoderResolution - encoder ticks per one full wheel rotation
    //axleTrack - the distance in units between the two wheels
    //wheelDiameter - the deiameter in units of one wheel
    Controller2WD(MotorWithEncoder& leftMotor, MotorWithEncoder& rightMotor,
        uint16_t encoderResolution, float axleTrack, float wheelDiameter)

        : leftMotor(leftMotor), rightMotor(rightMotor),
        encoderResolution(encoderResolution), axleTrack(axleTrack), wheelDiameter(wheelDiameter) {

        steerRatio = axleTrack / wheelDiameter;
        wheelPerimeter = wheelDiameter * PI;

        leftMotor.subscribe(*this);
        rightMotor.subscribe(*this);
    }

    //starts moving in straight direction
    //will asynchronously stop - observers will be notified for state change
    //value - how much to move measured in the given metric; if negative the direction is switched
    //metric - UNITS/WHEEL_ROTATIONS
    //direction - FORWARD/BACKWARD
    //returns true if successfully started moving
    bool move(float value, Metric metric = UNITS, MotionDirection direction = FORWARD);

    //starts turning
    //will asynchronously stop - observers will be notified for state change
    //value - how much to turn measured in the given metric; if negative the direction is switched
    //metric - UNITS/WHEEL_ROTATIONS/DEGREES
    //direction - LEFT/RIGHT
    //returns true if successfully started turning
    bool turn(float value, Metric metric = DEGREES, MotionDirection direction = LEFT);

    bool start(Motion motion, float value, Metric metric, MotionDirection direction);

    bool isMoving();

    ControllerState getState() { return state; }

    //the diameter in units of one wheel
    float getWheelDiameter() { return wheelDiameter; }

    //the perimeter in units of one wheel
    float getWheelPerimeter() { return wheelPerimeter; }

    //the distance in units between the two wheels
    float getAxleTrack() { return axleTrack; }

private:

    void setState(ControllerState state) {
        (*this).state = state;
        notifyObservers();
    }

    //called when the motors change state;
    void update(Observable& updatedMotor);

    float resolveWheelRotations(float value, Metric metric);

    MotionDirection resolveMotionDirection(MotionDirection direction, float wheelRotations);

    uint16_t resolveEncoderTicks(float wheelRotations);

    float normalizeDegrees(float degrees);
};

