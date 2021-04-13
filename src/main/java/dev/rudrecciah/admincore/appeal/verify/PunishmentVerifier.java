package dev.rudrecciah.admincore.appeal.verify;

import dev.rudrecciah.admincore.data.DataHandler;
import dev.rudrecciah.admincore.playerdata.PlayerDataHandler;
import org.bukkit.BanList;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;
import java.util.UUID;

import static dev.rudrecciah.admincore.Main.plugin;

import static dev.rudrecciah.admincore.Main.plugin;

public class PunishmentVerifier implements Runnable {

    private boolean run = true;

    @Override
    public void run() {
        while(run) {
            verify();
        }
    }

    private void verify() {
        File dir = new File(plugin.getDataFolder() + File.separator + "data" + File.separator + "ad");
        if(!dir.exists()) {
            dir.mkdirs();
        }
        String[] files = dir.list();
        for(String fileName : files) {
            File file = new File(plugin.getDataFolder() + File.separator + "data" + File.separator + "ad", fileName);
            if(file.exists()) {
                FileConfiguration yaml = YamlConfiguration.loadConfiguration(file);
                yaml.options().copyDefaults(true);
                String type = yaml.getString("type");
                if(type != null && yaml.getString("ip") != null && yaml.getString("uuid") != null) {
                    if(type.equalsIgnoreCase("IP_BAN")) {
                        if(plugin.getServer().getBanList(BanList.Type.IP).isBanned(yaml.getString("ip"))) {
                            return;
                        }
                        file.delete();
                    }
                    if(type.equalsIgnoreCase("BAN")) {
                        if(plugin.getServer().getBanList(BanList.Type.NAME).isBanned(yaml.getString("uuid"))) {
                            return;
                        }
                        file.delete();
                    }
                    if(type.equalsIgnoreCase("MUTE")) {
                        if(PlayerDataHandler.muteExpired(UUID.fromString(yaml.getString("uuid")))) {
                            return;
                        }
                        file.delete();
                    }
                }
                file.delete();
            }
        }
    }

    public void end() {
        run = false;
    }

}