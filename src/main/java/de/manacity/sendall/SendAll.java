package de.manacity.sendall;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class SendAll extends Plugin {

    private int batchSize = 5;
    private long delayMs = 1000;
    private boolean skipIfAlreadyThere = true;

    @Override
    public void onEnable() {
        loadConfig();

        getProxy().getPluginManager().registerCommand(this,
                new Command("sendall", "manacity.sendall", "sendtoall") {
                    @Override
                    public void execute(CommandSender sender, String[] args) {
                        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                            loadConfig();
                            sender.sendMessage("§a[SendAll] Config neu geladen!");
                            return;
                        }

                        if (args.length < 2) {
                            sender.sendMessage("§cBenutzung: /sendall <vonServer> <zuServer>");
                            sender.sendMessage("§7Oder: /sendall reload");
                            return;
                        }

                        String from = args[0];
                        String to = args[1];

                        var fromServer = ProxyServer.getInstance().getServerInfo(from);
                        var toServer = ProxyServer.getInstance().getServerInfo(to);

                        if (fromServer == null) {
                            sender.sendMessage("§cServer '" + from + "' nicht gefunden!");
                            return;
                        }
                        if (toServer == null) {
                            sender.sendMessage("§cServer '" + to + "' nicht gefunden!");
                            return;
                        }

                        // Alle Spieler vom Source-Server holen
                        List<ProxiedPlayer> players = new ArrayList<>();
                        for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
                            if (p.getServer() != null &&
                                p.getServer().getInfo().getName().equalsIgnoreCase(from)) {
                                if (skipIfAlreadyThere &&
                                    to.equalsIgnoreCase(p.getServer().getInfo().getName()))
                                    continue;
                                players.add(p);
                            }
                        }

                        if (players.isEmpty()) {
                            sender.sendMessage("§eKeine Spieler auf §b" + from + " §ezu verschieben.");
                            return;
                        }

                        sender.sendMessage("§aVerschiebe §e" + players.size() +
                                "§a Spieler von §b" + from + " §anach §b" + to +
                                "§a (Batch=" + batchSize + ", Delay=" + delayMs + "ms)…");

                        Iterator<ProxiedPlayer> it = players.iterator();

                        AtomicReference<ScheduledTask> ref = new AtomicReference<>();
                        ScheduledTask task = ProxyServer.getInstance().getScheduler().schedule(
                                SendAll.this,
                                () -> {
                                    int sent = 0;
                                    while (sent < batchSize && it.hasNext()) {
                                        ProxiedPlayer p = it.next();
                                        p.connect(toServer);
                                        sent++;
                                    }
                                    if (!it.hasNext()) {
                                        ScheduledTask t = ref.get();
                                        if (t != null) t.cancel();
                                        sender.sendMessage("§a[SendAll] Alle Spieler wurden erfolgreich verschoben!");
                                    }
                                },
                                0L,
                                Math.max(1L, delayMs),
                                TimeUnit.MILLISECONDS
                        );
                        ref.set(task);
                    }
                });

        getLogger().info("SendAll aktiviert! Batch=" + batchSize + ", Delay=" + delayMs + "ms");
    }

    private void loadConfig() {
        try {
            File folder = getDataFolder();
            if (!folder.exists()) folder.mkdirs();

            File file = new File(folder, "config.yml");
            if (!file.exists()) {
                String def = """
                        # Anzahl Spieler pro Schub
                        batch-size: 5

                        # Zeit zwischen Schüben (Millisekunden)
                        delay-ms: 1000

                        # Spieler, die schon auf dem Zielserver sind, überspringen
                        skip-if-already-there: true
                        """;
                Files.writeString(file.toPath(), def);
            }

            Yaml yaml = new Yaml();
            Map<String, Object> cfg = yaml.load(Files.newInputStream(file.toPath()));
            batchSize = ((Number) cfg.getOrDefault("batch-size", 5)).intValue();
            delayMs = ((Number) cfg.getOrDefault("delay-ms", 1000L)).longValue();
            skipIfAlreadyThere = (Boolean) cfg.getOrDefault("skip-if-already-there", true);

            getLogger().info("Config geladen! Batch=" + batchSize + ", Delay=" + delayMs + "ms");

        } catch (IOException e) {
            getLogger().warning("Config konnte nicht geladen werden: " + e.getMessage());
        }
    }
}
