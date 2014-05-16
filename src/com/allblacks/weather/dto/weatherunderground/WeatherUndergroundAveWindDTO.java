package com.allblacks.weather.dto.weatherunderground;

import com.allblacks.utils.json.JSONSetter;

/**
 * Created by marc on 27/04/14.
 */
public class WeatherUndergroundAveWindDTO {

    private Integer milesPerHours;
    private Integer kilometersPerHours;
    private String direction;
    private Integer degrees;

    public Integer getMilesPerHours() {
        return milesPerHours;
    }

    @JSONSetter(name = "mph")
    public void setMilesPerHours(Integer milesPerHours) {
        this.milesPerHours = milesPerHours;
    }

    public Integer getKilometersPerHours() {
        return kilometersPerHours;
    }

    @JSONSetter(name = "kph")
    public void setKilometersPerHours(Integer kilometersPerHours) {
        this.kilometersPerHours = kilometersPerHours;
    }

    public String getDirection() {
        return direction;
    }

    @JSONSetter(name = "dir")
    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Integer getDegrees() {
        return degrees;
    }

    @JSONSetter(name = "degrees")
    public void setDegrees(Integer degrees) {
        this.degrees = degrees;
    }
}
