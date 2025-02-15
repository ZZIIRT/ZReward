package org.zzii.zreward;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.zzii.zreward.utils.Utils;

import java.io.File;
import java.io.IOException;

public class ZCommandEx implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Только игроки могут использовать эту команду!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (!player.hasPermission("ZReward.*")) {
                player.sendMessage(Utils.replaceColor("&cУ вас нет прав на выполнение этой команды."));
                return true;
            }

            ZReward.getInstance().reloadConfig();

            File rewardsFile = new File(ZReward.getInstance().getDataFolder(), "rewards.yml");
            File dataFile = new File(ZReward.getInstance().getDataFolder(), "data.yml");

            if (rewardsFile.exists()) {
                try {
                    ZReward.getRewardsConfigFile().load(rewardsFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InvalidConfigurationException e) {
                    throw new RuntimeException(e);
                }
            }

            if (dataFile.exists()) {
                try {
                    ZReward.getDataConfigFile().load(dataFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InvalidConfigurationException e) {
                    throw new RuntimeException(e);
                }
            }

            player.sendMessage(Utils.replaceColor("&aКонфигурация, награды и данные перезагружены!"));
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(Utils.replaceColor(ZReward.getInstance().getConfig()
                    .getString("messages.error", "&cИспользуйте: /reward <id>")));
            return true;
        }

        ZHandler.giveReward(player, args[0]);
        return true;
    }
}
