package dev.rudrecciah.admincore.ban;

import dev.rudrecciah.admincore.data.DataHandler;
import dev.rudrecciah.admincore.staffmode.menus.BanChoiceMenu;
import dev.rudrecciah.admincore.staffmode.menus.BanMenu;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;

import static dev.rudrecciah.admincore.Main.plugin;

public class Banner implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof ConsoleCommandSender) {
            plugin.getLogger().info("This command can only be executed by a player! (Use minecraft:ban or minecraft:ban-ip instead if needed.)");
        }
        Player p = (Player) sender;
        if(args.length != 1 || plugin.getServer().getPlayer(args[0]) == null) {
            return false;
        }
        if(!DataHandler.getMetaBoolean((Player) sender, "staffmode")) {
            p.sendMessage(ChatColor.YELLOW + "You must be in staffmode to ban a player!");
        }
        if(!plugin.getServer().getPlayer(args[0]).hasPermission("admincore.staff")) {
            p.sendMessage(ChatColor.YELLOW + "You can't ban a staff member! (Use /minecraft:ban or /minecraft:ban-ip instead if needed.)");
        }
        p.setMetadata("staffmodeChecking", new FixedMetadataValue(plugin, plugin.getServer().getPlayer(args[0]).getUniqueId()));
        if(plugin.getConfig().getBoolean("staffmode.punishment.ban.ip-ban")) {
            BanChoiceMenu.openMenu(p);
        }else{
            BanMenu.openMenu(p);
        }
        return true;
    }
}