package com.freezeui.iproductionsmc;

import com.freezeui.iproductionsmc.command.FreezeCommand;
import com.freezeui.iproductionsmc.handler.FreezeHandler;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class FreezeUI extends JavaPlugin {

    private static FreezeUI instance;

    @Getter
    private FreezeHandler freezeHandler;

    public static FreezeUI getInstance() {

        return instance;

    }

    public static boolean isFrozen(Player player) {

        return getInstance().getFreezeHandler().isFrozen(player);

    }

    @Override
    public void onEnable() {

        saveDefaultConfig();
        instance = this;
        freezeHandler = new FreezeHandler();

        Bukkit.getPluginManager().registerEvents(freezeHandler,this);
        getCommand("freeze").setExecutor(new FreezeCommand());

    }

    @Override
    public void onDisable() {


    }


}
