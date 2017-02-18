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
package fr.litarvan.craftix.auth;

import fr.litarvan.craftix.CraftixServer;

/**
 * An Auth Manager
 */
public interface AuthManager<E extends Exception>
{
    /**
     * Authenticate the player with an username and a password
     *
     * @param server The current Craftix server
     * @param username The player email (or username if using a non-migrated account)
     * @param password The player password
     * @param clientToken The client token (see {@link AuthResult#clientToken}
     *
     * @return The {@link AuthResult} containing the player infos
     */
    AuthResult authenticate(CraftixServer server, String username, String password, String clientToken) throws E;

    /**
     * Refresh the player session with an access token
     *
     * @param server The current Craftix server
     * @param accessToken The access token got at the authentication
     * @param clientToken The client token (see {@link AuthResult#clientToken})
     *
     * @return The {@link AuthResult} containing the player infos
     */
    AuthResult refresh(CraftixServer server, String accessToken, String clientToken) throws E;
}
