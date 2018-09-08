#include "SerialRequestHandler.h"

#include "ExStream.h"
#include "Debug.h"


#define INVALID_REQUEST "INVALID"
#define STATE_REQUEST "GET_STATE"
#define REQUEST_WAIT 100


void SerialRequestHandler::handle()
{
    delay(REQUEST_WAIT);

    String request = ExSerial.readWord();
    DEBUG(ExSerial.println("Request: " + request));

    Motion motion = motionFromStr(request);
    if (motion != Motion::NULL_MOTION) {
        handleMotionRequest(motion);
    }
    else if (request.equalsIgnoreCase(STATE_REQUEST)) {
        handleStateRequest();
    }
    else {
        ExSerial.println(INVALID_REQUEST);
    }
}

void SerialRequestHandler::handleMotionRequest(Motion motion)
{
    bool validInput = true;

    float value = ExSerial.parseFloat();
    Metric metric = Metric::NULL_METRIC;
    MotionDirection direction = MotionDirection::NULL_DIR;

    String var1 = ExSerial.readWord();
    if (var1.length() > 0) {
        Metric parsedMetric = metricFromStr(var1);
        if (parsedMetric != Metric::NULL_METRIC) {
            metric = parsedMetric;
        } else {
            MotionDirection parsedDirection = motionDirFromStr(var1);
            if (parsedDirection != MotionDirection::NULL_DIR) {
                direction = parsedDirection;
            }
            else {
                DEBUG(ExSerial.println("Invalid metric/direction: " + var1));
                validInput = false;
            }
        }

        String var2 = ExSerial.readWord();
        if (var2.length() > 0) {
            if (direction != MotionDirection::NULL_DIR) {
                //direction is var1
                DEBUG(ExSerial.println("Invalid 3rd argument."));
                validInput = false;
            }
            else {

                DEBUG(ExSerial.println("Direction read: " + var2));
                direction = motionDirFromStr(var2);
                if (direction == MotionDirection::NULL_DIR) {
                    DEBUG(ExSerial.println("Invalid direction: " + var2));
                    validInput = false;
                }
            }
        }
    }
    if (!validInput) {
        ExSerial.clear();                       //clears remaining unread data from the invalid request
        ExSerial.println(INVALID_REQUEST);
        return;
    }

    bool success = controller.start(motion, value, metric, direction);
    if (!success) {
        ExSerial.println(INVALID_REQUEST);
    }
}

void SerialRequestHandler::handleStateRequest()
{
    ControllerState state = controller.getState();
    ExSerial.println(controllerStateToStr(state));
}
