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

import fr.litarvan.craftix.launch.CraftixLauncher;
import fr.litarvan.craftix.launch.OpenLauncherLibLauncher;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import net.wytrem.logging.Logger;
import net.wytrem.logging.LoggerFactory;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
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
public class CraftixServer extends WebSocketServer
{
    /**
     * The Craftix version
     */
    public static final String VERSION = "1.0.0";

    /**
     * WebSocket closed reason when Minecraft was closed
     */
    public static final String REASON_CLOSED = "mc_closed";

    /**
     * Server logger
     */
    private Logger logger;

    /**
     * The commands that the server can receive from the client
     */
    private List<CraftixCommand> commands = new ArrayList<>();

    /**
     * The Game launcher
     */
    private CraftixLauncher launcher = new OpenLauncherLibLauncher();

    /**
     * A WebSocket server with the given log file
     *
     * @param address The address to use
     * @param logFile The file where the logs will be output
     */
    public CraftixServer(InetSocketAddress address, File logFile)
    {
        super(address);

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

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake)
    {
        JSONObject object = new JSONObject();

        object.put("status", "ok");
        object.put("version", VERSION);

        conn.send(object.toString());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote)
    {
        String error = "(code : " + code + ", reason : " + reason + ")";

        if (remote)
        {
            logger.info("Remote closed connection " + error);
            logger.info("Exiting");

            System.exit(0);
        }

        if (reason.equals(REASON_CLOSED))
        {
            logger.info("Minecraft was closed " + error);
            logger.info("Exiting");

            System.exit(0);
        }

        logger.info("Unexcepted socket close, " + (remote ? "by the server itself, it probably crashed. " : "by remote, it was probably closed. ") + error);
        System.exit(1);
    }

    @Override
    public void onMessage(WebSocket conn, String message)
    {
        JSONObject object = new JSONObject(message);

        String id = object.getString("command");
        JSONObject params = object.getJSONObject("params");

        for (CraftixCommand command : commands)
        {
            if (command.getIdentifier().equals(id))
            {
                conn.send(command.call(params).toString());
                return;
            }
        }

        onError(conn, new IllegalArgumentException("Unknown command '" + id + "'"));
    }

    @Override
    public void onError(WebSocket conn, Exception ex)
    {
        logger.error("Exception thrown : ", ex);
        logger.error("Sending it to the server");

        JSONObject object = new JSONObject();

        object.put("type", "error");
        object.put("message", ex.getMessage());
        object.put("type", ex.getClass().getName());

        conn.send(object.toString());
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
     * Called by the wrapper to launch the server with default configuration
     * (127.0.0.1:given_port, ./server.log)
     *
     * @param port The port to use (wrapper will try to find one)
     */
    public static void startServer(int port)
    {
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", port);
        File logsFile = new File("server.log");

        CraftixServer server = new CraftixServer(address, logsFile);
        server.start();
    }
}
