#include "SerialRequestHandler.h"

#include "ExStream.h"
#include "Debug.h"


#define REQUEST_WAIT 200

#define INVALID_REQUEST "INVALID"


void SerialRequestHandler::handle()
{
    delay(REQUEST_WAIT);

    String request = ExSerial.readWord();
    Motion motion = motionFromStr(request);
    DEBUG(ExSerial.printf("Request: %s; Motion: %d\n", request.c_str(), motionToStr(motion).c_str()));

    if (motion != Motion::NULL_MOTION) {
        bool success = handleMotionRequest(motion);
        if (!success)
            ExSerial.println(INVALID_REQUEST);
    }
    else {
        ExSerial.println(INVALID_REQUEST);
    }
}

bool SerialRequestHandler::handleMotionRequest(Motion motion)
{
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
                DEBUG(ExSerial.printf("Invalid metric/direction '%s'\n", var1.c_str()));
                return false;
            }
        }

        String var2 = ExSerial.readWord();
        if (var2.length() > 0) {
            if (direction != MotionDirection::NULL_DIR) {
                //direction is var1
                DEBUG(ExSerial.printf("Invalid argument '%s'\n", var2.c_str()));
                return false;
            }

            DEBUG(ExSerial.printf("Direction read - '%s'\n", var2.c_str()));
            direction = motionDirFromStr(var2);
            if (direction == MotionDirection::NULL_DIR) {
                DEBUG(ExSerial.printf("Invalid dir '%s'\n", var2.c_str()));
                return false;
            }
        }
    }

    return controller.start(motion, value, metric, direction);
}
