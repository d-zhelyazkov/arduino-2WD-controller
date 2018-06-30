package com.xrc.arduino.serial;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.Writer;
import java.util.TooManyListenersException;

public interface SerialConnection extends Closeable {

    void initialize() throws Exception;

    BufferedReader getInputReader();

    Writer getOutputWriter();

    void subscribe(SerialListener listener) throws TooManyListenersException;
}
