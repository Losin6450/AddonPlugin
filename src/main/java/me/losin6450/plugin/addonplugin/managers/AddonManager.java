package me.losin6450.plugin.addonplugin.managers;

import me.losin6450.plugin.addonplugin.AddonPlugin;
import me.losin6450.plugin.addonplugin.api.Addon;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class AddonManager {

    private List<Addon> addons = new ArrayList<>();
    private List<String> mainclasses = new ArrayList<>();

    public void loadAddons() {
        File dir = new File(AddonPlugin.getInstance().getDataFolder(), "addons");
        if(!dir.exists()){
            dir.mkdirs();
        }
        List<File> files = new ArrayList<File>();
        List<URL> urls = new ArrayList<URL>();
        for(File file : dir.listFiles()){
            if(!file.isDirectory() && file.getName().endsWith(".jar")){
                try {
                    files.add(file);
                    urls.add(file.toURI().toURL());
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        URLClassLoader loader = new URLClassLoader(urls.toArray(new URL[urls.size()]), this.getClass().getClassLoader());
        for(File file : files){
            try {
                JarFile jarFile = new JarFile(file);
                YamlConfiguration info = getAddonInfo(jarFile);
                if(info != null) {
                    if (info.contains("Main-Class") && info.contains("Name")) {
                        if(mainclasses.contains(info.getString("Main-Class"))){
                            throw new Exception("Addon " + jarFile.getName() + " has already been loaded!");
                        } else {
                            Class main = loader.loadClass(info.getString("Main-Class"));
                            if(Addon.class.isAssignableFrom(main)) {
                                Addon addon = (Addon) main.getDeclaredConstructor(String.class).newInstance(info.getString("Name"));
                                addons.add(addon);
                            } else {
                                throw new Exception("the main class of the addon " + jarFile.getName() + " doesn't extend Addon");
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    public Addon getAddon(String name){
        for(Addon addon : addons){
            if(addon.getName().equalsIgnoreCase(name)){
                return addon;
            }
        }
        return null;
    }

    public YamlConfiguration getAddonInfo(JarFile jarFile) throws Exception {
        JarEntry entry =  jarFile.getJarEntry("addon.yml");
        if(entry == null){
            throw new Exception("Addon " + jarFile.getName() + " is missing the addon.yml file!");
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(jarFile.getInputStream(entry)));
        YamlConfiguration yaml = new YamlConfiguration();
        yaml.load(reader);
        reader.close();
        return yaml;
    }

    public List<Addon> getAddons() {
        return addons;
    }

    public void enableAddons(){
        for(Addon addon : addons){
            addon.setEnabled(true);
            addon.onEnable();
        }
    }

    public void disableAddons(){
        for(Addon addon : addons){
            addon.setEnabled(false);
            addon.onDisable();
        }
    }
}
