package com.allblacks.utils.json.impl;

import com.allblacks.utils.json.Marshaller;
import com.allblacks.utils.json.UnMarshaller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Marc on 17/11/13.
 */
public class JSONMarshaller implements Marshaller, UnMarshaller {

    private static final String TAG = JSONMarshaller.class.getCanonicalName();

    public JSONMarshaller() {}

    @Override
    public Object marshall(CharSequence element, Class clazz) {
        throw new NoSuchMethodError();
    }

    @Override
    public Object unMarshall(CharSequence json, Class clazz) throws InstantiationException, IllegalAccessException, IOException, ParseException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(((String) json).getBytes());
        return unMarshall(byteArrayInputStream, clazz);
    }

    @Override
    public Object unMarshall(InputStream inputStream, Class clazz) throws IllegalAccessException, InstantiationException, IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        java.util.HashMap<String, Object> objectHashMap = (HashMap<String, Object>) jsonParser.parse(inputStream, new AtomicInteger(0), null);

        JSONPojoMapper simpleJSONMarshaller = new JSONPojoMapper(clazz);
        return simpleJSONMarshaller.unMarshal(objectHashMap);
    }
}
