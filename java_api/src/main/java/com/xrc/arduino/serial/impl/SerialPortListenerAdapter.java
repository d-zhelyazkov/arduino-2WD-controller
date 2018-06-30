package com.xrc.arduino.serial.impl;

import com.xrc.arduino.serial.SerialListener;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class SerialPortListenerAdapter implements SerialPortEventListener {

    private final SerialListener listener;

    SerialPortListenerAdapter(SerialListener listener) {
        this.listener = listener;
    }

    @Override
    public void serialEvent(SerialPortEvent ev) {
        listener.onSerialInput();
    }
}
