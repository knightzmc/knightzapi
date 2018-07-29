/*
 *     This file is part of KnightzAPI
 *
 *     KnightzAPI - A cross server communication library and general utility API for Minecraft Servers
 *     Copyright (C) 2018 Alexander Leslie John Wood
 *
 *     KnightzAPI is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     KnightzAPI is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with KnightzAPI.  If not, see <https://www.gnu.org/licenses/>.
 *
 *     The author of this program, Alexander Leslie John Wood can be contacted at alexwood2403@gmail.com
 *
 */

package uk.knightz.knightzapi.user;

import java.util.Objects;

/**
 * Holds user stats
 */
public class StatsContainer {
    private final int kills;
    private final int deaths;
    private final String userName;

    public StatsContainer(int kills, int deaths, String userName) {
        this.kills = kills;
        this.deaths = deaths;
        this.userName = userName;

    }

    public String getUserName() {
        return userName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatsContainer that = (StatsContainer) o;
        return getKills() == that.getKills() &&
                getDeaths() == that.getDeaths() &&
                Objects.equals(getUserName(), that.getUserName());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getKills(), getDeaths(), getUserName());
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }
}
