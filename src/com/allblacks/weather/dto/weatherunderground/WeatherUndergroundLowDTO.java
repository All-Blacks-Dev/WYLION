package com.allblacks.weather.dto.weatherunderground;

import com.allblacks.utils.json.JSONSetter;

/**
 * Created by Marc on 27/04/2014.
 */
public class WeatherUndergroundLowDTO {

    private String fahrenheit;
    private String celsius;

    public String getFahrenheit() {
        return fahrenheit;
    }

    @JSONSetter(name = "fahrenheit")
    public void setFahrenheit(String fahrenheit) {
        this.fahrenheit = fahrenheit;
    }

    public String getCelsius() {
        return celsius;
    }

    @JSONSetter(name = "celsius")
    public void setCelsius(String celsius) {
        this.celsius = celsius;
    }
}
