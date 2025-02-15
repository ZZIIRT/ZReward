package org.zzii.zreward.utils;

import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

public class Utils {

    public static int getPlayTime(Player player) {
        int ticks = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
        return ticks / 20;
    }

    public static String replaceColor(String text) {
        if (text == null) return "";
        return ChatColor.translateAlternateColorCodes('&', text);
    }

}
