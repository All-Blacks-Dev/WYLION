package com.allblacks.weather;

import android.test.AndroidTestCase;

import com.allblacks.utils.json.impl.JSONMarshaller;
import com.allblacks.weather.dto.weatherunderground.WeatherUndergroundWeatherDTO;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;

/**
 * Created by marc on 27/04/14.
 */
public class WeatherUndergroundParsingTest extends AndroidTestCase {

    public void testForecastJson() throws IOException, ParseException, IllegalAccessException, InstantiationException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        InputStream stream = getClass().getResourceAsStream("WeatherUnderground.json");
        int c;
        while ((c = stream.read()) != -1) {
            byteArrayOutputStream.write((char) c);
        }

        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        JSONMarshaller jsonMarshaller = new JSONMarshaller();
        WeatherUndergroundWeatherDTO weatherUndergroundWeatherDTO = (WeatherUndergroundWeatherDTO) jsonMarshaller.unMarshall(inputStream, WeatherUndergroundWeatherDTO.class);

        assertNotNull(weatherUndergroundWeatherDTO);
    }
}
