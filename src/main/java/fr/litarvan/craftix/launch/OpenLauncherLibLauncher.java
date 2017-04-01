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
import fr.litarvan.craftix.auth.AuthResult;
import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.openlauncherlib.external.*;
import fr.theshark34.openlauncherlib.internal.*;
import fr.theshark34.openlauncherlib.minecraft.*;
import fr.theshark34.openlauncherlib.minecraft.util.GameDirGenerator;
import fr.theshark34.openlauncherlib.util.ProcessLogManager;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.wytrem.logging.Logger;
import net.wytrem.logging.LoggerFactory;

/**
 * OpenLauncherLib Craftix Launcher
 *
 *
 * Launches Minecraft using the OpenLauncherLib.
 *
 * @author Litarvan
 * @version 1.0.0
 * @since 1.0.0
 */
public class OpenLauncherLibLauncher implements CraftixLauncher<LaunchException>
{
    private static final Logger logger = LoggerFactory.getLogger(OpenLauncherLibLauncher.class);

    @Override
    public void launch(CraftixServer server, LaunchConfig config, AuthResult auth) throws LaunchException
    {
        logger.info("Launching Minecraft using OpenLauncherLib");

        AuthInfos authInfos = new AuthInfos(auth.getUsername(), auth.getAccessToken(), auth.getUuid());
        GameType type = getGameType(config);

        logger.info("GameType detected : " + type.getName());

        ArrayList<GameTweak> tweaks = new ArrayList<GameTweak>();

        for (String str : config.getTweaks())
        {
            String tweak = str.trim();

            if (tweak.equals("forge"))
            {
                tweaks.add(GameTweak.FORGE);
            }
            else if (tweak.equals("optifine"))
            {
                tweaks.add(GameTweak.OPTIFINE);
            }
            else if (tweak.equals("shader"))
            {
                tweaks.add(GameTweak.SHADER);
            }

            logger.info("Adding tweak : " + tweaks.get(tweaks.size() - 1).getName());
        }

        GameInfos infos = new GameInfos(config.getName(), getGameDir(config.getName()), new GameVersion(config.getVersion(), type), tweaks.toArray(new GameTweak[tweaks.size()]));
        logger.info("From directory : " + infos.getGameDir().getAbsolutePath());

        if (config.getVmParams() == null || config.getVmParams().length == 0)
        {
            logger.info("Using internal launching");
            internalLaunch(infos, authInfos);
        }
        else
        {
            logger.info("Using external launching");
            externalLaunch(infos, config.getVmParams(), authInfos);
        }
    }

    protected void internalLaunch(GameInfos infos, AuthInfos authInfos) throws LaunchException
    {
        InternalLaunchProfile profile = MinecraftLauncher.createInternalProfile(infos, getGameFolder(), authInfos);
        InternalLauncher launcher = new InternalLauncher(profile);

        logger.info("Launching Minecraft : " + infos.getServerName());
        launcher.launch();
    }

    protected void externalLaunch(GameInfos infos, String[] vmParams, AuthInfos authInfos) throws LaunchException
    {
        ExternalLaunchProfile profile = MinecraftLauncher.createExternalProfile(infos, getGameFolder(), authInfos);

        List<String> params = Arrays.asList(vmParams);

        if (profile.getVmArgs() != null)
        {
            profile.getVmArgs().addAll(params);
        }
        else
        {
            profile.setVmArgs(params);
        }

        ExternalLauncher launcher = new ExternalLauncher(profile);
        Process p = launcher.launch();
        ProcessLogManager manager = new ProcessLogManager(p.getInputStream());

        logger.info("Launched Minecraft : " + infos.getServerName());
        manager.start();

        try
        {
            p.waitFor();
        }
        catch (InterruptedException ignored)
        {
        }
    }

    public GameType getGameType(LaunchConfig config)
    {
        String version = config.getVersion().trim();
        int first = version.indexOf('.');
        int second = version.lastIndexOf('.');
        String majorVersion = first == second ? version : version.substring(first, second);
        float ver = Float.parseFloat(majorVersion);

        GameType type = GameType.V1_8_HIGHER;
        if (version.equals("1.7.10"))
        {
            type = GameType.V1_7_10;
        }
        else if (ver < 1.6)
        {
            type = GameType.V1_5_2_LOWER;
        }
        else if (ver < 1.8 || version.equals("1.7.2"))
        {
            type = GameType.V1_7_2_LOWER;
        }

        return type;
    }

    public GameFolder getGameFolder()
    {
        return GameFolder.BASIC;
    }

    public File getGameDir(String serverName)
    {
        return GameDirGenerator.createGameDir(serverName);
    }
}
