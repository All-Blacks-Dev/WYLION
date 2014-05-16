package com.allblacks.weather.dto.weatherunderground;

import com.allblacks.utils.json.JSONSetter;
import com.allblacks.utils.json.impl.JSONType;

/**
 * Created by Marc on 27/04/2014.
 */
public class WeatherUndergroundForecastDayDTO {

    private WeatherUndergroundForecastDateDTO forecastDate;
    private Integer period;
    private WeatherUndergroundHighDTO high;
    private WeatherUndergroundLowDTO low;
    private String conditions;
    private WeatherUndergroundRainAllDayDTO rainAllDay;
    private WeatherUndergroundRainDayDTO rainDay;
    private WeatherUndergroundRainNightDTO rainNight;
    private WeatherUndergroundSnowAllDayDTO snowAllDay;
    private WeatherUndergroundSnowDayDTO snowDay;
    private WeatherUndergroundSnowNightDTO snowNight;
    private WeatherUndergroundMaxWindDTO maxWind;
    private WeatherUndergroundAveWindDTO aveWind;
    private Integer maxHumid;
    private Integer aveHumid;
    private Integer lowHumid;

    public WeatherUndergroundForecastDateDTO getForecastDate() {
        return forecastDate;
    }

    @JSONSetter(name = "date", type = JSONType.JSON_OBJECT)
    public void setForecastDate(WeatherUndergroundForecastDateDTO forecastDate) {
        this.forecastDate = forecastDate;
    }

    public Integer getPeriod() {
        return period;
    }

    @JSONSetter(name = "period")
    public void setPeriod(Integer period) {
        this.period = period;
    }

    public WeatherUndergroundHighDTO getHigh() {
        return high;
    }

    @JSONSetter(name = "high", type = JSONType.JSON_OBJECT)
    public void setHigh(WeatherUndergroundHighDTO high) {
        this.high = high;
    }

    public WeatherUndergroundLowDTO getLow() {
        return low;
    }

    @JSONSetter(name = "low", type = JSONType.JSON_OBJECT)
    public void setLow(WeatherUndergroundLowDTO low) {
        this.low = low;
    }

    public String getConditions() {
        return conditions;
    }

    @JSONSetter(name = "conditions")
    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public WeatherUndergroundRainAllDayDTO getRainAllDay() {
        return rainAllDay;
    }

    @JSONSetter(name = "qpf_allday", type = JSONType.JSON_OBJECT)
    public void setRainAllDay(WeatherUndergroundRainAllDayDTO rainAllDay) {
        this.rainAllDay = rainAllDay;
    }

    public WeatherUndergroundRainDayDTO getRainDay() {
        return rainDay;
    }

    @JSONSetter(name = "qpf_day", type = JSONType.JSON_OBJECT)
    public void setRainDay(WeatherUndergroundRainDayDTO rainDay) {
        this.rainDay = rainDay;
    }

    public WeatherUndergroundRainNightDTO getRainNight() {
        return rainNight;
    }

    @JSONSetter(name = "qpf_night", type = JSONType.JSON_OBJECT)
    public void setRainNight(WeatherUndergroundRainNightDTO rainNight) {
        this.rainNight = rainNight;
    }

    public WeatherUndergroundSnowAllDayDTO getSnowAllDay() {
        return snowAllDay;
    }

    @JSONSetter(name = "snow_allday", type = JSONType.JSON_OBJECT)
    public void setSnowAllDay(WeatherUndergroundSnowAllDayDTO snowAllDay) {
        this.snowAllDay = snowAllDay;
    }

    public WeatherUndergroundSnowDayDTO getSnowDay() {
        return snowDay;
    }

    @JSONSetter(name = "snow_day", type = JSONType.JSON_OBJECT)
    public void setSnowDay(WeatherUndergroundSnowDayDTO snowDay) {
        this.snowDay = snowDay;
    }

    public WeatherUndergroundSnowNightDTO getSnowNight() {
        return snowNight;
    }

    @JSONSetter(name = "snow_night", type = JSONType.JSON_OBJECT)
    public void setSnowNight(WeatherUndergroundSnowNightDTO snowNight) {
        this.snowNight = snowNight;
    }

    public WeatherUndergroundMaxWindDTO getMaxWind() {
        return maxWind;
    }

    @JSONSetter(name = "maxwind", type = JSONType.JSON_OBJECT)
    public void setMaxWind(WeatherUndergroundMaxWindDTO maxWind) {
        this.maxWind = maxWind;
    }

    public WeatherUndergroundAveWindDTO getAveWind() {
        return aveWind;
    }

    @JSONSetter(name = "avewind", type = JSONType.JSON_OBJECT)
    public void setAveWind(WeatherUndergroundAveWindDTO aveWind) {
        this.aveWind = aveWind;
    }

    public Integer getMaxHumid() {
        return maxHumid;
    }

    @JSONSetter(name = "maxhumidity")
    public void setMaxHumid(Integer maxHumid) {
        this.maxHumid = maxHumid;
    }

    public Integer getAveHumid() {
        return aveHumid;
    }

    @JSONSetter(name = "avehumidity")
    public void setAveHumid(Integer aveHumid) {
        this.aveHumid = aveHumid;
    }

    public Integer getLowHumid() {
        return lowHumid;
    }

    @JSONSetter(name = "minhumidity")
    public void setLowHumid(Integer minHumid) {
        this.lowHumid = minHumid;
    }
}
