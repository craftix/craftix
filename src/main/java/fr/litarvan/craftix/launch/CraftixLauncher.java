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
package fr.litarvan.craftix.launch;

import fr.litarvan.craftix.CraftixServer;
import fr.litarvan.craftix.auth.AuthManager;
import fr.litarvan.craftix.auth.AuthResult;

/**
 * A Craftix Launcher
 *
 *
 * The object that launches Minecraft with the given params,
 * default is {@link OpenLauncherLibLauncher}, but a plugin
 * can set another one.
 *
 * @author Litarvan
 * @version 1.0.0
 * @since 1.0.0
 */
public interface CraftixLauncher<E extends Exception>
{
    /**
     * Launches Minecraft with the given parameters.
     *
     * @param server The current Craftix server
     * @param config Launch config sent by the client
     * @param auth Authentication result from the {@link AuthManager}
     */
    void launch(CraftixServer server, LaunchConfig config, AuthResult auth) throws E;
}
