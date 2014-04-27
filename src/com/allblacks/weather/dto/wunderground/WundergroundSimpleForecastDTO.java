package com.allblacks.weather.dto.wunderground;

import com.allblacks.utils.json.JSONSetter;
import com.allblacks.utils.json.impl.JSONType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marc on 26/04/2014.
 */
public class WundergroundSimpleForecastDTO {

    List<WundergroundForecastDayDTO> wundergroundForecastDayDTOs;

    public List<WundergroundForecastDayDTO> getWundergroundForecastDayDTOs() {
        return wundergroundForecastDayDTOs;
    }

    @JSONSetter(name = "forecastday", type = JSONType.LIST, objectClazz = WundergroundForecastDayDTO.class, listClazz = ArrayList.class)
    public void setWundergroundForecastDayDTOs(List<WundergroundForecastDayDTO> wundergroundForecastDayDTOs) {
        this.wundergroundForecastDayDTOs = wundergroundForecastDayDTOs;
    }
}
