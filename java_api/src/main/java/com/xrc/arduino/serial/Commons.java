package com.xrc.arduino.serial;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Commons {
    public static final DecimalFormat FLOAT_PARSE_FORMAT ;
    static {
        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(Locale.getDefault());
        formatSymbols.setDecimalSeparator('.');
        FLOAT_PARSE_FORMAT = new DecimalFormat("####.##", formatSymbols);
    }
}
