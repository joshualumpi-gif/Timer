
package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.example.events.TimerFinishEvent;
import org.example.events.TimerStartEvent;

public class TimerPlugin extends JavaPlugin implements Listener, CommandExecutor, TimerAPI {
    private static TimerPlugin instance;
    private int timeSeconds = 0;
    private int tickCounter = 0;
    private boolean isRunning = false;
    private boolean countUp = true;
    private float waveTime = 0.0F;
    private float wavePhase = 0.0F;
    private final Map<UUID, String> playerBaseColor = new HashMap();
    private final Map<String, String> colorMap = new LinkedHashMap();
    private final MiniMessage mm = MiniMessage.miniMessage();
    private TimerGUI gui;

    public static TimerPlugin getInstance() {
        return instance;
    }

    public static TimerAPI getApi() {
        return instance;
    }

    public void onEnable() {
        instance = this;
        this.getConfig().addDefault("timer.time", 0);
        this.getConfig().addDefault("timer.mode", true);
        this.getConfig().addDefault("timer.running", false);
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
        this.timeSeconds = this.getConfig().getInt("timer.time");
        this.countUp = this.getConfig().getBoolean("timer.mode");
        boolean configRunning = this.getConfig().getBoolean("timer.running");
        if (Bukkit.getOnlinePlayers().isEmpty()) {
            this.isRunning = false;
        } else {
            this.isRunning = configRunning;
        }

        if (this.getConfig().contains("colors")) {
            for(String key : this.getConfig().getConfigurationSection("colors").getKeys(false)) {
                try {
                    this.playerBaseColor.put(UUID.fromString(key), this.getConfig().getString("colors." + key));
                } catch (Exception var5) {
                }
            }
        }

        this.initColors();
        this.gui = new TimerGUI(this);
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(this.gui, this);
        if (this.getCommand("timer") != null) {
            this.getCommand("timer").setExecutor(this);
        }

        for(World world : Bukkit.getWorlds()) {
            world.setGameRule(GameRule.KEEP_INVENTORY, true);
        }

        (new BukkitRunnable() {
            public void run() {
                TimerPlugin.this.saveAllData();
            }
        }).runTaskTimer(this, 100L, 100L);
        (new BukkitRunnable() {
            public void run() {
                if (TimerPlugin.this.isRunning) {
                    ++TimerPlugin.this.tickCounter;
                    if (TimerPlugin.this.tickCounter >= 20) {
                        if (TimerPlugin.this.countUp) {
                            ++TimerPlugin.this.timeSeconds;
                        } else if (TimerPlugin.this.timeSeconds > 0) {
                            --TimerPlugin.this.timeSeconds;
                            if (TimerPlugin.this.timeSeconds == 0) {
                                TimerPlugin.this.finishTimer();
                            }
                        }

                        TimerPlugin.this.tickCounter = 0;
                    }
                }

                TimerPlugin var6 = TimerPlugin.this;
                var6.waveTime += 0.05F;
                TimerPlugin.this.wavePhase = (float)((Math.sin((double)TimerPlugin.this.waveTime) + (double)1.0F) / (double)2.0F);
                TimerPlugin.this.updateActionBar();
            }
        }).runTaskTimer(this, 0L, 1L);
    }

    public void onDisable() {
        this.saveAllData();
    }

    public void setTime(int seconds) {
        this.timeSeconds = seconds;
    }

    public int getTime() {
        return this.timeSeconds;
    }

    public void setRunning(boolean running) {
        if (running && !this.isRunning) {
            TimerStartEvent event = new TimerStartEvent();
            Bukkit.getPluginManager().callEvent(event);
        }
        else if (!running && this.isRunning) {
            org.example.events.TimerPauseEvent event = new org.example.events.TimerPauseEvent();
            Bukkit.getPluginManager().callEvent(event);
        }

        this.isRunning = running;
    }

    public boolean isRunning() {
        return this.isRunning;
    }

    public void setCountUp(boolean countUp) {
        this.countUp = countUp;
    }

    public boolean isCountUp() {
        return this.countUp;
    }

    public void reset() {
        this.isRunning = false;
        this.timeSeconds = 0;
        this.countUp = true;
    }

    public String getFormattedTime(int seconds) {
        return this.formatTime(seconds);
    }

    public List<String> getColorNames() {
        return new ArrayList(this.colorMap.keySet());
    }

    public String getPlayerColorName(Player p) {
        String hex = (String)this.playerBaseColor.getOrDefault(p.getUniqueId(), "#ff0000");

        for(Map.Entry<String, String> entry : this.colorMap.entrySet()) {
            if (((String)entry.getValue()).equals(hex)) {
                return (String)entry.getKey();
            }
        }

        return "red";
    }

    public void setPlayerColor(Player p, String colorName) {
        if (this.colorMap.containsKey(colorName)) {
            this.playerBaseColor.put(p.getUniqueId(), (String)this.colorMap.get(colorName));
        }

    }

    private void saveAllData() {
        this.getConfig().set("timer.time", this.timeSeconds);
        this.getConfig().set("timer.mode", this.countUp);
        this.getConfig().set("timer.running", this.isRunning);

        for(Map.Entry<UUID, String> entry : this.playerBaseColor.entrySet()) {
            this.getConfig().set("colors." + ((UUID)entry.getKey()).toString(), entry.getValue());
        }

        this.saveConfig();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (Bukkit.getOnlinePlayers().size() - 1 <= 0 && this.isRunning) {
            this.isRunning = false;
            this.saveAllData();
            this.getLogger().info("Timer paused automatically (Last player left).");
        }

    }

    private void finishTimer() {
        this.isRunning = false;
        this.saveAllData();
        TimerFinishEvent event = new TimerFinishEvent();
        Bukkit.getPluginManager().callEvent(event);
    }

    private void initColors() {
        this.colorMap.put("red", "#ff0000");
        this.colorMap.put("blue", "#0000ff");
        this.colorMap.put("green", "#00ff00");
        this.colorMap.put("yellow", "#ffff00");
        this.colorMap.put("gold", "#ffaa00");
        this.colorMap.put("aqua", "#00ffff");
        this.colorMap.put("pink", "#ff00ff");
        this.colorMap.put("purple", "#800080");
        this.colorMap.put("white", "#ffffff");
        this.colorMap.put("gray", "#aaaaaa");
        this.colorMap.put("orange", "#ff6600");
        this.colorMap.put("cyan", "#40e0d0");
        this.colorMap.put("neon", "#39ff14");
        this.colorMap.put("magenta", "#ff0090");
        this.colorMap.put("black", "#000000");
    }

    private void updateActionBar() {
        String timeText = this.formatTime(this.timeSeconds);

        for(Player player : Bukkit.getOnlinePlayers()) {
            String baseHex = (String)this.playerBaseColor.getOrDefault(player.getUniqueId(), "#ff0000");
            String darkerHex = this.darkenColor(baseHex, 0.4);
            String gradientString = String.format(Locale.US, "<gradient:%s:%s:%s:%.3f><b>%s</b></gradient>", baseHex, darkerHex, baseHex, this.wavePhase, timeText);
            player.sendActionBar(this.mm.deserialize(gradientString));
        }

    }

    private String darkenColor(String hex, double factor) {
        try {
            if (hex.startsWith("#")) {
                hex = hex.substring(1);
            }

            int rgb = Integer.parseInt(hex, 16);
            int r = rgb >> 16 & 255;
            int g = rgb >> 8 & 255;
            int b = rgb & 255;
            r = (int)((double)r * ((double)1.0F - factor));
            g = (int)((double)g * ((double)1.0F - factor));
            b = (int)((double)b * ((double)1.0F - factor));
            return String.format("#%02x%02x%02x", r, g, b);
        } catch (Exception var8) {
            return "#ffffff";
        }
    }

    private String formatTime(int seconds) {
        long w = (long)(seconds / 604800);
        long rem = (long)(seconds % 604800);
        long d = rem / 86400L;
        rem %= 86400L;
        long h = rem / 3600L;
        long m = rem % 3600L / 60L;
        long s = rem % 60L;
        StringBuilder sb = new StringBuilder();
        if (w > 0L) {
            sb.append(w).append("w ");
        }

        if (d > 0L) {
            sb.append(d).append("d ");
        }

        if (h > 0L) {
            sb.append(h).append("h ");
        }

        if (m > 0L) {
            sb.append(m).append("m ");
        }

        sb.append(s).append("s");
        return sb.toString().trim();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("timer.use")) {
            return true;
        } else if (!(sender instanceof Player) || args.length != 0 && !args[0].equalsIgnoreCase("config") && !args[0].equalsIgnoreCase("gui")) {
            if (args.length > 0) {
                switch (args[0].toLowerCase()) {
                    case "start":
                    case "resume":
                        this.setRunning(true);
                        break;
                    case "pause":
                    case "stop":
                        this.setRunning(false);
                        break;
                    case "reset":
                        this.reset();
                }
            }

            return true;
        } else {
            this.gui.openGUI((Player)sender);
            return true;
        }
    }
}
