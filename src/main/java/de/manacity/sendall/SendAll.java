package de.manacity.sendall;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SendAll extends Plugin {

    private Configuration config;
    private int batchSize;
    private long delayMs;
    private boolean skipIfAlreadyThere;

    @Override
    public void onEnable() {
        loadConfig();
        getProxy().getPluginManager().registerCommand(this, new SendPlayersCommand());
        getLogger().info("§a[SendPlayers] Plugin aktiviert!");
    }

    private void loadConfig() {
        try {
            File file = new File(getDataFolder(), "config.yml");
            if (!file.exists()) {
                getDataFolder().mkdirs();
                file.createNewFile();
                config = new Configuration();
                config.set("batch-size", 5);
                config.set("delay-ms", 1000);
                config.set("skip-if-already-there", true);
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
            }
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
            batchSize = config.getInt("batch-size", 5);
            delayMs = config.getLong("delay-ms", 1000);
            skipIfAlreadyThere = config.getBoolean("skip-if-already-there", true);
            getLogger().info("Config geladen! Batch=" + batchSize + ", Delay=" + delayMs + "ms");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class SendPlayersCommand extends Command {
        public SendPlayersCommand() {
            super("sendplayers", "manacity.sendall", "sp", "sendp");
        }

        @Override
        public void execute(CommandSender sender, String[] args) {
            if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                loadConfig();
                sender.sendMessage(new TextComponent("§a[SendPlayers] Config neu geladen."));
                return;
            }

            if (args.length != 2) {
                sender.sendMessage(new TextComponent("§cBenutzung: /sendplayers <fromServer> <toServer>"));
                return;
            }

            String fromServerName = args[0];
            String toServerName = args[1];

            ServerInfo fromServer = ProxyServer.getInstance().getServerInfo(fromServerName);
            ServerInfo toServer = ProxyServer.getInstance().getServerInfo(toServerName);

            if (fromServer == null) {
                sender.sendMessage(new TextComponent("§cServer '" + fromServerName + "' nicht gefunden."));
                return;
            }
            if (toServer == null) {
                sender.sendMessage(new TextComponent("§cZielserver '" + toServerName + "' nicht gefunden."));
                return;
            }

            List<ProxiedPlayer> playersToSend = new ArrayList<>();
            for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                if (player.getServer() != null && player.getServer().getInfo().getName().equalsIgnoreCase(fromServerName)) {
                    if (skipIfAlreadyThere && player.getServer().getInfo().equals(toServer)) continue;
                    playersToSend.add(player);
                }
            }

            if (playersToSend.isEmpty()) {
                sender.sendMessage(new TextComponent("§eKeine Spieler auf Server '" + fromServerName + "' gefunden."));
                return;
            }

            sender.sendMessage(new TextComponent("§a[SendPlayers] Verschiebe §e" + playersToSend.size() + " §aSpieler von §e" + fromServerName + " §anach §e" + toServerName + "§a..."));

            ProxyServer.getInstance().getScheduler().runAsync(SendAll.this, () -> {
                int total = playersToSend.size();
                int sent = 0;
                while (sent < total) {
                    int end = Math.min(sent + batchSize, total);
                    List<ProxiedPlayer> batch = playersToSend.subList(sent, end);
                    for (ProxiedPlayer p : batch) {
                        p.connect(toServer);
                    }
                    sent = end;
                    try {
                        Thread.sleep(delayMs);
                    } catch (InterruptedException ignored) {
                    }
                }
                sender.sendMessage(new TextComponent("§a[SendPlayers] Alle Spieler erfolgreich verschoben!"));
            });
        }
    }
}
