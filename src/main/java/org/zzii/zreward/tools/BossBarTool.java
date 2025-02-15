package org.zzii.zreward.tools;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.zzii.zreward.ZReward;

public class BossBarTool {

    public static void createBossBar(String title, BarColor color, BarStyle style) {
        BossBar bar = Bukkit.createBossBar(title, color, style, new BarFlag[0]);
        for (Player player : Bukkit.getOnlinePlayers()) {
            bar.addPlayer(player);
        }
        playAnimation(bar);
    }

    private static void playAnimation(BossBar bossBar) {
        final double[] progress = {100.0};
        Bukkit.getScheduler().runTaskTimer(ZReward.getInstance(), () -> {
            if (progress[0] <= 0.0) {
                bossBar.removeAll();
                return;
            }
            progress[0] -= 1.0;
            bossBar.setProgress(progress[0] / 100.0);
        }, 0L, 1L);
    }
}
