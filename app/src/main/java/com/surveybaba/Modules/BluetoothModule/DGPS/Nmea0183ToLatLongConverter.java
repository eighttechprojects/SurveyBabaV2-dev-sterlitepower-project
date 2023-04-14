package com.surveybaba.Modules.BluetoothModule.DGPS;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Nmea0183ToLatLongConverter {

    double lat;
    String latPos;
    double lon;
    String lonPos;

//------------------------------------------------------- Constructor ---------------------------------------------------------------------------------------------------------------------------

    public Nmea0183ToLatLongConverter(double lat, String latPos, double lon, String lonPos) {
        this.lat = lat;
        this.latPos = latPos;
        this.lon = lon;
        this.lonPos = lonPos;
    }

//------------------------------------------------------- Latitude ---------------------------------------------------------------------------------------------------------------------------

    public double getDecimalLatitude() {
        return toDecimal(lat);
    }

    public double getSignedDecimalLatitude() {
        double l = getDecimalLatitude();
        if (l > 0 && latPos.equalsIgnoreCase("W")|| latPos.equalsIgnoreCase("S")) {
            return l * -1;
        } else {
            return l;
        }
    }

//------------------------------------------------------- Longitude ---------------------------------------------------------------------------------------------------------------------------

    public double getDecimalLongitude() {
        return toDecimal(lon);
    }

    public double getSignedDecimalLongitude() {
        double l = getDecimalLongitude();
        if (l > 0 && lonPos.equalsIgnoreCase("W") || lonPos.equalsIgnoreCase("S")) {
            return l * -1;
        } else {
            return l;
        }
    }

//------------------------------------------------------- Degree Minutes Sec ---------------------------------------------------------------------------------------------------------------------------

    private BigDecimal getDegrees(double d) {
        BigDecimal bd = new BigDecimal(d);
        bd = bd.movePointLeft(2);

        return new BigDecimal(bd.intValue());
    }

    private BigDecimal getMinutes(double d) {
        BigDecimal bd = new BigDecimal(d);
        bd = bd.movePointLeft(2);

        BigDecimal minutesBd = bd.subtract(new BigDecimal(bd.intValue()));
        minutesBd = minutesBd.movePointRight(2);

        return BigDecimal.valueOf((minutesBd.doubleValue() * 100) / 60).movePointLeft(2);
    }

//------------------------------------------------------- toDecimal ---------------------------------------------------------------------------------------------------------------------------

    private double toDecimal(double d) {

        BigDecimal degrees = getDegrees(d);

        BigDecimal minutesAndSeconds = getMinutes(d);

        BigDecimal decimal = degrees.add(minutesAndSeconds).setScale(4, RoundingMode.HALF_EVEN);

        return decimal.doubleValue();
    }


}
