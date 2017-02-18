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
import fr.theshark34.openauth.AuthPoints;
import fr.theshark34.openauth.AuthenticationException;
import fr.theshark34.openauth.Authenticator;
import fr.theshark34.openauth.model.AuthAgent;
import fr.theshark34.openauth.model.AuthProfile;
import fr.theshark34.openauth.model.response.AuthResponse;
import fr.theshark34.openauth.model.response.RefreshResponse;

/**
 * OpenAuth Auth Manager
 *
 *
 * Mojang-like server authentication using the OpenAuth
 * library.
 *
 * @author Litarvan
 * @version 1.0.0
 * @since 1.0.0
 */
public class OpenAuthManager implements AuthManager<AuthenticationException>
{
    /**
     * OpenAuth authenticator
     */
    private Authenticator authenticator = new Authenticator(getAuthServer(), AuthPoints.NORMAL_AUTH_POINTS);

    @Override
    public AuthResult authenticate(CraftixServer server, String username, String password, String clientToken) throws AuthenticationException
    {
        AuthResponse response = authenticator.authenticate(AuthAgent.MINECRAFT, username, password, clientToken);
        AuthProfile profile = response.getSelectedProfile();

        return new AuthResult(profile.getName(), profile.getId(), response.getAccessToken(), response.getClientToken());
    }

    @Override
    public AuthResult refresh(CraftixServer server, String accessToken, String clientToken) throws AuthenticationException
    {
        RefreshResponse response = authenticator.refresh(accessToken, clientToken);
        AuthProfile profile = response.getSelectedProfile();

        return new AuthResult(profile.getName(), profile.getId(), response.getAccessToken(), response.getClientToken());
    }

    /**
     * @return The authentication server used
     */
    public String getAuthServer()
    {
        return Authenticator.MOJANG_AUTH_URL;
    }
}