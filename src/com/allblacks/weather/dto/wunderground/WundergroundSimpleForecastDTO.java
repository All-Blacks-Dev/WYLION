package com.allblacks.weather.dto.wunderground;

import java.util.List;

/**
 * Created by Marc on 26/04/2014.
 */
public class WundergroundSimpleForecastDTO {

    List<WundergroundForecastDayDTO> wundergroundForecastDayDTOs;

    public List<WundergroundForecastDayDTO> getWundergroundForecastDayDTOs() {
        return wundergroundForecastDayDTOs;
    }

    public void setWundergroundForecastDayDTOs(List<WundergroundForecastDayDTO> wundergroundForecastDayDTOs) {
        this.wundergroundForecastDayDTOs = wundergroundForecastDayDTOs;
    }
}
