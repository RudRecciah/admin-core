package dev.rudrecciah.admincore.alias;

import dev.rudrecciah.admincore.data.DataHandler;
import dev.rudrecciah.admincore.staffmode.grabber.StatsGrabber;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static dev.rudrecciah.admincore.Main.plugin;

public class AliasChecker implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof ConsoleCommandSender) {
            plugin.getLogger().info("This command can only be executed by a player!");
        }
        Player p = (Player) sender;
        if(args.length != 1 || plugin.getServer().getPlayer(args[0]) == null) {
            return false;
        }
        if(!DataHandler.getMetaBoolean((Player) sender, "staffmode")) {
            p.sendMessage(ChatColor.YELLOW + "You must be in staffmode to ban a player!");
        }
        if(!plugin.getServer().getPlayer(args[0]).hasPermission("admincore.staff")) {
            p.sendMessage(ChatColor.YELLOW + "You can't ban a staff member!");
        }
        p.setMetadata("staffmodeChecking", new FixedMetadataValue(plugin, plugin.getServer().getPlayer(args[0]).getUniqueId()));
        Player t = plugin.getServer().getPlayer(args[0]);
        List<String> aliases = StatsGrabber.grabAliases(t);
        p.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "[" + t.getName() + "'S ALIASES]");
        for(String str : aliases) {
            p.sendMessage(str);
        }
        return true;
    }
}