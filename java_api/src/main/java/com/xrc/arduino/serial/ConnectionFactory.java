package com.xrc.arduino.serial;

import com.xrc.arduino.serial.impl.SerialPortConnection;
import org.apache.commons.lang3.SystemUtils;

public class ConnectionFactory {

    private static final String UNIX_USB_PORT = "/dev/ttyUSB0";

    private static final String UNIX_ACM_PORT = "/dev/ttyACM0";

    private static final String MAC_PORT = "/dev/tty.usbserial-A9007UX1";

    private static final String WINDOWS_PORT = "COM3";

    private static final ConnectionFactory instance = new ConnectionFactory();

    public static ConnectionFactory getInstance() {
        return instance;
    }

    private ConnectionFactory() {
    }

    public SerialConnection getConnection() {

        if (SystemUtils.IS_OS_WINDOWS) {
            return new SerialPortConnection(WINDOWS_PORT);
        } else if (SystemUtils.IS_OS_MAC) {
            return new SerialPortConnection(MAC_PORT);
        } else {
            // the next line is for Raspberry Pi
            // was suggested here was suggested http://www.raspberrypi.org/phpBB3/viewtopic.php?f=81&t=32186
            //System.setProperty("gnu.io.rxtx.SerialPorts", UNIX_ACM_PORT);
            return new SerialPortConnection(UNIX_USB_PORT, UNIX_ACM_PORT);
        }
    }
}
