#pragma once

#include "Controller2WD.h"
#include "ControllerConstants.h"

class SerialRequestHandler {

    Controller2WD& controller;

public:

    SerialRequestHandler(Controller2WD& controller) : controller(controller) {}

    void handle();

private:

    bool handleMotionRequest(Motion motion);

};
