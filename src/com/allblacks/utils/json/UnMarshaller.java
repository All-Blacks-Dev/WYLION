package com.allblacks.utils.json;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;

/**
 * Created by Marc on 17/11/13.
 */
public interface UnMarshaller {

    Object unMarshall(CharSequence element, Class clazz) throws InstantiationException, IllegalAccessException, IOException, ParseException;

    Object unMarshall(InputStream inputStream, Class clazz) throws IllegalAccessException, InstantiationException, IOException, ParseException;
}
