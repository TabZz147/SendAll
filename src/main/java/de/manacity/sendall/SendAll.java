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

    private int batchSize = 5;             // Spieler pro Schub
    private long delayMs = 1000;           // Zeit zwischen Schüben (Millisekunden)
    private boolean skipIfAlreadyThere = true; // Spieler auf Zielserver überspringen

    @Override
    public void onEnable() {
        loadConfig();

        // /sendall <server>  |  /sendall reload
        getProxy().getPluginManager().registerCommand(this,
                new Command("sendall", "manacity.sendall", "sendtoall") {
                    @Override
                    public void execute(CommandSender sender, String[] args) {
                        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                            loadConfig();
                            sender.sendMessage("§a[SendAll] Config neu geladen!");
                            return;
                        }

                        if (args.length < 1) {
                            sender.sendMessage("§cBenutzung: /sendall <server>  oder  /sendall reload");
                            return;
                        }

                        String target = args[0];
                        var server = ProxyServer.getInstance().getServerInfo(target);
                        if (server == null) {
                            sender.sendMessage("§cServer '" + target + "' nicht gefunden!");
                            return;
                        }

                        List<ProxiedPlayer> players = new ArrayList<>(ProxyServer.getInstance().getPlayers());
                        if (skipIfAlreadyThere) {
                            players.removeIf(p -> p.getServer() != null
                                    && p.getServer().getInfo().getName().equalsIgnoreCase(target));
                        }

                        if (players.isEmpty()) {
                            sender.sendMessage("§eKeine Spieler zu verschieben.");
                            return;
                        }

                        sender.sendMessage("§aSende §e" + players.size() + "§a Spieler nach §b" + target
                                + "§a (Batch=" + batchSize + ", Delay=" + delayMs + "ms)…");

                        Iterator<ProxiedPlayer> it = players.iterator();

                        // ScheduledTask-Referenz speichern, damit wir korrekt canceln können
                        AtomicReference<ScheduledTask> ref = new AtomicReference<>();
                        ScheduledTask task = ProxyServer.getInstance().getScheduler().schedule(
                                SendAll.this,
                                () -> {
                                    int sent = 0;
                                    while (sent < batchSize && it.hasNext()) {
                                        ProxiedPlayer p = it.next();
                                        p.connect(server);
                                        sent++;
                                    }
                                    if (!it.hasNext()) {
                                        // Task sauber beenden
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
