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
import fr.litarvan.openauth.*;
import fr.litarvan.openauth.model.AuthAgent;
import fr.litarvan.openauth.model.AuthProfile;
import fr.litarvan.openauth.model.response.*;
import net.wytrem.logging.Logger;
import net.wytrem.logging.LoggerFactory;

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
    private static final Logger logger = LoggerFactory.getLogger(OpenAuthManager.class);

    /**
     * OpenAuth authenticator
     */
    private Authenticator authenticator = new Authenticator(getAuthServer(), AuthPoints.NORMAL_AUTH_POINTS);

    @Override
    public AuthResult authenticate(CraftixServer server, String username, String password, String clientToken) throws AuthenticationException
    {
        logger.info("Authenticating '" + username + "' (" + getAuthServer() + AuthPoints.NORMAL_AUTH_POINTS.getAuthenticatePoint() + ")");

        AuthResponse response = authenticator.authenticate(AuthAgent.MINECRAFT, username, password, clientToken);
        AuthProfile profile = response.getSelectedProfile();

        logger.info("Success ! Player '" + profile.getName() + "' (" + profile.getId() + ") connected !");

        return new AuthResult(profile.getName(), profile.getId(), response.getAccessToken(), response.getClientToken());
    }

    @Override
    public AuthResult refresh(CraftixServer server, String accessToken, String clientToken) throws AuthenticationException
    {
        logger.info("Refreshing session (" + getAuthServer() + AuthPoints.NORMAL_AUTH_POINTS.getRefreshPoint() + ")");

        RefreshResponse response = authenticator.refresh(accessToken, clientToken);
        AuthProfile profile = response.getSelectedProfile();

        logger.info("Success ! Player '" + profile.getName() + "' (" + profile.getId() + ") connected !");

        return new AuthResult(profile.getName(), profile.getId(), response.getAccessToken(), response.getClientToken());
    }

    @Override
    public boolean logout(CraftixServer server, String accessToken, String clientToken) throws AuthenticationException
    {
        logger.info("Terminating session (" + getAuthServer() + AuthPoints.NORMAL_AUTH_POINTS.getInvalidatePoint() + ")");

        authenticator.invalidate(accessToken, clientToken);

        logger.info("Logged out !");

        return true;
    }

    /**
     * @return The authentication server used
     */
    public String getAuthServer()
    {
        return Authenticator.MOJANG_AUTH_URL;
    }
}
