package io.github.orionlibs.orion_json;

import java.util.Map;

public class JSONObject
{
    private Map<?, ?> JSONMapData;
    private String JSONStringData;


    public JSONObject()
    {
    }


    public JSONObject(Map<?, ?> JSONMapData, String JSONStringData)
    {
        this.JSONMapData = JSONMapData;
        this.JSONStringData = JSONStringData;
    }


    public Map<?, ?> getJSONMapData()
    {
        return JSONMapData;
    }


    public String getJSONStringData()
    {
        return JSONStringData;
    }
}