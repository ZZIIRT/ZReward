package org.zzii.zreward;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.zzii.zreward.utils.Utils;

import java.io.File;

public final class ZReward extends JavaPlugin {

    private static ZReward instance;


    private static FileConfiguration rewardsConfigFile;
    private static FileConfiguration dataConfigFile;

    public static ZReward getInstance() {
        return instance;
    }

    public static FileConfiguration getRewardsConfigFile() {
        return rewardsConfigFile;
    }

    public static FileConfiguration getDataConfigFile() {
        return dataConfigFile;
    }



    @Override
    public void onEnable() {
        instance = this;
        if (getConfig().getBoolean("utils.console-messages-plugin")) {
            System.out.println(ChatColor.GREEN + "ON");
            getLogger().info(String.join("\n" + ChatColor.GRAY,
                    "",
                    "┎╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍",
                    "┋     " + Utils.replaceColor("&9" + getDescription().getName()),
                    "┋ ᴘʟᴜɢɪɴ ᴠᴇʀsɪᴏɴ: " + getDescription().getVersion(),
                    "┋ ᴀᴜᴛʜᴏʀ: " + String.join(", ", getDescription().getAuthors()),
                    "┗╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍"
            ));
        }

        saveDefaultConfig();
        File rewardsConfig = new File(getDataFolder(), "rewards.yml");
        saveResourceIfNotExists(rewardsConfig, "rewards.yml");
        rewardsConfigFile = YamlConfiguration.loadConfiguration(rewardsConfig);

        File dataConfig = new File(getDataFolder(), "data.yml");
        saveResourceIfNotExists(dataConfig, "data.yml");
        dataConfigFile = YamlConfiguration.loadConfiguration(dataConfig);

        getCommand("zreward").setExecutor(new ZCommandEx());
        getCommand("zreward").setTabCompleter(new ZTabCompleter());

    }

    @Override
    public void onDisable() {
        if (getConfig().getBoolean("utils.console-messages-plugin")) {
            System.out.println(ChatColor.RED + "OFF");
            getLogger().info(String.join("\n" + ChatColor.GRAY,
                    "",
                    "┎╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍",
                    "┋     " + Utils.replaceColor("&9" + getDescription().getName()),
                    "┋ ᴘʟᴜɢɪɴ ᴠᴇʀsɪᴏɴ: " + getDescription().getVersion(),
                    "┋ ᴀᴜᴛʜᴏʀ: " + String.join(", ", getDescription().getAuthors()),
                    "┗╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍"
            ));
        }
    }

    private void saveResourceIfNotExists(File file, String resourcePath) {
        if (!file.exists()) {
            saveResource(resourcePath, false);
        }
    }
}
