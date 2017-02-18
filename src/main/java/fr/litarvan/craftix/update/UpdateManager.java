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
package fr.litarvan.craftix.update;

import fr.litarvan.craftix.CraftixServer;
import org.json.JSONObject;

/**
 * The Update Manager
 *
 *
 * Manage the game updating, default is {@link SUpdateManager}.
 *
 * @author Litarvan
 * @version 1.0.0
 * @since 1.0.0
 */
public interface UpdateManager<E extends Exception>
{
    /**
     * Updates Minecraft
     *
     * @param server The current Craftix Server
     * @param params The params sent by the client
     *
     * @throws E Any exception
     */
    void update(CraftixServer server, JSONObject params) throws E;
}
