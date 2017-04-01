package fr.litarvan.craftix.command;

import fr.litarvan.craftix.CraftixCommand;
import fr.litarvan.craftix.CraftixServer;
import fr.litarvan.craftix.launch.CraftixLauncher;
import fr.litarvan.craftix.launch.LaunchConfig;
import org.json.JSONArray;
import org.json.JSONObject;

public class LaunchCommand extends CraftixCommand
{
    @Override
    public String getIdentifier()
    {
        return "launch";
    }

    @Override
    public JSONObject call(CraftixServer server, JSONObject params) throws Exception
    {
        CraftixLauncher launcher = server.getLauncher();
        LaunchConfig config = new LaunchConfig(params.getString("name"),
                                               params.getString("version"),
                                               toArray(params.getJSONArray("params")),
                                               toArray(params.getJSONArray("vmParams")),
                                               toArray(params.getJSONArray("tweaks")));

        launcher.launch(server, config, server.getAuthResult());

        return CraftixServer.SUCCESS;
    }

    private String[] toArray(JSONArray array)
    {
        String[] result = new String[array.length()];

        for (int i = 0; i < result.length; i++)
        {
            result[i] = (String) array.get(i);
        }

        return result;
    }
}
