package com.freezeui.iproductionsmc.handler;

import com.freezeui.iproductionsmc.FreezeUI;
import com.freezeui.iproductionsmc.thread.KeepInventoryOpenRunnable;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FreezeHandler implements Listener{

    @Getter
    private List<UUID> frozenUsers = new ArrayList<>();
    @Getter
    private Inventory freezeInventory;
    @Getter
    private KeepInventoryOpenRunnable keepInventoryOpenRunnable;

    public static String color(String message) {

        return ChatColor.translateAlternateColorCodes('&', message);

    }

    public static List<String> retrieveColoredStringList(List<String> s) {

        for (int i = 0; i < s.size(); i++) s.set(i, color(s.get(i)));

        return s;
    }

    public static List<String> retrievePlayerStringList(List<String> s, Player player) {

        for (int i = 0; i < s.size(); i++) s.set(i, color(s.get(i).replaceAll("%player%", player.getName())));

        return s;
    }

    public boolean isFrozen(Player player) {

        return isFrozenUUID(player.getUniqueId());

    }

    public boolean isFrozenUUID(UUID uuid) {

        return frozenUsers.contains(uuid);

    }

    public void freezePlayer(Player player) {

        if (isFrozen(player)) {

            if (freezeInventory != null)
                player.closeInventory();

            player.removePotionEffect(PotionEffectType.SLOW);

            player.removePotionEffect(PotionEffectType.BLINDNESS);

            player.removePotionEffect(PotionEffectType.JUMP);


            keepInventoryOpenRunnable.cancel();

            if (player.getOpenInventory().getTitle().equals(freezeInventory.getTitle())) player.closeInventory();

            frozenUsers.remove(player.getUniqueId());

            //unfreeze

        } else {

            freezeInventory = Bukkit.createInventory(null, 9, color(FreezeUI.getInstance().getConfig().getString("freeze.freeze-inventory-name").replaceAll("%player%", player.getName())));

            ItemStack freezeItem = new ItemStack(Material.ICE);
            ItemMeta freezeMeta = freezeItem.getItemMeta();

            freezeMeta.setDisplayName(color(FreezeUI.getInstance().getConfig().getString("freeze.freeze-inventory-item-name").replaceAll("%player%", player.getName())));
            freezeMeta.setLore(retrievePlayerStringList(FreezeUI.getInstance().getConfig().getStringList("freeze.freeze-inventory-item-lore"), player));

            freezeItem.setItemMeta(freezeMeta);

            for (int i = 0; i < freezeInventory.getSize(); i++) freezeInventory.setItem(i, freezeItem);

            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 128));

            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 128));

            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 128));

            player.openInventory(freezeInventory);

            keepInventoryOpenRunnable = new KeepInventoryOpenRunnable(player, freezeInventory);

            keepInventoryOpenRunnable.runTaskTimer(FreezeUI.getInstance(), 0, 20);

            frozenUsers.add(player.getUniqueId());
        }

    }
    @EventHandler
    public void attack(EntityDamageByEntityEvent e){

        if(isFrozenUUID(e.getDamager().getUniqueId()) || isFrozenUUID(e.getEntity().getUniqueId())) e.setCancelled(true);

    }
    @EventHandler
    public void drop(PlayerDropItemEvent e){

        if(isFrozen(e.getPlayer())) e.setCancelled(true);

    }
    @EventHandler
    public void destroy(BlockBreakEvent e){

     if(isFrozen(e.getPlayer())) e.setCancelled(true);

    }
    @EventHandler
    public void quit(PlayerQuitEvent e){

        if(isFrozen(e.getPlayer())) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), FreezeUI.getInstance().getConfig().getString("freeze.command-on-leave").replaceAll("%player%", e.getPlayer().getName()));
            freezePlayer(e.getPlayer());
        }
    }
    @EventHandler
    public void click(InventoryClickEvent e){
        if(isFrozenUUID(e.getWhoClicked().getUniqueId())) e.setCancelled(true);
    }



}
