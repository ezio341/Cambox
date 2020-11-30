package com.example.cambox.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class FormatUtil {
    public static DecimalFormat getCurrencyFormat(){
        DecimalFormat rupiah = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols format = new DecimalFormatSymbols();

        format.setCurrencySymbol("Rp ");
        format.setGroupingSeparator('.');
        format.setMonetaryDecimalSeparator(',');
        rupiah.setDecimalFormatSymbols(format);
        return rupiah;
    }
}
