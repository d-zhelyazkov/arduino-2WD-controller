/*
 Name:		Arduino2WDController3.ino
 Created:	6/9/2018 3:59:03 PM
 Author:	dzhelyazkov
*/


#include "MotorWithEncoder.h"
#include "ArduinoMotorShield.h"
#include "Observer.h"

#include "Controller2WD.h"

#define ENCODER_RESOLUTION 36
#define MOTOR_POWER 255

class ControllerObserver : public Observer {
public:
    void update(Observable& controller) {
        Serial.println("READY");
    }
};

MotorWithEncoder leftMotor(ArduinoMotorShield::MOTOR_A, A3);
MotorWithEncoder rightMotor(ArduinoMotorShield::MOTOR_B, A2);
Controller2WD controller2WD(leftMotor, rightMotor, ENCODER_RESOLUTION);
ControllerObserver controllerObserver;

// the setup function runs once when you press reset or power the board
void setup() {
    Serial.begin(9600);
    Serial.println("READY");

    leftMotor.setPower(MOTOR_POWER);
    rightMotor.setPower(MOTOR_POWER);

    controller2WD.registerObserver(controllerObserver);

    /*controller2WD.move(FORWARD, 1);
    controller2WD.move(BACKWARD, 1);
    controller2WD.turn(LEFT, 1);
    controller2WD.turn(RIGHT, 1);*/
    
}

// the loop function runs over and over again until power down or reset
void loop() {

}

void serialEvent() {
    Serial.println("serial event");

    String command = Serial.readString();
    while (!Serial.available())
        delay(100);
    float wheelRotations = Serial.parseFloat();
    Serial.println("DATA_RECEIVED");

    if (command.equalsIgnoreCase("MOVE_FORWARD")) {
        bool success = controller2WD.move(FORWARD, wheelRotations);
        //bool success = (wheelRotations > 0);
        Serial.println((success) ? "SUCCESS" : "FAIL");
        
    }
    else
    {
        Serial.println("DATA_NOT_OK");
    }
}