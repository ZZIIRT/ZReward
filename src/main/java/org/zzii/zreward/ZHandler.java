package org.zzii.zreward;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.zzii.zreward.utils.Utils;
import org.zzii.zreward.tools.BossBarTool;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ZHandler {

    private static boolean hasEnoughPlayTime(int currentPlayTime, int requiredPlayTime) {
        return currentPlayTime >= requiredPlayTime;
    }

    public static void giveReward(Player player, String rewardId) {
        List<String> allRewards = new ArrayList<>(
                ZReward.getRewardsConfigFile().getConfigurationSection("rewards").getKeys(false)
        );

        if (!allRewards.contains(rewardId)) {
            player.sendMessage(Utils.replaceColor(ZReward.getInstance().getConfig()
                    .getString("messages.unkownreward", "Награда не найдена!")));
            return;
        }

        int needPlayTime = ZReward.getRewardsConfigFile().getInt("rewards." + rewardId + ".cooldown", 0);

        if (!canReceiveAgain(player.getName(), rewardId)) {
            player.sendMessage(Utils.replaceColor(ZReward.getInstance().getConfig()
                    .getString("messages.re-receipt", "Вы уже получали эту награду.")));
            return;
        }

        String rewardName = ZReward.getRewardsConfigFile().getString("rewards." + rewardId + ".name", rewardId);
        int playedTimeInSec = Utils.getPlayTime(player);

        if (!hasEnoughPlayTime(playedTimeInSec, needPlayTime)) {
            int left = needPlayTime - playedTimeInSec;
            String msg = ZReward.getInstance().getConfig().getString("messages.little_play_time",
                    "&cНе хватает %time секунд.").replace("%time", String.valueOf(left));
            player.sendMessage(Utils.replaceColor(msg));
            return;
        }

        if (ZReward.getInstance().getConfig().getBoolean("bossbar.enable", false)) {
            String barTitle = ZReward.getInstance().getConfig().getString("bossbar.title", "%player получил %reward")
                    .replace("%player", player.getName())
                    .replace("%reward", Utils.replaceColor(rewardName));

            BossBarTool.createBossBar(
                    Utils.replaceColor(barTitle),
                    BarColor.valueOf(ZReward.getInstance().getConfig().getString("bossbar.color", "YELLOW")),
                    BarStyle.SOLID
            );
        }

        List<String> receiptMessages = ZReward.getRewardsConfigFile()
                .getStringList("rewards." + rewardId + ".receipt_message");
        for (String msg : receiptMessages) {
            String finalMsg = Utils.replaceColor(
                    msg.replace("%player", player.getName())
                            .replace("%reward", rewardName)
            );
            Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(finalMsg));
        }

        List<String> commands = ZReward.getRewardsConfigFile()
                .getStringList("rewards." + rewardId + ".gift_command");
        for (String cmd : commands) {
            String finalCmd = cmd.replace("%player", player.getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalCmd);
        }

        addToData(player.getName(), rewardId);
    }

    private static void addToData(String playerName, String rewardId) {
        List<String> savedRewards = ZReward.getDataConfigFile().getStringList("data." + playerName);
        if (savedRewards == null) {
            savedRewards = new ArrayList<>();
        }
        savedRewards.add(rewardId);
        ZReward.getDataConfigFile().set("data." + playerName, savedRewards);
        saveData();
    }

    private static boolean canReceiveAgain(String playerName, String rewardId) {
        if (ZReward.getDataConfigFile().getString("data." + playerName) == null) {
            return true;
        }
        List<String> rewards = ZReward.getDataConfigFile().getStringList("data." + playerName);
        return (rewards == null || !rewards.contains(rewardId));
    }

    private static void saveData() {
        try {
            ZReward.getDataConfigFile().save(new File(ZReward.getInstance().getDataFolder(), "data.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
