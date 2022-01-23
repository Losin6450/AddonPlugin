package me.losin6450.plugin.addonplugin.api;

import me.losin6450.plugin.addonplugin.AddonPlugin;

import java.io.File;

public abstract class Addon {

    private String name;
    private boolean enabled = false;

    public Addon(String n){
        name = n;
    }

    public void setEnabled(Boolean b){
        enabled = b;
    }

    public abstract void onEnable();

    public abstract void onDisable();

    public File getDatafolder(){
        return new File(getPlugin().getDataFolder(), name);
    }

    public AddonPlugin getPlugin(){
        return AddonPlugin.getInstance();
    }

    public String getName() {
        return name;
    }

    public boolean isEnabled(){
        return enabled;
    }
}
