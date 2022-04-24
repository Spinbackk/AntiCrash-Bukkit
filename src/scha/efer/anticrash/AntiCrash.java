package scha.efer.anticrash;

import org.bukkit.plugin.java.*;
import org.bukkit.*;
import org.bukkit.plugin.*;
import org.bukkit.command.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import java.util.*;
import org.bukkit.event.inventory.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.event.block.*;

public class AntiCrash extends JavaPlugin implements Listener
{
    PluginManager pm;
    String Prefix;
    String sign;
    String cmdcrash;
    String illegal;
    String chat;
    String inv;
    String reload;
    String noperm;

    public AntiCrash() {
        this.pm = Bukkit.getPluginManager();
        this.Prefix = this.getConfig().getString("Messages.Prefix").replace('&', '§');
        this.sign = this.getConfig().getString("Messages.Sign-Crash").replace('&', '§');
        this.cmdcrash = this.getConfig().getString("Messages.Crash-With-Command").replace('&', '§');
        this.illegal = this.getConfig().getString("Messages.Illegal-Items").replace('&', '§');
        this.chat = this.getConfig().getString("Messages.Chat-Crash").replace('&', '§');
        this.inv = this.getConfig().getString("Messages.Inventory-Crash").replace('&', '§');
        this.reload = this.getConfig().getString("Messages.Reload-Config").replace('&', '§');
        this.noperm = this.getConfig().getString("Messages.No-Permission").replace('&', '§');
    }

    public void onEnable() {
        this.pm.registerEvents((Listener)this, (Plugin)this);
        Bukkit.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this);
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
    }

    public boolean onCommand(final CommandSender sender, final Command cmd, final String l, final String[] args) {
        final Player p = (Player)sender;
        if (cmd.getName().equalsIgnoreCase("anticrash")) {
            if (args.length == 0) {
                p.sendMessage("");
                p.sendMessage("§b§lAnti Crash");
                p.sendMessage("§f /anticrash reload §7- §3Reload config.");
                p.sendMessage("");
            }
            else if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                if (p.hasPermission("anticrash.reload")) {
                    this.saveConfig();
                    this.reloadConfig();
                    p.sendMessage(String.valueOf(this.Prefix) + this.reload);
                }
                else {
                    p.sendMessage(String.valueOf(this.Prefix) + this.noperm);
                }
            }
        }
        return false;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onchat(final AsyncPlayerChatEvent olay) {
        if (olay.getPlayer() instanceof Player) {
            final Player oyuncu = olay.getPlayer();
            if (olay.getMessage().contains("I\u0307\ufeff")) {
                olay.setCancelled(true);
                oyuncu.sendMessage(String.valueOf(this.Prefix) + this.chat);
            }
            if (olay.getMessage().contains("i\u0307\ufeff\ufeff")) {
                olay.setCancelled(true);
                oyuncu.sendMessage(String.valueOf(this.Prefix) + this.chat);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void oncommand(final PlayerCommandPreprocessEvent olay) {
        if (olay.getPlayer() instanceof Player) {
            final Player oyuncu = olay.getPlayer();
            if (olay.getMessage().contains("I\u0307\ufeff")) {
                olay.setCancelled(true);
                oyuncu.sendMessage(String.valueOf(this.Prefix) + this.cmdcrash);
            }
            if (olay.getMessage().contains("i\u0307\ufeff\ufeff")) {
                olay.setCancelled(true);
                oyuncu.sendMessage(String.valueOf(this.Prefix) + this.cmdcrash);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onmove(final PlayerMoveEvent olay) {
        try {
            final Player oyuncu = olay.getPlayer();
            if (olay.getPlayer() instanceof Player && oyuncu.getItemInHand() != null) {
                final String itemadi = oyuncu.getItemInHand().getItemMeta().getDisplayName();
                if (itemadi != null && itemadi.contains("I\u0307\ufeff")) {
                    oyuncu.getInventory().setItemInHand((ItemStack)null);
                    oyuncu.sendMessage(String.valueOf(this.Prefix) + this.illegal);
                }
                final List<String> lore = (List<String>)oyuncu.getItemInHand().getItemMeta().getLore();
                if (lore != null && lore.contains("I\u0307\ufeff")) {
                    oyuncu.getInventory().setItemInHand((ItemStack)null);
                    oyuncu.sendMessage(String.valueOf(this.Prefix) + this.illegal);
                }
            }
        }
        catch (Exception ex) {}
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onclick(final InventoryClickEvent e) {
        if (!e.isCancelled()) {
            final HumanEntity ent = e.getWhoClicked();
            if (ent instanceof Player) {
                final Player player = (Player)ent;
                final Inventory inv = e.getInventory();
                if (inv instanceof AnvilInventory) {
                    final InventoryView view = e.getView();
                    final int rawSlot = e.getRawSlot();
                    if (rawSlot == view.convertSlot(rawSlot) && rawSlot == 2) {
                        final ItemStack item = e.getCurrentItem();
                        if (item != null) {
                            final ItemMeta meta = item.getItemMeta();
                            if (meta != null && meta.hasDisplayName()) {
                                final String displayName = meta.getDisplayName();
                                if (displayName.contains("I\u0307\ufeff")) {
                                    e.setCancelled(true);
                                    player.sendMessage(String.valueOf(this.Prefix) + inv);
                                }
                                if (displayName.contains("i\u0307\ufeff")) {
                                    e.setCancelled(true);
                                    player.sendMessage(String.valueOf(this.Prefix) + inv);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onsignchange(final SignChangeEvent e) {
        for (int i = 0; i < 4; ++i) {
            if (e.getLine(i).matches("^[a-zA-Z0-9_]*$")) {
                if (e.getLine(i).length() > 20) {
                    e.setCancelled(true);
                    e.getPlayer().sendMessage(String.valueOf(this.Prefix) + this.sign);
                }
            }
            else if (e.getLine(i).length() > 50) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(String.valueOf(this.Prefix) + this.sign);
            }
        }
    }
}
