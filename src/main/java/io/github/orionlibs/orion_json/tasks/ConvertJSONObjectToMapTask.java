package io.github.orionlibs.orion_json.tasks;

import io.github.orionlibs.orion_assert.Assert;
import io.github.orionlibs.orion_assert.InvalidArgumentException;
import io.github.orionlibs.orion_json.JSONObject;
import io.github.orionlibs.orion_json.JSONService;
import java.util.Map;

public class ConvertJSONObjectToMapTask
{
    public static Map<?, ?> run(JSONObject jsonObject)
    {
        Assert.notNull(jsonObject, "The given jsonObject is null.");
        if(jsonObject.getJSONMapData() != null)
        {
            return jsonObject.getJSONMapData();
        }
        else if(jsonObject.getJSONStringData() != null)
        {
            return JSONService.convertJSONToMap(jsonObject.getJSONStringData());
        }
        else
        {
            throw new InvalidArgumentException("The given jsonObject does not have mapData or stringData in it.");
        }
    }
}