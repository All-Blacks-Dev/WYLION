package com.allblacks.weather.dto.weatherunderground;

import com.allblacks.utils.json.JSONSetter;

/**
 * Created by Marc on 27/04/2014.
 */
public class WeatherUndergroundForecastDateDTO {

    private Integer day;
    private String monthName;
    private String weekday;

    public Integer getDay() {
        return day;
    }

    @JSONSetter(name = "day")
    public void setDay(Integer day) {
        this.day = day;
    }

    public String getMonthName() {
        return monthName;
    }

    @JSONSetter(name = "monthname")
    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public String getWeekday() {
        return weekday;
    }

    @JSONSetter(name = "weekday")
    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }
}
