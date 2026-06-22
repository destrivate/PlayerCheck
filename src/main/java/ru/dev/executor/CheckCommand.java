package ru.dev.executor;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import ru.dev.manager.CheckManager;
import ru.dev.util.ConfigUtil;

public class CheckCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("§cЭта команда доступна только игрокам!");
            return true; 
        }

        Player inspector = (Player) sender;

        if (!inspector.hasPermission("playercheck.use")) {
            inspector.sendMessage("§cУ вас недостаточно прав! &7(playercheck.use)");
            return true;
        }

        if (args.length >= 2) {
            Player suspect = Bukkit.getPlayer(args[0]);
            String action = args[1].toLowerCase();

            if (suspect == null || !suspect.isOnline()) {
                inspector.sendMessage("§cИгрок §e" + args[0] + " §cне найден или не в сети!");
                return true;
            }

            if (action.equals("start")) {
                if (CheckManager.isChecking(suspect)) {
                    inspector.sendMessage("§cИгрок §e" + suspect.getName() + " §cуже находится на проверке!");
                    return true;
                }
                CheckManager.start(suspect, inspector);
                inspector.sendMessage("§aВы успешно начали проверку игрока §e" + suspect.getName());
                for(String s : ConfigUtil.getList("on-check")){
                    suspect.sendMessage(s);
                }
                return true;
            }

            if (action.equals("stop")) {
                if (!CheckManager.isChecking(suspect)) {
                    inspector.sendMessage("§cИгрок §e" + suspect.getName() + " §cне находится на проверке!");
                    return true;
                }
                CheckManager.stop(suspect, inspector);
                inspector.sendMessage("§eВы успешно завершили проверку игрока §a" + suspect.getName());
                suspect.sendMessage("§a§lПроверка успешно пройдена! §7Приятной игры.");
                return true;
            }

            inspector.sendMessage("§cНеизвестное действие! Используйте §estart §cили §estop.");

        } else {
            inspector.sendMessage("§cНедостаточно аргументов! Пишите: §e/revise [ник] [start/stop]");
        }

        return true;
    }
}

