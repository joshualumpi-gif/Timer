

package org.example;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class TimerGUI implements Listener {
    private final TimerPlugin plugin;
    private final Component TITLE_MAIN;
    private final Component TITLE_TIME;
    private final Component TITLE_COLOR;
    private final String TEX_ARROW_UP;
    private final String TEX_ARROW_DOWN;
    private final String TEX_PLAY;
    private final String TEX_PAUSE;
    private final String TEX_COLOR;
    private final String TEX_WEEK;
    private final String TEX_DAY;
    private final String TEX_HOUR;
    private final String TEX_MINUTE;
    private final String TEX_SECOND;

    public TimerGUI(TimerPlugin plugin) {
        this.TITLE_MAIN = Component.text("Timer Menu", NamedTextColor.DARK_GRAY);
        this.TITLE_TIME = Component.text("Time Settings", NamedTextColor.DARK_GRAY);
        this.TITLE_COLOR = Component.text("Select Color", NamedTextColor.DARK_GRAY);
        this.TEX_ARROW_UP = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTk5YWFmMjQ1NmE2MTIyZGU4ZjZiNjI2ODNmMmJjMmVlZDlhYmI4MWZkNWJlYTFiNGMyM2E1ODE1NmI2NjkifX19";
        this.TEX_ARROW_DOWN = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzkxMmQ0NWIxYzc4Y2MyMjQ1MjcyM2VlNjZiYTJkMTU3NzdjYzI4ODU2OGQ2YzFiNjJhNTQ1YjI5YzcxODcifX19";
        this.TEX_PLAY = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTdiMDNiNzFkM2Y4NjIyMGVmMTIyZjk4MzFhNzI2ZWIyYjI4MzMxOWM3YjYyZTdkY2QyZDY0ZDk2ODIifX19";
        this.TEX_PAUSE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzAzMDgyZjAzM2Y5NzI0Y2IyMmZlMjdkMGRlNDk3NTA5MDMzNTY0MWVlZTVkOGQ5MjdhZGY1YThiNjdmIn19fQ==";
        this.TEX_COLOR = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzdmZjEzNzc3NTQ1NjNhYjQxYjhhMDMwNWRhYzAzZGU2M2UwMmU1YTM5YTY5NTZhZmQ2Y2NhYmYyOTVhOTZkOCJ9fX0=";
        this.TEX_WEEK = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjk1YTIzZWYyNTQyZjlkZDFkMTFjZmRhZTYxOGUxYWYxZGQ4MmE5YjNmN2EyNjdhYjI4OGJlMzI5NTQ5MmFjIn19fQ==";
        this.TEX_DAY = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGE2YTdkMWI3NjI5MTkzOTQyZjhlMTQ2YzZkMWQyZGIxOTFkMjdmODExZDIxZTI5YTJlNGNmYmFiZGEwODgifX19";
        this.TEX_HOUR = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYThkMGM2N2JhYTcyNWExZTQxYmFiMzA4MDU3NzdlNzMyYmIyYWU2ZTkzNGY4NzM1Y2M0MDc4NzVkNjRjY2IifX19";
        this.TEX_MINUTE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2Y4MzM0MTUxYzIzNGY0MTY0NzExM2JlM2VhZGYyODdkMTgxNzExNWJhYzk0NDVmZmJiYmU5Y2IyYjI4NGIwIn19fQ==";
        this.TEX_SECOND = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjM4ZDI3NTk1NjlkNTE1ZDI0NTRkNGE3ODkxYTk0Y2M2M2RkZmU3MmQwM2JmZGY3NmYxZDQyNzdkNTkwIn19fQ==";
        this.plugin = plugin;
    }

    public void openGUI(Player player) {
        Inventory inv = Bukkit.createInventory((InventoryHolder)null, 45, this.TITLE_MAIN);
        String timeStr = this.plugin.getFormattedTime(this.plugin.getTime());
        inv.setItem(4, this.createItem(Material.CLOCK, "§e§lTIME: §f" + timeStr, "§7Click to Reset"));
        String modeName = this.plugin.isCountUp() ? "§bMode: §fCount Up" : "§bMode: §fCountdown";
        String modeTex = this.plugin.isCountUp() ? "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTk5YWFmMjQ1NmE2MTIyZGU4ZjZiNjI2ODNmMmJjMmVlZDlhYmI4MWZkNWJlYTFiNGMyM2E1ODE1NmI2NjkifX19" : "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzkxMmQ0NWIxYzc4Y2MyMjQ1MjcyM2VlNjZiYTJkMTU3NzdjYzI4ODU2OGQ2YzFiNjJhNTQ1YjI5YzcxODcifX19";
        inv.setItem(20, this.createSkull(modeTex, modeName, "§7Click to toggle mode"));
        inv.setItem(22, this.createSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjM4ZDI3NTk1NjlkNTE1ZDI0NTRkNGE3ODkxYTk0Y2M2M2RkZmU3MmQwM2JmZGY3NmYxZDQyNzdkNTkwIn19fQ==", "§6Timer Settings", "§7Click to adjust time"));
        inv.setItem(24, this.createSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzdmZjEzNzc3NTQ1NjNhYjQxYjhhMDMwNWRhYzAzZGU2M2UwMmU1YTM5YTY5NTZhZmQ2Y2NhYmYyOTVhOTZkOCJ9fX0=", "§dDesign Color", "§7Click to choose color"));
        boolean isRunning = this.plugin.isRunning();
        String statusText = isRunning ? "§a§lRUNNING" : "§c§lPAUSED";
        String statusLore = isRunning ? "§7Click to §cpause" : "§7Click to §astart §7(closes menu)";
        String statusTex = isRunning ? "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzAzMDgyZjAzM2Y5NzI0Y2IyMmZlMjdkMGRlNDk3NTA5MDMzNTY0MWVlZTVkOGQ5MjdhZGY1YThiNjdmIn19fQ==" : "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTdiMDNiNzFkM2Y4NjIyMGVmMTIyZjk4MzFhNzI2ZWIyYjI4MzMxOWM3YjYyZTdkY2QyZDY0ZDk2ODIifX19";
        inv.setItem(40, this.createSkull(statusTex, statusText, statusLore));
        this.fillGlass(inv);
        player.openInventory(inv);
    }

    public void openTimeGUI(Player player) {
        Inventory inv = Bukkit.createInventory((InventoryHolder)null, 54, this.TITLE_TIME);
        String timeStr = this.plugin.getFormattedTime(this.plugin.getTime());
        inv.setItem(4, this.createItem(Material.CLOCK, "§eCurrent: §f" + timeStr));
        int totalSeconds = this.plugin.getTime();
        long w = (long)(totalSeconds / 604800);
        long rem = (long)(totalSeconds % 604800);
        long d = rem / 86400L;
        rem %= 86400L;
        long h = rem / 3600L;
        rem %= 3600L;
        long m = rem / 60L;
        long s = rem % 60L;
        inv.setItem(9, this.createSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTk5YWFmMjQ1NmE2MTIyZGU4ZjZiNjI2ODNmMmJjMmVlZDlhYmI4MWZkNWJlYTFiNGMyM2E1ODE1NmI2NjkifX19", "§a+1 Week §7(§e" + w + "§7)", "§7Shift: +10"));
        inv.setItem(18, this.createSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjk1YTIzZWYyNTQyZjlkZDFkMTFjZmRhZTYxOGUxYWYxZGQ4MmE5YjNmN2EyNjdhYjI4OGJlMzI5NTQ5MmFjIn19fQ==", "§6Weeks"));
        inv.setItem(27, this.createSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzkxMmQ0NWIxYzc4Y2MyMjQ1MjcyM2VlNjZiYTJkMTU3NzdjYzI4ODU2OGQ2YzFiNjJhNTQ1YjI5YzcxODcifX19", "§c-1 Week §7(§e" + w + "§7)", "§7Shift: -10"));
        inv.setItem(11, this.createSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTk5YWFmMjQ1NmE2MTIyZGU4ZjZiNjI2ODNmMmJjMmVlZDlhYmI4MWZkNWJlYTFiNGMyM2E1ODE1NmI2NjkifX19", "§a+1 Day §7(§e" + d + "§7)", "§7Shift: +10"));
        inv.setItem(20, this.createSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGE2YTdkMWI3NjI5MTkzOTQyZjhlMTQ2YzZkMWQyZGIxOTFkMjdmODExZDIxZTI5YTJlNGNmYmFiZGEwODgifX19", "§bDays"));
        inv.setItem(29, this.createSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzkxMmQ0NWIxYzc4Y2MyMjQ1MjcyM2VlNjZiYTJkMTU3NzdjYzI4ODU2OGQ2YzFiNjJhNTQ1YjI5YzcxODcifX19", "§c-1 Day §7(§e" + d + "§7)", "§7Shift: -10"));
        inv.setItem(13, this.createSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTk5YWFmMjQ1NmE2MTIyZGU4ZjZiNjI2ODNmMmJjMmVlZDlhYmI4MWZkNWJlYTFiNGMyM2E1ODE1NmI2NjkifX19", "§a+1 Hour §7(§e" + h + "§7)", "§7Shift: +10"));
        inv.setItem(22, this.createSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYThkMGM2N2JhYTcyNWExZTQxYmFiMzA4MDU3NzdlNzMyYmIyYWU2ZTkzNGY4NzM1Y2M0MDc4NzVkNjRjY2IifX19", "§eHours"));
        inv.setItem(31, this.createSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzkxMmQ0NWIxYzc4Y2MyMjQ1MjcyM2VlNjZiYTJkMTU3NzdjYzI4ODU2OGQ2YzFiNjJhNTQ1YjI5YzcxODcifX19", "§c-1 Hour §7(§e" + h + "§7)", "§7Shift: -10"));
        inv.setItem(15, this.createSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTk5YWFmMjQ1NmE2MTIyZGU4ZjZiNjI2ODNmMmJjMmVlZDlhYmI4MWZkNWJlYTFiNGMyM2E1ODE1NmI2NjkifX19", "§a+1 Minute §7(§e" + m + "§7)", "§7Shift: +10"));
        inv.setItem(24, this.createSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2Y4MzM0MTUxYzIzNGY0MTY0NzExM2JlM2VhZGYyODdkMTgxNzExNWJhYzk0NDVmZmJiYmU5Y2IyYjI4NGIwIn19fQ==", "§aMinutes"));
        inv.setItem(33, this.createSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzkxMmQ0NWIxYzc4Y2MyMjQ1MjcyM2VlNjZiYTJkMTU3NzdjYzI4ODU2OGQ2YzFiNjJhNTQ1YjI5YzcxODcifX19", "§c-1 Minute §7(§e" + m + "§7)", "§7Shift: -10"));
        inv.setItem(17, this.createSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTk5YWFmMjQ1NmE2MTIyZGU4ZjZiNjI2ODNmMmJjMmVlZDlhYmI4MWZkNWJlYTFiNGMyM2E1ODE1NmI2NjkifX19", "§a+1 Second §7(§e" + s + "§7)", "§7Shift: +10"));
        inv.setItem(26, this.createSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjM4ZDI3NTk1NjlkNTE1ZDI0NTRkNGE3ODkxYTk0Y2M2M2RkZmU3MmQwM2JmZGY3NmYxZDQyNzdkNTkwIn19fQ==", "§dSeconds"));
        inv.setItem(35, this.createSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzkxMmQ0NWIxYzc4Y2MyMjQ1MjcyM2VlNjZiYTJkMTU3NzdjYzI4ODU2OGQ2YzFiNjJhNTQ1YjI5YzcxODcifX19", "§c-1 Second §7(§e" + s + "§7)", "§7Shift: -10"));
        inv.setItem(49, this.createItem(Material.BARRIER, "§cBack to Menu"));
        this.fillGlass(inv);
        player.openInventory(inv);
    }

    public void openColorGUI(Player player) {
        Inventory inv = Bukkit.createInventory((InventoryHolder)null, 36, this.TITLE_COLOR);
        List<String> colors = this.plugin.getColorNames();
        int slot = 0;

        for(String colorName : colors) {
            Material icon = this.getColorMaterial(colorName);
            boolean isSelected = this.plugin.getPlayerColorName(player).equalsIgnoreCase(colorName);
            String prefix = isSelected ? "§a§lSELECTED: §f" : "§7Select: §f";
            String var10000 = colorName.substring(0, 1).toUpperCase();
            String displayName = var10000 + colorName.substring(1);
            ItemStack item = this.createItem(icon, prefix + displayName);
            if (isSelected) {
                ItemMeta meta = item.getItemMeta();
                meta.addEnchant(Enchantment.UNBREAKING, 1, true);
                meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
                item.setItemMeta(meta);
            }

            inv.setItem(slot++, item);
        }

        inv.setItem(31, this.createItem(Material.BARRIER, "§cBack to Menu"));
        this.fillGlass(inv);
        player.openInventory(inv);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            Player p = (Player)e.getWhoClicked();
            Component title = e.getView().title();
            if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.GRAY_STAINED_GLASS_PANE) {
                if (title.equals(this.TITLE_MAIN)) {
                    e.setCancelled(true);
                    p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 0.5F, 1.0F);
                    switch (e.getSlot()) {
                        case 4:
                            this.plugin.reset();
                            this.openGUI(p);
                            break;
                        case 20:
                            this.plugin.setCountUp(!this.plugin.isCountUp());
                            this.openGUI(p);
                            break;
                        case 22:
                            this.openTimeGUI(p);
                            return;
                        case 24:
                            this.openColorGUI(p);
                            return;
                        case 40:
                            boolean newState = !this.plugin.isRunning();
                            this.plugin.setRunning(newState);
                            if (newState) {
                                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 2.0F);
                                Bukkit.getScheduler().runTask(this.plugin, () -> p.closeInventory());
                                return;
                            }

                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0F, 0.5F);
                            this.openGUI(p);
                    }
                } else if (title.equals(this.TITLE_TIME)) {
                    e.setCancelled(true);
                    p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 0.5F, 1.0F);
                    int slot = e.getSlot();
                    int multiplier = e.isShiftClick() ? 10 : 1;
                    if (slot == 49) {
                        this.openGUI(p);
                        return;
                    }

                    int change = 0;
                    if (slot == 9) {
                        change = 604800 * multiplier;
                    }

                    if (slot == 27) {
                        change = -604800 * multiplier;
                    }

                    if (slot == 11) {
                        change = 86400 * multiplier;
                    }

                    if (slot == 29) {
                        change = -86400 * multiplier;
                    }

                    if (slot == 13) {
                        change = 3600 * multiplier;
                    }

                    if (slot == 31) {
                        change = -3600 * multiplier;
                    }

                    if (slot == 15) {
                        change = 60 * multiplier;
                    }

                    if (slot == 33) {
                        change = -60 * multiplier;
                    }

                    if (slot == 17) {
                        change = 1 * multiplier;
                    }

                    if (slot == 35) {
                        change = -1 * multiplier;
                    }

                    if (change != 0) {
                        this.addTime(change);
                        this.openTimeGUI(p);
                    }
                } else if (title.equals(this.TITLE_COLOR)) {
                    e.setCancelled(true);
                    p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 0.5F, 1.0F);
                    if (e.getSlot() == 31) {
                        this.openGUI(p);
                        return;
                    }

                    List<String> colors = this.plugin.getColorNames();
                    int clickedSlot = e.getSlot();
                    if (clickedSlot >= 0 && clickedSlot < colors.size()) {
                        this.plugin.setPlayerColor(p, (String)colors.get(clickedSlot));
                        this.openColorGUI(p);
                    }
                }

            } else {
                if (title.equals(this.TITLE_MAIN) || title.equals(this.TITLE_TIME) || title.equals(this.TITLE_COLOR)) {
                    e.setCancelled(true);
                }

            }
        }
    }

    private void addTime(int seconds) {
        long newTime = (long)this.plugin.getTime() + (long)seconds;
        if (newTime < 0L) {
            newTime = 0L;
        }

        if (newTime > 2147483647L) {
            newTime = 2147483647L;
        }

        this.plugin.setTime((int)newTime);
    }

    private void fillGlass(Inventory inv) {
        ItemStack glass = this.createItem(Material.GRAY_STAINED_GLASS_PANE, "§7");

        for(int i = 0; i < inv.getSize(); ++i) {
            if (inv.getItem(i) == null) {
                inv.setItem(i, glass);
            }
        }

    }

    private ItemStack createItem(Material mat, String name, String... lore) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(LegacyComponentSerializer.legacySection().deserialize(name).decoration(TextDecoration.ITALIC, false));
        if (lore.length > 0) {
            List<Component> loreList = (List)Arrays.stream(lore).map((s) -> (TextComponent)LegacyComponentSerializer.legacySection().deserialize(s).decoration(TextDecoration.ITALIC, false)).collect(Collectors.toList());
            meta.lore(loreList);
        }

        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createSkull(String base64, String name, String... lore) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta)head.getItemMeta();
        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID(), (String)null);
        profile.setProperty(new ProfileProperty("textures", base64));
        meta.setPlayerProfile(profile);
        meta.displayName(LegacyComponentSerializer.legacySection().deserialize(name).decoration(TextDecoration.ITALIC, false));
        if (lore.length > 0) {
            List<Component> loreList = (List)Arrays.stream(lore).map((s) -> (TextComponent)LegacyComponentSerializer.legacySection().deserialize(s).decoration(TextDecoration.ITALIC, false)).collect(Collectors.toList());
            meta.lore(loreList);
        }

        head.setItemMeta(meta);
        return head;
    }

    private Material getColorMaterial(String colorName) {
        switch (colorName.toLowerCase()) {
            case "red" -> {
                return Material.RED_CONCRETE;
            }
            case "blue" -> {
                return Material.BLUE_CONCRETE;
            }
            case "green" -> {
                return Material.LIME_CONCRETE;
            }
            case "yellow" -> {
                return Material.YELLOW_CONCRETE;
            }
            case "gold" -> {
                return Material.ORANGE_TERRACOTTA;
            }
            case "aqua" -> {
                return Material.LIGHT_BLUE_CONCRETE;
            }
            case "pink" -> {
                return Material.PINK_CONCRETE;
            }
            case "purple" -> {
                return Material.PURPLE_CONCRETE;
            }
            case "white" -> {
                return Material.WHITE_CONCRETE;
            }
            case "gray" -> {
                return Material.GRAY_CONCRETE;
            }
            case "orange" -> {
                return Material.ORANGE_CONCRETE;
            }
            case "cyan" -> {
                return Material.CYAN_CONCRETE;
            }
            case "neon" -> {
                return Material.LIME_GLAZED_TERRACOTTA;
            }
            case "magenta" -> {
                return Material.MAGENTA_CONCRETE;
            }
            case "black" -> {
                return Material.BLACK_CONCRETE;
            }
            default -> {
                return Material.WHITE_WOOL;
            }
        }
    }
}
