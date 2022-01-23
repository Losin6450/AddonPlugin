package me.losin6450.plugin.addonplugin;

import me.losin6450.plugin.addonplugin.managers.AddonManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class AddonPlugin extends JavaPlugin {

    private static AddonPlugin instance;
    private AddonManager addonManager;

    @Override
    public void onEnable() {
        instance = this;
        addonManager = new AddonManager();
        addonManager.loadAddons();
        addonManager.enableAddons();
    }

    @Override
    public void onDisable() {
        addonManager.disableAddons();
    }

    public static AddonPlugin getInstance(){
        return instance;
    }

    public AddonManager getAddonManager() {
        return addonManager;
    }
}
