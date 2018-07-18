/*
 Name:		Arduino2WDController3.ino
 Created:	6/9/2018 3:59:03 PM
 Author:	dzhelyazkov
*/

#include "MotorWithEncoder.h"
#include "ArduinoMotorShield.h"

#include "Controller2WD.h"
#include "ExStream.h"
#include "State.h"


#define SERIAL_BOUD 9600
#define ENCODER_RESOLUTION 36
#define AXLE_TRACK 17
#define WHEEL_DIAMETER 5
#define MOTOR_POWER 255

#define PROGRAM_STARTED "PROGRAM_STARTED"

MotorWithEncoder leftMotor(ArduinoMotorShield::MOTOR_A, A3);
MotorWithEncoder rightMotor(ArduinoMotorShield::MOTOR_B, A2);
Controller2WD controller2WD(leftMotor, rightMotor, ENCODER_RESOLUTION, AXLE_TRACK, WHEEL_DIAMETER);
StateContext stateContext(controller2WD);

// the setup function runs once when you press reset or power the board
void setup() {
    Serial.begin(SERIAL_BOUD);

    leftMotor.setPower(MOTOR_POWER);
    rightMotor.setPower(MOTOR_POWER);

    /*ExSerial.println("Controller2WD initialization...");
    ExSerial.println("Wheel diameter: " + String(controller2WD.getWheelDiameter()));
    ExSerial.println("Wheel perimeter: " + String(controller2WD.getWheelPerimeter()));
    ExSerial.println("Axle track: " + String(controller2WD.getAxleTrack()));*/

    stateContext.reset();

    ExSerial.println(PROGRAM_STARTED);
}

// the loop function runs over and over again until power down or reset
void loop() {
}

void serialEvent() {
    //ExSerial.println("serial event");
    stateContext.serialEvent();
}