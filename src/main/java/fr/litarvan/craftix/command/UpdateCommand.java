package fr.litarvan.craftix.command;

import fr.litarvan.craftix.CraftixCommand;
import fr.litarvan.craftix.CraftixServer;
import fr.litarvan.craftix.update.UpdateManager;
import org.json.JSONObject;

public class UpdateCommand extends CraftixCommand
{
    @Override
    public String getIdentifier()
    {
        return "update";
    }

    @Override
    public JSONObject call(CraftixServer server, JSONObject params) throws Exception
    {
        UpdateManager updater = server.getUpdateManager();
        updater.update(server, params);

        return CraftixServer.SUCCESS;
    }
}
