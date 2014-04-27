package com.allblacks.weather.dto.wunderground;

import com.allblacks.utils.json.JSONSetter;
import com.allblacks.utils.json.impl.JSONType;

/**
 * Created by Marc on 27/04/2014.
 */
public class WundergroundForecastDayDTO {

    private WundergroundForecastDateDTO forecastDate;
    private Integer period;
    private WundergroundHighDTO high;
    private WundergroundLowDTO low;
    private String conditions;
    private WundergroundRainAllDayDTO rainAllDay;
    private WundergroundRainDayDTO rainDay;
    private WundergroundRainNightDTO rainNight;
    private WundergroundSnowAllDayDTO snowAllDay;
    private WundergroundSnowDayDTO snowDay;
    private WundergroundSnowNightDTO snowNight;
    private WundergroundMaxWindDTO maxWind;
    private WundergroundAveWindDTO aveWind;
    private WundergroundMaxHumidDTO maxHumid;
    private WundergroundAveHumidDTO aveHumid;
    private WundergroundLowHumidDTO lowHumid;

    public WundergroundForecastDateDTO getForecastDate() {
        return forecastDate;
    }

    @JSONSetter(name = "date", type = JSONType.JSON_OBJECT)
    public void setForecastDate(WundergroundForecastDateDTO forecastDate) {
        this.forecastDate = forecastDate;
    }

    public Integer getPeriod() {
        return period;
    }

    @JSONSetter(name = "period")
    public void setPeriod(Integer period) {
        this.period = period;
    }

    public WundergroundHighDTO getHigh() {
        return high;
    }

    @JSONSetter(name = "high", type = JSONType.JSON_OBJECT)
    public void setHigh(WundergroundHighDTO high) {
        this.high = high;
    }

    public WundergroundLowDTO getLow() {
        return low;
    }

    @JSONSetter(name = "low", type = JSONType.JSON_OBJECT)
    public void setLow(WundergroundLowDTO low) {
        this.low = low;
    }

    public String getConditions() {
        return conditions;
    }

    @JSONSetter(name = "conditions")
    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public WundergroundRainAllDayDTO getRainAllDay() {
        return rainAllDay;
    }

    @JSONSetter(name = "qpf_allday", type = JSONType.JSON_OBJECT)
    public void setRainAllDay(WundergroundRainAllDayDTO rainAllDay) {
        this.rainAllDay = rainAllDay;
    }

    public WundergroundRainDayDTO getRainDay() {
        return rainDay;
    }

    @JSONSetter(name = "qpf_day", type = JSONType.JSON_OBJECT)
    public void setRainDay(WundergroundRainDayDTO rainDay) {
        this.rainDay = rainDay;
    }

    public WundergroundRainNightDTO getRainNight() {
        return rainNight;
    }

    @JSONSetter(name = "qpf_night", type = JSONType.JSON_OBJECT)
    public void setRainNight(WundergroundRainNightDTO rainNight) {
        this.rainNight = rainNight;
    }

    public WundergroundSnowAllDayDTO getSnowAllDay() {
        return snowAllDay;
    }

    @JSONSetter(name = "snow_allday", type = JSONType.JSON_OBJECT)
    public void setSnowAllDay(WundergroundSnowAllDayDTO snowAllDay) {
        this.snowAllDay = snowAllDay;
    }

    public WundergroundSnowDayDTO getSnowDay() {
        return snowDay;
    }

    @JSONSetter(name = "snow_day", type = JSONType.JSON_OBJECT)
    public void setSnowDay(WundergroundSnowDayDTO snowDay) {
        this.snowDay = snowDay;
    }

    public WundergroundSnowNightDTO getSnowNight() {
        return snowNight;
    }

    @JSONSetter(name = "snow_night", type = JSONType.JSON_OBJECT)
    public void setSnowNight(WundergroundSnowNightDTO snowNight) {
        this.snowNight = snowNight;
    }

    public WundergroundMaxWindDTO getMaxWind() {
        return maxWind;
    }

    @JSONSetter(name = "maxwind", type = JSONType.JSON_OBJECT)
    public void setMaxWind(WundergroundMaxWindDTO maxWind) {
        this.maxWind = maxWind;
    }

    public WundergroundAveWindDTO getAveWind() {
        return aveWind;
    }

    @JSONSetter(name = "avewind", type = JSONType.JSON_OBJECT)
    public void setAveWind(WundergroundAveWindDTO aveWind) {
        this.aveWind = aveWind;
    }

    public WundergroundMaxHumidDTO getMaxHumid() {
        return maxHumid;
    }

    @JSONSetter(name = "maxhumidity", type = JSONType.JSON_OBJECT)
    public void setMaxHumid(WundergroundMaxHumidDTO maxHumid) {
        this.maxHumid = maxHumid;
    }

    public WundergroundAveHumidDTO getAveHumid() {
        return aveHumid;
    }

    @JSONSetter(name = "avehumidity", type = JSONType.JSON_OBJECT)
    public void setAveHumid(WundergroundAveHumidDTO aveHumid) {
        this.aveHumid = aveHumid;
    }

    public WundergroundLowHumidDTO getLowHumid() {
        return lowHumid;
    }

    @JSONSetter(name = "lowhumidity", type = JSONType.JSON_OBJECT)
    public void setLowHumid(WundergroundLowHumidDTO lowHumid) {
        this.lowHumid = lowHumid;
    }
}
