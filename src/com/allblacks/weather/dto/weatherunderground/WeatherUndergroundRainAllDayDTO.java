package com.allblacks.weather.dto.weatherunderground;

import com.allblacks.utils.json.JSONSetter;

/**
 * Created by marc on 27/04/14.
 */
public class WeatherUndergroundRainAllDayDTO {

    private Double inches;
    private Double milimeters;

    public Double getInches() {
        return inches;
    }

    @JSONSetter(name = "in")
    public void setInches(Double inches) {
        this.inches = inches;
    }

    public Double getMilimeters() {
        return milimeters;
    }

    @JSONSetter(name = "mm")
    public void setMilimeters(Double milimeters) {
        this.milimeters = milimeters;
    }
}
