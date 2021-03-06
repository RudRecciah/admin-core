package dev.rudrecciah.admincore.webhook;

import com.mrpowergamerbr.temmiewebhook.DiscordEmbed;
import com.mrpowergamerbr.temmiewebhook.DiscordMessage;
import com.mrpowergamerbr.temmiewebhook.TemmieWebhook;
import com.mrpowergamerbr.temmiewebhook.embed.FieldEmbed;
import com.mrpowergamerbr.temmiewebhook.embed.FooterEmbed;
import com.mrpowergamerbr.temmiewebhook.embed.ThumbnailEmbed;
import dev.rudrecciah.admincore.data.DataHandler;
import dev.rudrecciah.admincore.playerdata.PlayerDataHandler;
import dev.rudrecciah.admincore.punishlogs.PunishmentLogger;
import org.bukkit.BanList;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static dev.rudrecciah.admincore.Main.plugin;

public class BanLogger {
    public static void logBan(OfflinePlayer p) {
        StringBuilder reasonBuilder = new StringBuilder();
        StringBuilder typeBuilder = new StringBuilder();
        StringBuilder lengthBuilder = new StringBuilder();
        StringBuilder sourceBuilder = new StringBuilder();
        Boolean ip = false;
        if(plugin.getServer().getBanList(BanList.Type.NAME).isBanned(p.getUniqueId().toString())) {
            typeBuilder.append("Player");
            if(plugin.getServer().getBanList(BanList.Type.NAME).getBanEntry(p.getUniqueId().toString()).getReason() != null) {
                reasonBuilder.append(plugin.getServer().getBanList(BanList.Type.NAME).getBanEntry(p.getUniqueId().toString()).getReason());
            }else{
                reasonBuilder.append("None");
            }
            if(plugin.getServer().getBanList(BanList.Type.NAME).getBanEntry(p.getUniqueId().toString()).getExpiration() != null) {
                lengthBuilder.append(plugin.getConfig().getInt("staffmode.punishment.ban.tempban-length"));
            }else{
                lengthBuilder.append("Forever");
            }
            if(plugin.getServer().getBanList(BanList.Type.NAME).getBanEntry(p.getUniqueId().toString()).getSource() != null) {
                sourceBuilder.append(plugin.getServer().getBanList(BanList.Type.NAME).getBanEntry(p.getUniqueId().toString()).getSource());
            }else{
                sourceBuilder.append("Unknown");
            }
        }else{
            ip = true;
            typeBuilder.append("IP");
            if(plugin.getServer().getBanList(BanList.Type.IP).getBanEntry(PlayerDataHandler.getIP(p.getUniqueId())).getReason() != null) {
                reasonBuilder.append(plugin.getServer().getBanList(BanList.Type.IP).getBanEntry(PlayerDataHandler.getIP(p.getUniqueId())).getReason());
            }else{
                reasonBuilder.append("None");
            }
            if(plugin.getServer().getBanList(BanList.Type.IP).getBanEntry(PlayerDataHandler.getIP(p.getUniqueId())).getExpiration() != null) {
                lengthBuilder.append("Until ").append(plugin.getServer().getBanList(BanList.Type.IP).getBanEntry(PlayerDataHandler.getIP(p.getUniqueId())).getExpiration());
            }else{
                lengthBuilder.append("Forever");
            }
            if(plugin.getServer().getBanList(BanList.Type.IP).getBanEntry(PlayerDataHandler.getIP(p.getUniqueId())).getSource() != null) {
                sourceBuilder.append(plugin.getServer().getBanList(BanList.Type.IP).getBanEntry(PlayerDataHandler.getIP(p.getUniqueId())).getSource());
            }else{
                sourceBuilder.append("Unknown");
            }
        }
        String type = typeBuilder.toString();
        if(reasonBuilder.toString().contains("\n")) {
            reasonBuilder.append(reasonBuilder.toString().split("\n")[0]);
        }
        String reason = reasonBuilder.toString();
        String length = lengthBuilder.toString();
        String source = sourceBuilder.toString();
        String[] smallerReason = reason.split("\n");
        reason = smallerReason[0];
        if(ip) {
            PunishmentLogger.logIpBan(p, reason, source);
        }else{
            if(!length.equalsIgnoreCase("forever")) {
                PunishmentLogger.logTempban(p, reason, length, source);
            }else{
                PunishmentLogger.logBan(p, reason, source);
            }
        }
        List<Player> players = (List) plugin.getServer().getOnlinePlayers();
        if(type.equalsIgnoreCase("player")) {
            for (Player player : players) {
                if (player.hasPermission("admincore.staff")) {
                    player.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "[STAFF CHANNEL] " + ChatColor.LIGHT_PURPLE + "" + ChatColor.ITALIC + "Admincore " + ChatColor.YELLOW + "Ban Logger: " + p.getName() + " was just banned " + length.toLowerCase(Locale.ROOT) + "!");
                    if(DataHandler.getBoolean(player, "notifs")) {
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1f);
                    }
                }
            }
        }else{
            for (Player player : players) {
                if (player.hasPermission("admincore.staff")) {
                    player.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "[STAFF CHANNEL] " + ChatColor.LIGHT_PURPLE + "" + ChatColor.ITALIC + "Admincore " + ChatColor.YELLOW + "Ban Logger: " + p.getName() + " was just IP banned " + length.toLowerCase(Locale.ROOT) + "!");
                    if(DataHandler.getBoolean(player, "notifs")) {
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1f);
                    }
                }
            }
        }
        if(plugin.getConfig().getBoolean("webhook.banLogger.use")) {
            StringBuilder nameBuilder = new StringBuilder();
            StringBuilder iconBuilder = new StringBuilder();
            if(plugin.getConfig().getBoolean("webhook.customName")) {
                nameBuilder.append(plugin.getConfig().getString("webhook.name"));
                iconBuilder.append(plugin.getConfig().getString("webhook.icon"));
            }else{
                nameBuilder.append("Admincore");
                iconBuilder.append("https://raw.githubusercontent.com/RudRecciah/Admin-Core/main/icons/logo.png");
            }
            String name = nameBuilder.toString();
            String icon = iconBuilder.toString();
            TemmieWebhook webhook = new TemmieWebhook(plugin.getConfig().getString("webhook.banLogger.token"));
            DiscordEmbed de = new DiscordEmbed();
            ThumbnailEmbed te = new ThumbnailEmbed();
            te.setUrl("https://raw.githubusercontent.com/RudRecciah/Admin-Core/main/icons/ban.png");
            te.setHeight(96);
            te.setWidth(96);
            de.setThumbnail(te);
            de.setTitle("New Ban!");
            de.setDescription("A player has been banned.");
            de.setFields(Arrays.asList(FieldEmbed.builder().name("Type:").value(type).build(), FieldEmbed.builder().name("Player:").value(p.getName()).build(), FieldEmbed.builder().name("IP:").value(PlayerDataHandler.getIP(p.getUniqueId())).build(), FieldEmbed.builder().name("Reason:").value(reason).build(), FieldEmbed.builder().name("Length:").value(length).build(), FieldEmbed.builder().name("Banned By:").value(source).build()));
            de.setFooter(FooterEmbed.builder().text("Admincore Ban Logger").icon_url("https://raw.githubusercontent.com/RudRecciah/Admin-Core/main/icons/logo.png").build());
            DiscordMessage dm = new DiscordMessage(name, "", icon);
            dm.getEmbeds().add(de);
            webhook.sendMessage(dm);
        }
    }
}
