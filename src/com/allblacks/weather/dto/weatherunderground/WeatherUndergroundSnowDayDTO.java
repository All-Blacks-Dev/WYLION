package com.allblacks.weather.dto.weatherunderground;

import com.allblacks.utils.json.JSONSetter;

/**
 * Created by marc on 27/04/14.
 */
public class WeatherUndergroundSnowDayDTO {

    private Double inches;
    private Double centimeters;

    public Double getInches() {
        return inches;
    }

    @JSONSetter(name = "in")
    public void setInches(Double inches) {
        this.inches = inches;
    }

    public Double getCentimeters() {
        return centimeters;
    }

    @JSONSetter(name = "cm")
    public void setCentimeters(Double centimeters) {
        this.centimeters = centimeters;
    }
}
