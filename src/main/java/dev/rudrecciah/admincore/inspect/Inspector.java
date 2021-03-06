package dev.rudrecciah.admincore.inspect;

import dev.rudrecciah.admincore.data.DataHandler;
import dev.rudrecciah.admincore.staffmode.menus.MainMenu;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;

import static dev.rudrecciah.admincore.Main.plugin;

public class Inspector implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof ConsoleCommandSender) {
            plugin.getLogger().info("This command can only be executed by a player!");
        }
        Player p = (Player) sender;
        if(args.length != 1) {
            sender.sendMessage(ChatColor.YELLOW + "You need to specify a player!");
            return true;
        }
        if(plugin.getServer().getPlayer(args[0]) == null) {
            sender.sendMessage(ChatColor.YELLOW + "This player is offline!");
            return true;
        }
        if(plugin.getServer().getPlayer(args[0]) == p) {
            p.sendMessage(ChatColor.YELLOW + "You can't inspect yourself!");
            return true;
        }
        if(!DataHandler.getMetaBoolean((Player) sender, "staffmode")) {
            p.sendMessage(ChatColor.YELLOW + "You must be in staffmode to inspect a player!");
            return true;
        }
        p.setMetadata("staffmodeChecking", new FixedMetadataValue(plugin, plugin.getServer().getPlayer(args[0]).getUniqueId()));
        MainMenu.openMenu(p);
        return true;
    }
}
