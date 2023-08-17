package cloum.berk.pixelmoncooldowns;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;

public final class PixelmonCooldowns extends JavaPlugin {

    public static long cooldown = 0L;
    public static String message = "";

    public void onEnable() {
        initConfig();
        Bukkit.getServer().getPluginManager().registerEvents(new CommandListener(), this);
    }

    private void initConfig() {
        getConfig().addDefault("cooldown", 3000);
        getConfig().addDefault("message", "Please wait a bit before using this command again!");
        getConfig().options().copyDefaults(true);
        saveConfig();
        cooldown = getConfig().getLong("cooldown");
        message = getConfig().getString("message");
    }
}
