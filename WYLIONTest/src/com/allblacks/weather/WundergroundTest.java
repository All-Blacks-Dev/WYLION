package com.allblacks.weather;

import android.test.AndroidTestCase;

import com.allblacks.utils.json.impl.JSONMarshaller;
import com.allblacks.utils.json.impl.JSONParser;
import com.allblacks.utils.json.impl.JSONPojoMapper;
import com.allblacks.weather.dto.wunderground.WundergroundWeatherDTO;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by marc on 27/04/14.
 */
public class WundergroundTest extends AndroidTestCase {

    public void testForecastJson() throws IOException, ParseException, IllegalAccessException, InstantiationException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        InputStream stream = getClass().getResourceAsStream("wunderground.json");
        int c;
        while ((c = stream.read()) != -1) {
            byteArrayOutputStream.write((char) c);
        }

        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        JSONMarshaller jsonMarshaller = new JSONMarshaller();
        WundergroundWeatherDTO wundergroundWeatherDTO = (WundergroundWeatherDTO) jsonMarshaller.unMarshall(inputStream, WundergroundWeatherDTO.class);

        assertNotNull(wundergroundWeatherDTO);
    }
}
