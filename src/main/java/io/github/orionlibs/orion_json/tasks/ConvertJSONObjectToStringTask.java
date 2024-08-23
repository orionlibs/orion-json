package io.github.orionlibs.orion_json.tasks;

import com.google.gson.Gson;
import io.github.orionlibs.orion_assert.Assert;
import io.github.orionlibs.orion_assert.InvalidArgumentException;
import io.github.orionlibs.orion_json.JSONObject;

public class ConvertJSONObjectToStringTask
{
    public static String run(JSONObject jsonObject)
    {
        Assert.notNull(jsonObject, "The given jsonObject is null.");
        if(jsonObject.getJSONStringData() != null)
        {
            return jsonObject.getJSONStringData();
        }
        else if(jsonObject.getJSONMapData() != null)
        {
            return new Gson().toJson(jsonObject.getJSONMapData());
        }
        else
        {
            throw new InvalidArgumentException("The given jsonObject does not have mapData or stringData in it.");
        }
    }
}