package com.freezeui.iproductionsmc.thread;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

public class KeepInventoryOpenRunnable extends BukkitRunnable {

    @Getter
    private Inventory inventory;
    @Getter
    private Player player;

    public KeepInventoryOpenRunnable(Player player, Inventory inventory) {
        this.inventory = inventory;
        this.player = player;
    }

    @Override
    public void run() {

        if (player.getOpenInventory() == null || !player.getOpenInventory().getTitle().equalsIgnoreCase(inventory.getTitle())) {
            player.openInventory(inventory);
        }

    }
}
