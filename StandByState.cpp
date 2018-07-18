#include "StandByState.h"
#include "ActionState.h"
#include "ExStream.h" 
#include "ControllerConstants.h"

#define ACTION_WAIT 200

bool StandByState::serialEvent() {
    delay(ACTION_WAIT);

    String action = ExSerial.readWord();
    Motion motion = motionFromStr(action);
    //ExSerial.printf("Action: %s;Motion: %d\n", action.c_str(), motion);
    if (motion != Motion::NULL_MOTION) {
        return handleMotionRequest(motion);
    }
    else {
        return false;
    }
}

bool StandByState::handleMotionRequest(Motion motion)
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
                //ExSerial.printf("Invalid metric/direction '%s'\n", var1.c_str());
                return false;
            }
        }

        String var2 = ExSerial.readWord();
        if (var2.length() > 0) {
            if (direction != MotionDirection::NULL_DIR) {
                //direction is var1
                //ExSerial.printf("Invalid argument '%s'\n", var2.c_str());
                return false;
            }

            //ExSerial.printf("Direction read - '%s'\n", var2.c_str());
            direction = motionDirFromStr(var2);
            if (direction == MotionDirection::NULL_DIR) {
                //ExSerial.printf("Invalid dir '%s'\n", var2.c_str());
                return false;
            }
        }
    }

    //ExSerial.printf("ActionState set - Motion: %d; val: %s; metric: %d; dir: %d\n", motion, String(value).c_str(), metric, direction);
    return context.setState(new ActionState(context, motion, value, metric, direction));
}
