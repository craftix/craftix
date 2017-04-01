package fr.litarvan.craftix.test;

import fr.litarvan.craftix.CraftixServer;
import java.io.IOException;

public class Launch
{
    public static void main(String[] args)
    {
        try
        {
            CraftixServer.startServer(6544);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
