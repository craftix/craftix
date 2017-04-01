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

/**
 * The Launch Configuration
 *
 *
 * Configuration given by the client to give to the
 * {@link CraftixLauncher}.
 *
 * @author Litarvan
 * @version 1.0.0
 * @since 1.0.0
 */
public class LaunchConfig
{
    /**
     * Server name, used as title and folder name
     */
    private String name;

    /**
     * Minecraft version to use
     */
    private String version;

    /**
     * Minecraft parameters to add (ex: --server play.bestserveroftheworld.eu)
     */
    private String[] params;

    /**
     * Java VM parameters to add (ex: -Xms1024M)
     */
    private String[] vmParams;

    /**
     * The Game tweaks (ex: forge) to load
     */
    private String[] tweaks;

    /**
     * Launch configuration
     *
     * @param name Server name, used as title and folder name
     * @param version Minecraft version to use
     * @param params Minecraft parameters to add (ex: --server play.bestserveroftheworld.eu)
     * @param vmParams Java VM parameters to add (ex: -Xms1024M)
     * @param tweaks The Game tweaks (ex: forge) to load
     */
    public LaunchConfig(String name, String version, String[] params, String[] vmParams, String[] tweaks)
    {
        this.name = name;
        this.version = version;
        this.params = params;
        this.vmParams = vmParams;
        this.tweaks = tweaks;
    }

    /**
     * @return Server name, used as title and folder name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return Minecraft version to use
     */
    public String getVersion()
    {
        return version;
    }

    /**
     * @return Minecraft parameters to add (ex: --server play.bestserveroftheworld.eu)
     */
    public String[] getParams()
    {
        return params;
    }

    /**
     * @return Java VM parameters to add (ex: -Xms1024M)
     */
    public String[] getVmParams()
    {
        return vmParams;
    }

    /**
     * @return The Game tweaks (ex: forge) to load
     */
    public String[] getTweaks()
    {
        return tweaks;
    }
}
