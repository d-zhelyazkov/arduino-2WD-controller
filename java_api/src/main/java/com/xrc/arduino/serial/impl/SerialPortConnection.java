package com.xrc.arduino.serial.impl;

import com.xrc.arduino.serial.SerialConnection;
import com.xrc.arduino.serial.SerialListener;
import com.xrc.io.AutoFlushWriter;
import com.xrc.util.Arrays;
import com.xrc.util.Enumerations;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Optional;
import java.util.TooManyListenersException;

public class SerialPortConnection implements SerialConnection {

    /**
     * Milliseconds to block while waiting for port open
     */
    private static final int TIME_OUT = 5000;

    /**
     * Default bits per second for COM port.
     */
    private static final int DATA_RATE = 9600;

    private final Collection<String> portNames;

    private SerialPort serialPort;

    private BufferedReader inputReader;

    private Writer outputWriter;

    public SerialPortConnection(String... portNames) {
        this.portNames = Arrays.asSet(portNames);
    }

    @Override
    public void initialize() throws Exception {

        //noinspection unchecked
        Enumeration<CommPortIdentifier> portIdentifiers = CommPortIdentifier.getPortIdentifiers();
        Optional<CommPortIdentifier> portIdResult = Enumerations.stream(portIdentifiers)
                .filter(commPortIdentifier -> portNames.contains(commPortIdentifier.getName()))
                .findFirst();
        if (!portIdResult.isPresent()) {
            throw new Exception("Cannot find any of ports " + portNames);
        }
        CommPortIdentifier portId = portIdResult.get();

        // open serial port, and use class name for the appName.
        serialPort = (SerialPort) portId.open(this.getClass().getName(), TIME_OUT);

        // set port parameters
        serialPort.setSerialPortParams(
                DATA_RATE,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);

        inputReader = new BufferedReader(
                new InputStreamReader(
                        serialPort.getInputStream(), StandardCharsets.US_ASCII));
        outputWriter = new AutoFlushWriter(
                new OutputStreamWriter(
                        serialPort.getOutputStream(), StandardCharsets.US_ASCII));

        serialPort.notifyOnDataAvailable(true);
    }

    @Override
    public BufferedReader getInputReader() {
        return inputReader;
    }

    @Override
    public Writer getOutputWriter() {
        return outputWriter;
    }

    @Override
    public void subscribe(SerialListener listener) throws TooManyListenersException {
        // add event listeners
        serialPort.addEventListener(new SerialPortListenerAdapter(listener));
    }

    @Override
    public void close() {
        if (serialPort == null) {
            return;
        }

        serialPort.removeEventListener();
        serialPort.close();
    }
}
