/*
 * Copyright (C) 2011-2012  Marc Boulanger
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.*
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.allblacks.utils.json.impl;

import android.os.Debug;
import android.util.Log;

import com.allblacks.utils.json.JSONSetter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JSONPojoMapper {

    private Class<?> clazz;
    private static final String TAG = JSONPojoMapper.class.getCanonicalName();

    public JSONPojoMapper(Class<?> clazz) {
        this.clazz = clazz;
    }

    /**
     * This method creates an instance of the object targeted by the Class passed to the constructor,
     * it then tries to call all the methods with the JSONSetter annotation with the corresponding field.
     *
     * @param jsonObject This is the JSON object that will be used to fill-up an
     *                   instance of the Class that is targeted.
     * @return
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    @SuppressWarnings("unchecked")
    public Object unMarshal(final Map<String, Object> jsonObject) throws InstantiationException, IllegalAccessException {
        Object result = clazz.newInstance();

        try {
            Method[] methods = clazz.getMethods();

            for(int i = 0; i < methods.length; i++) {
                JSONSetter setter = methods[i].getAnnotation(JSONSetter.class);
                /**
                 * Checking if the method has the JSONSetter annotation This
                 * only allows one parameter per setter method
                 */
                if(setter != null) {
                    /**
                     * Checking the JSONType defined in the annotation
                     */
                    if(setter.type() == JSONType.SIMPLE) {
                        /**
                         * Used for simple object (String, Integer, etc ...)
                         */
                        try {
                            Object parameter = jsonObject.get(setter.name());
                            methods[i].invoke(result, parameter);
                        }
                        catch(NullPointerException exception) {
                            if(Debug.isDebuggerConnected()) {
                                Log.d(TAG, methods[i].getName() + " " + exception.getLocalizedMessage());
                            }
                        }
                        catch(IllegalArgumentException exception) {
                            /**
                             * This happens if the object returned by the getter
                             * is a null, it would be defined as a JSONObject
                             * and thus cause an IllegalArgumentException when
                             * calling the targeted method.
                             *
                             * This is yet another problem in an Android API
                             * which is bypassed by creating an empty object of
                             * the awaited type.
                             */
                            if(Debug.isDebuggerConnected()) {
                                Log.w(TAG, methods[i].getName() + " " + exception.getLocalizedMessage());
                            }
                            try {
                                Constructor<?>[] constructors = methods[i].getParameterTypes()[0].getConstructors();
                                Object parameter = null;

                                /**
                                 * Checking all the constructor of the parameter
                                 * object.
                                 */
                                for(Constructor<?> constructor : constructors) {
                                    Class<?>[] params = constructor.getParameterTypes();
                                    if(params.length == 0) {
                                        /**
                                         * If the empty constructor is found we
                                         * use it.
                                         */
                                        parameter = constructor.newInstance();
                                        break;
                                    }
                                    else if(params[0].isPrimitive()) {
                                        /**
                                         * If a constructor using a primitive is
                                         * found we use it, this happens for
                                         * classes that extend Number.
                                         */
                                        parameter = constructor.newInstance(0);
                                    }
                                }

                                methods[i].invoke(result, parameter);
                            }
                            catch(Exception e) {
                                if(Debug.isDebuggerConnected()) {
                                    Log.d(TAG, methods[i].getName() + " " + e.getLocalizedMessage());
                                }
                            }
                        }
                    }
                    if(setter.type() == JSONType.JSON_OBJECT) {
                        /**
                         * Used for object that are instantiated from JSON
                         * (Show, Movie, etc ...)
                         */
                        try {
                            JSONPojoMapper simpleJSONMarshaller = new JSONPojoMapper(methods[i].getParameterTypes()[0]);
                            Map<String, Object> jsonElement = (Map<String, Object>) jsonObject.get(setter.name());
                            Object parameter = simpleJSONMarshaller.unMarshal(jsonElement);
                            methods[i].invoke(result, parameter);
                        }
                        catch(Exception exception) {
                            if(Debug.isDebuggerConnected()) {
                                Log.d(TAG, methods[i].getName() + " " + exception.getLocalizedMessage());
                            }
                            try {
                                Constructor<?>[] constructors = methods[i].getParameterTypes()[0].getConstructors();
                                Object parameter = null;

                                /**
                                 * Checking all the constructor of the parameter
                                 * object.
                                 */
                                for(Constructor<?> constructor : constructors) {
                                    Class<?>[] params = constructor.getParameterTypes();
                                    if(params.length == 0) {
                                        /**
                                         * If the empty constructor is found we
                                         * use it.
                                         */
                                        parameter = constructor.newInstance();
                                        break;
                                    }
                                    else if(params[0].isPrimitive()) {
                                        /**
                                         * If a constructor using a primitive is
                                         * found we use it, this happens for
                                         * classes that extend Number.
                                         */
                                        parameter = constructor.newInstance(0);
                                    }
                                }

                                methods[i].invoke(result, parameter);
                            }
                            catch(Exception e) {
                                if(Debug.isDebuggerConnected()) {
                                    Log.d(TAG, methods[i].getName() + " " + e.getLocalizedMessage());
                                }
                            }
                        }
                    }
                    if(setter.type() == JSONType.LIST) {
                        /**
                         * Used for object that represent a Collection (List,
                         * ArrayList, Vector, etc ...)
                         */
                        try {
                            List jsonArray = (List) jsonObject.get(setter.name());
                            Collection<Object> collection = null;
                            if(methods[i].getParameterTypes()[0] == List.class) {
                                collection = (Collection<Object>) setter.listClazz().newInstance();
                            }
                            else {
                                collection = (Collection<Object>) methods[i].getParameterTypes()[0].newInstance();
                            }

                            for(int j = 0; j < jsonArray.size(); j++) {
                                Object element = jsonArray.get(j);
                                if(setter.objectClazz() != Void.class) {
                                    JSONPojoMapper simpleJSONMarshaller = new JSONPojoMapper(setter.objectClazz());
                                    element = simpleJSONMarshaller.unMarshal((Map<String, Object>) element);
                                }
                                collection.add(element);
                            }
                            methods[i].invoke(result, collection);
                        }
                        catch(Exception exception) {
                            if(Debug.isDebuggerConnected()) {
                                Log.d(TAG, methods[i].getName() + " " + exception.getLocalizedMessage());
                            }
                        }
                    }
                    if(setter.type() == JSONType.UNKNOWN_KEY_ELEMENTS) {
                        /**
                         * Used for object that represent a Collection (List,
                         * ArrayList, Vector, etc ...) and that do not have a
                         * key that is predefined.
                         */
                        try {
                            Collection<Object> collection = null;
                            if(methods[i].getParameterTypes()[0] == List.class) {
                                collection = (Collection<Object>) setter.listClazz().newInstance();
                            }
                            else {
                                collection = (Collection<Object>) methods[i].getParameterTypes()[0].newInstance();
                            }

                            Iterator iterator = jsonObject.keySet().iterator();
                            while(iterator.hasNext()) {
                                String key = (String) iterator.next();
                                Object element = jsonObject.get(key);
                                if(setter.objectClazz() != Void.class) {
                                    JSONPojoMapper simpleJSONMarshaller = new JSONPojoMapper(setter.objectClazz());
                                    element = simpleJSONMarshaller.unMarshal((Map<String, Object>) element);
                                    if(element instanceof UnknowMappingElement) {
                                        ((UnknowMappingElement) element).setId(key);
                                    }
                                }
                                collection.add(element);
                            }
                            methods[i].invoke(result, collection);
                        }
                        catch(Exception exception) {
                            if(Debug.isDebuggerConnected()) {
                                Log.d(TAG, methods[i].getName() + " " + exception.getLocalizedMessage());
                            }
                        }
                    }
                    if(setter.type() == JSONType.ENUM) {
                        /**
                         * Used when a String should be transformed into an Enum value,
                         * this is usefull when we have a know limited set of data.
                         */
                        try {
                            Enum parameter = Enum.valueOf((Class<Enum>) setter.objectClazz().newInstance(), setter.name());
                            methods[i].invoke(result, parameter);
                        }
                        catch(Exception exception) {
                            if(Debug.isDebuggerConnected()) {
                                Log.d(TAG, methods[i].getName() + " " + exception.getLocalizedMessage());
                            }
                        }
                    }
                }
            }
        }
        catch(
                Exception e
                )

        {
            if(Debug.isDebuggerConnected()) {
                Log.d(TAG, e.getLocalizedMessage());
            }
        }

        return result;
    }
}
