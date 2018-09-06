#pragma once

#include "ExStream.h"

#include "Controller2WD.h"


class ControllerStateNotifier : private Observer {

public:

    ControllerStateNotifier(Controller2WD& controller2WD) {
        controller2WD.subscribe(*this);
    }

    void update(Observable& observable) {
        Controller2WD& controller = (Controller2WD&)observable;
        ControllerState state = controller.getState();
        ExSerial.println(controllerStateToStr(state));
    }

};