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

/**
 * The Authentication Result
 *
 *
 * This results received from the {@link AuthManager}
 * (player infos).
 *
 * @author Litarvan
 * @version 1.0.0
 * @since 1.0.0
 */
public class AuthResult
{
    /**
     * The player username
     */
    private String username;

    /**
     * The player unique ID (as a UUID)
     */
    private String uuid;

    /**
     * The client token (unique token generated for each computer)
     */
    private String clientToken;

    /**
     * The access token (session id)
     */
    private String accessToken;

    /**
     * Authentication result
     *
     * @param username The player username
     * @param uuid The player unique ID (as a UUID)
     * @param clientToken The client token (unique token generated for each computer)
     * @param accessToken The access token (session id)
     */
    public AuthResult(String username, String uuid, String clientToken, String accessToken)
    {
        this.username = username;
        this.uuid = uuid;
        this.clientToken = clientToken;
        this.accessToken = accessToken;
    }

    /**
     * @return The player username
     */
    public String getUsername()
    {
        return username;
    }

    /**
     * @return The player unique ID (as a UUID)
     */
    public String getUuid()
    {
        return uuid;
    }

    /**
     * @return The client token (unique token generated for each computer)
     */
    public String getClientToken()
    {
        return clientToken;
    }

    /**
     * @return The access token (session id)
     */
    public String getAccessToken()
    {
        return accessToken;
    }
}
