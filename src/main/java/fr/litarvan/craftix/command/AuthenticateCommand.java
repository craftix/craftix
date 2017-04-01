package fr.litarvan.craftix.command;

import fr.litarvan.craftix.CraftixCommand;
import fr.litarvan.craftix.CraftixServer;
import fr.litarvan.craftix.auth.AuthManager;
import fr.litarvan.craftix.auth.AuthResult;
import org.json.JSONObject;

public class AuthenticateCommand extends CraftixCommand
{
    @Override
    public String getIdentifier()
    {
        return "authenticate";
    }

    @Override
    public JSONObject call(CraftixServer server, JSONObject params) throws Exception
    {
        AuthManager auth = server.getAuthManager();
        AuthResult result = auth.authenticate(server, params.getString("username"), params.getString("password"), params.getString("clientToken"));

        server.setAuthResult(result);

        return new JSONObject(result);
    }
}
