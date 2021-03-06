package dev.rudrecciah.admincore.report.reviewer;

import dev.rudrecciah.admincore.bot.chat.Chat;
import dev.rudrecciah.admincore.data.DataHandler;
import dev.rudrecciah.admincore.playerdata.PlayerDataHandler;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;

import static dev.rudrecciah.admincore.Main.plugin;

public class ReportListHandler implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Boolean console = false;
        if(sender instanceof ConsoleCommandSender) {
            console = true;
        }
        if(!console && !DataHandler.getMetaBoolean((Player) sender, "staffmode")) {
            sender.sendMessage(ChatColor.YELLOW + "You must be in staffmode to view reports!");
            return true;
        }
        File dir = new File(plugin.getDataFolder() + File.separator + "data" + File.separator + "rd");
        if(!dir.exists()) {
            dir.mkdirs();
        }
        String[] files = dir.list();
        if(files.length == 0) {
            if(console) {
                plugin.getLogger().info("There are no open reports!");
                return true;
            }
            sender.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "[STAFFMODE]" + ChatColor.YELLOW + "There are no open reports!");
            return true;
        }
        ArrayList<String> openReports = new ArrayList<>();
        for(OfflinePlayer player : plugin.getServer().getOfflinePlayers()) {
            ArrayList<Report> reports = Getter.getReports(player.getUniqueId());
            if(reports != null) {
                openReports.add(player.getName() + " has " + reports.size() + " open reports!");
            }
        }
        if(openReports.size() != 0) {
            if(console) {
                plugin.getLogger().info("[OPEN REPORTS]");
                for(String str : openReports) {
                    plugin.getLogger().info(str);
                }
            }else{
                sender.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "[OPEN REPORTS]");
                for(String str : openReports) {
                    sender.sendMessage(ChatColor.YELLOW + str);
                }
            }
        }else{
            if(console) {
                plugin.getLogger().info("There are no open reports!");
                return true;
            }
            sender.sendMessage(ChatColor.YELLOW + "There are no open reports!");
            return true;
        }
        return true;
    }
}
