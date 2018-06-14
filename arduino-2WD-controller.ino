/*
 Name:		Arduino2WDController3.ino
 Created:	6/9/2018 3:59:03 PM
 Author:	dzhelyazkov
*/

#include "MotorWithEncoder.h"
#include "ArduinoMotorShield.h"

#include "Controller2WD.h"
#include "State.h"


#define ENCODER_RESOLUTION 36
#define MOTOR_POWER 255


MotorWithEncoder leftMotor(ArduinoMotorShield::MOTOR_A, A3);
MotorWithEncoder rightMotor(ArduinoMotorShield::MOTOR_B, A2);
Controller2WD controller2WD(leftMotor, rightMotor, ENCODER_RESOLUTION);
StateContext stateContext(controller2WD);

// the setup function runs once when you press reset or power the board
void setup() {

    leftMotor.setPower(MOTOR_POWER);
    rightMotor.setPower(MOTOR_POWER);

    Serial.begin(9600);
    Serial.println("PROGRAM_BEGIN");
    stateContext.reset();

    /*controller2WD.move(FORWARD, 1);
    controller2WD.move(BACKWARD, 1);
    controller2WD.turn(LEFT, 1);
    controller2WD.turn(RIGHT, 1);*/
    
}

// the loop function runs over and over again until power down or reset
void loop() {

}

void serialEvent() {
    //Serial.println("serial event");

    stateContext.serialEvent();
}