package io.github.orionlibs.orion_json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import io.github.orionlibs.orion_assert.Assert;
import io.github.orionlibs.orion_json.tasks.ConvertJSONObjectToMapTask;
import io.github.orionlibs.orion_json.tasks.ConvertJSONObjectToStringTask;
import java.util.HashMap;
import java.util.Map;

public class JSONService
{
    public static String convertObjectToJSON(Object objectToConvert)
    {
        Assert.notNull(objectToConvert, "The given objectToConvert is null.");
        return new Gson().toJson(objectToConvert);
    }


    public static Object convertJSONToObject(String JSONData, Object objectToConvertTo)
    {
        Assert.notEmpty(JSONData, "The given JSONData is null/empty.");
        Assert.notNull(objectToConvertTo, "The given objectToConvertTo is null.");
        return new Gson().fromJson(JSONData, objectToConvertTo.getClass());
    }


    public static Object convertJSONToObject(String JSONData, Class<?> classToConvertTo)
    {
        Assert.notEmpty(JSONData, "The given JSONData is null/empty.");
        Assert.notNull(classToConvertTo, "The given classToConvertTo is null.");
        return new Gson().fromJson(JSONData, classToConvertTo);
    }


    @SuppressWarnings("unchecked")
    public static Map<Object, Object> convertJSONToMap(String JSONData)
    {
        return (Map<Object, Object>)convertJSONToObject(JSONData, HashMap.class);
    }


    public static String convertJSONMapToString(Map<?, ?> JSONMap)
    {
        Assert.notEmpty(JSONMap, "The given JSONMap is null/empty.");
        return new Gson().toJson(JSONMap);
    }


    public static JSONObject convertJSONMapToJSONObject(Map<?, ?> JSONMap)
    {
        Assert.notEmpty(JSONMap, "The given JSONMap is null/empty.");
        String JSONString = convertJSONMapToString(JSONMap);
        return new JSONObject(JSONMap, JSONString);
    }


    public static Object convertJSONMapToObject(Map<?, ?> JSONMap, Object objectToConvertTo)
    {
        return convertJSONToObject(convertJSONMapToJSONObject(JSONMap).getJSONStringData(), objectToConvertTo);
    }


    public static Object convertJSONMapToObject(Map<?, ?> JSONMap, Class<?> classToConvertTo)
    {
        return convertJSONToObject(convertJSONMapToJSONObject(JSONMap).getJSONStringData(), classToConvertTo);
    }


    public static String convertJSONObjectToString(JSONObject jsonObject)
    {
        return ConvertJSONObjectToStringTask.run(jsonObject);
    }


    public static Map<?, ?> convertJSONObjectToMap(JSONObject jsonObject)
    {
        return ConvertJSONObjectToMapTask.run(jsonObject);
    }


    public static Object convertJSONObjectToObject(JSONObject jsonObject, Object objectToConvertTo)
    {
        return convertJSONToObject(jsonObject.getJSONStringData(), objectToConvertTo);
    }


    public static Object convertJSONObjectToObject(JSONObject jsonObject, Class<?> classToConvertTo)
    {
        return convertJSONToObject(jsonObject.getJSONStringData(), classToConvertTo);
    }


    @SuppressWarnings("unchecked")
    public static JSONObject convertJSONToJSONObject(String JSONData)
    {
        Assert.notEmpty(JSONData, "The given JSONData is null/empty.");
        Map<Object, Object> JSONMap = (Map<Object, Object>)convertJSONToObject(JSONData, HashMap.class);
        return new JSONObject(JSONMap, JSONData);
    }


    public static Map<?, ?> convertObjectToMap(Object objectToConvert)
    {
        Assert.notNull(objectToConvert, "The given objectToConvert is null.");
        String JSONString = convertObjectToJSON(objectToConvert);
        return convertJSONToMap(JSONString);
    }


    public static JSONObject convertObjectToJSONObject(Object objectToConvert)
    {
        Assert.notNull(objectToConvert, "The given objectToConvert is null.");
        String JSONString = convertObjectToJSON(objectToConvert);
        Map<Object, Object> JSONMap = convertJSONToMap(JSONString);
        return new JSONObject(JSONMap, JSONString);
    }


    public static boolean isValidJSON(String JSONData)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        try
        {
            objectMapper.readValue(JSONData, JsonNode.class);
            return true;
        }
        catch(JsonMappingException e)
        {
        }
        catch(JsonProcessingException e)
        {
        }
        return false;
    }


    public static boolean isInvalidJSON(String JSONData)
    {
        return !isValidJSON(JSONData);
    }


    public static String findValueForPath(String JSONData, String path)
    {
        String foundValue = null;
        try
        {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(JSONData);
            String[] keys = path.split("\\.");
            foundValue = findValueForPath(rootNode, keys, 0);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return foundValue;
    }


    private static String findValueForPath(JsonNode node, String[] keys, int index)
    {
        if(index < keys.length)
        {
            String currentKey = keys[index];
            int arrayIndex = parseArrayIndex(currentKey);
            if(arrayIndex != -1)
            {
                currentKey = currentKey.substring(0, currentKey.length() - (currentKey.length() - currentKey.indexOf("[")));
                JsonNode value = node.get(currentKey);
                JsonNode element = value.get(arrayIndex);
                return findValueForPath(element, keys, index + 1);
            }
            else
            {
                JsonNode value = node.get(currentKey);
                if(value != null)
                {
                    return findValueForPath(value, keys, index + 1);
                }
            }
        }
        else
        {
            return node.toString();
        }
        return null;
    }


    private static int parseArrayIndex(String key)
    {
        if(key.matches("^.+\\[\\d+\\]$"))
        {
            return Integer.parseInt(key.substring(key.indexOf("[") + 1, key.length() - 1));
        }
        return -1;
    }
}