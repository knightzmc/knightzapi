package uk.knightz.knightzapi.user;

import java.util.Objects;

/**
 * This class was created by AlexL (Knightz) on 15/02/2018 at 12:27.
 * Copyright Knightz 2018
 * For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 **/
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
