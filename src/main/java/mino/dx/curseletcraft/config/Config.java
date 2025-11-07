package mino.dx.curseletcraft.config;

import mino.dx.curseletcraft.PlatformPVPBlocker;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Config {
    private static PlatformPVPBlocker plugin = JavaPlugin.getPlugin(PlatformPVPBlocker.class);
    private static FileConfiguration config;

    public static void initialize(PlatformPVPBlocker plugin) {
        Config.plugin = plugin;
        plugin.saveDefaultConfig();
    }

    public static void reload() {
        plugin.reloadConfig();
        config = plugin.getConfig();
    }

    public static boolean forPath(String path) {
        return config.isSet(path);
    }

    public static void setIfMissing(String path, Object value) {
        if (!config.isSet(path)) {
            plugin.getLogger().warning("⚠ Thiếu cấu hình: " + path + " → Đang tạo giá trị mặc định.");
            config.set(path, value);
        }
    }

    public static FileConfiguration get() {
        return config;
    }
}
