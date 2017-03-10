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

import org.json.JSONObject;

/**
 * A Craftix Command
 *
 *
 * A command that the server can receive from the client.
 * Looks like that :
 *
 * <code>
 *     {
 *         "id": "(command identifier)",
 *         "params": {
 *             (command params)
 *         }
 *     }
 * </code>
 *
 * A command can return a JSONObject, the server will send :
 *
 * <code>
 *     {
 *         "id": "(command indentifier)",
 *         "response": (command json object response)
 *     }
 * </code>
 *
 * @author Litarvan
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class CraftixCommand
{
    /**
     * @return The command unique identifier (lower case, no space)
     */
    public abstract String getIdentifier();

    /**
     * Do the command job
     *
     * @param params The parameters sent by the client
     *
     * @return A JSONObject that will be sent to the client
     */
    public abstract JSONObject call(CraftixServer server, JSONObject params) throws Exception;
}
