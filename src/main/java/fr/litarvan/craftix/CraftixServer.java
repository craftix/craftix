/*
 * Copyright 2017 Adrien "Litarvan" Navratil
 *
 * This file is part of Craftix.
 *
 * Craftix is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Craftix is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Craftix.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.litarvan.craftix;

import fr.litarvan.craftix.auth.AuthManager;
import fr.litarvan.craftix.auth.AuthResult;
import fr.litarvan.craftix.auth.OpenAuthManager;
import fr.litarvan.craftix.launch.CraftixLauncher;
import fr.litarvan.craftix.launch.OpenLauncherLibLauncher;
import fr.litarvan.craftix.update.UpdateManager;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import net.wytrem.logging.Logger;
import net.wytrem.logging.LoggerFactory;
import org.json.JSONObject;

/**
 * The Craftix Server
 *
 *
 * WebSocket server launched by the wrapper.
 * The launcher will connect to it for authentication,
 * update, or game launching.
 *
 * @author Litarvan
 * @version 1.0.0
 * @since 1.0.0
 */
public class CraftixServer
{
    /**
     * The Craftix version
     */
    public static final String VERSION = "1.0.0";

    /**
     * JSON object used when the command successfully executed
     * and don't return anything
     */
    public static final JSONObject SUCCESS = new JSONObject(); static {
        SUCCESS.put("success", true);
    }

    /**
     * WebSocket closed reason when Minecraft was closed
     */
    public static final String REASON_CLOSED = "mc_closed";

    /**
     * The server
     */
    private ServerSocket socket;

    /**
     * Server logger
     */
    private Logger logger;

    /**
     * The commands that the server can receive from the client
     */
    private List<CraftixCommand> commands = new ArrayList<CraftixCommand>();

    /**
     * The authentication manager
     */
    private AuthManager authManager = new OpenAuthManager();

    /**
     * The update manager
     */
    private UpdateManager updateManager;

    /**
     * The game launcher
     */
    private CraftixLauncher launcher = new OpenLauncherLibLauncher();

    /**
     * The authentication result
     */
    private AuthResult authResult;

    /**
     * A WebSocket server with the given log file
     *
     * @param address The address to use
     * @param logFile The file where the logs will be output
     */
    public CraftixServer(InetSocketAddress address, File logFile) throws IOException
    {
        socket = new ServerSocket();
        socket.bind(address);

        try
        {
            LoggerFactory.addSharedFileHandler(logFile);
        }
        catch (IOException e)
        {
            System.err.println("Couldn't output logs to " + logFile.getAbsolutePath());
            e.printStackTrace();
        }

        logger = LoggerFactory.getLogger(CraftixServer.class);
    }

    public void start()
    {
        int result = 0;

        PrintWriter out = null;
        BufferedReader in = null;
        Socket client = null;

        try
        {
            logger.info("Listening on craftix://" + socket.getInetAddress().getHostAddress() + ":" + socket.getLocalPort());
            client = socket.accept();

            logger.info("Launched conneced");

            out = new PrintWriter(new DataOutputStream(client.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(new DataInputStream(client.getInputStream())));

            onOpen(out);

            logger.info("Listening for commands...");
            String line;

            while ((line = in.readLine()) != null)
            {
                onMessage(out, line);
            }

            logger.info("Launcher closed connection, closing server");
        }
        catch (IOException e)
        {
            logger.info("Server crashed !", e);
            result = 1;
        }
        finally
        {
            if (out != null)
            {
                out.close();
            }

            if (client != null)
            {
                try
                {
                    client.close();
                }
                catch (IOException ignored)
                {
                }
            }
        }

        logger.info("Closing...");
        System.exit(result);
    }

    public void onOpen(PrintWriter writer)
    {
        JSONObject object = new JSONObject();

        object.put("status", "ok");
        object.put("version", VERSION);

        writer.print(object.toString());

        logger.info("Connection opened ! Sent status message");
    }

    public void onMessage(PrintWriter out, String message)
    {
        JSONObject object = new JSONObject(message);

        String id = object.getString("command");
        JSONObject params = object.getJSONObject("params");

        logger.info("Command received : '" + id + "'");

        for (CraftixCommand command : commands)
        {
            if (command.getIdentifier().equals(id))
            {
                try
                {
                    logger.info("Executing command '" + command.getIdentifier() + "'");
                    out.print(command.call(this, params).toString());
                }
                catch (Exception e)
                {
                    logger.error("Command failed !");
                    onError(out, e);
                }

                return;
            }
        }

        logger.error("Couldn't find the command, sending error message");
        onError(out, new IllegalArgumentException("Unknown command '" + id + "'"));
    }

    public void onError(PrintWriter out, Exception ex)
    {
        logger.error("Exception thrown : ", ex);
        logger.error("Sending it to the server");

        JSONObject object = new JSONObject();

        object.put("error", ex.getClass().getName());
        object.put("message", ex.getMessage());

        out.print(object.toString());
    }

    /**
     * Register a command that the server can receive from the client
     *
     * @param command The command to register
     */
    public void registerCommand(CraftixCommand command)
    {
        this.commands.add(command);
    }

    /**
     * @return The commands that the server can receive from the client
     */
    public CraftixCommand[] getCommands()
    {
        return this.commands.toArray(new CraftixCommand[this.commands.size()]);
    }

    /**
     * @return The authentication manager
     */
    public AuthManager getAuthManager()
    {
        return authManager;
    }

    /**
     * Set the authentication manager
     *
     * @param authManager The new authentication manager
     */
    public void setAuthManager(AuthManager authManager)
    {
        this.authManager = authManager;
    }

    /**
     * @return The Authentication result set by the
     * {@link fr.litarvan.craftix.command.AuthenticateCommand} after it
     * was executed
     */
    public AuthResult getAuthResult()
    {
        return authResult;
    }

    /**
     * Set the Authentication result (called by the
     * {@link fr.litarvan.craftix.command.AuthenticateCommand} for
     * the {@link CraftixLauncher}
     *
     * @param authResult The result of the Authentication
     */
    public void setAuthResult(AuthResult authResult)
    {
        this.authResult = authResult;
    }

    /**
     * @return The update manager
     */
    public UpdateManager getUpdateManager()
    {
        return updateManager;
    }

    /**
     * Set the update manager
     *
     * @param updateManager The new update manager
     */
    public void setUpdateManager(UpdateManager updateManager)
    {
        this.updateManager = updateManager;
    }

    /**
     * @return The game launcher
     */
    public CraftixLauncher getLauncher()
    {
        return launcher;
    }

    /**
     * Set the game launcher
     *
     * @param launcher The new game launcher
     */
    public void setLauncher(CraftixLauncher launcher)
    {
        this.launcher = launcher;
    }

    /**
     * Called by the wrapper to launch the server with default configuration
     * (127.0.0.1:given_port, ./server.log)
     *
     * @param port The port to use (wrapper will try to find one)
     */
    public static void startServer(int port) throws IOException
    {
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", port);
        File logsFile = new File("server.log");

        CraftixServer server = new CraftixServer(address, logsFile);
        server.start();
    }
}
