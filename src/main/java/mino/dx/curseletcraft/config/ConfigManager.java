package mino.dx.curseletcraft.config;

import mino.dx.curseletcraft.PlatformPVPBlocker;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
    private final PlatformPVPBlocker plugin;

    public ConfigManager(PlatformPVPBlocker plugin) {
        this.plugin = plugin;
        initConfigs();
    }

    private void initConfigs() {
        plugin.getLogger().info("Đang kiểm tra tính hoàn chỉnh của file cài đặt (config.yml)...");
        plugin.saveDefaultConfig();
        Config.reload();

        // Thiết lập các key mặc định nếu thiếu
        Config.setIfMissing("enable-bypass-permission", true);
        Config.setIfMissing("message.enable", true);
        Config.setIfMissing("message.enable-sound", true);
        Config.setIfMissing("message.format", "<red>Bạn không thể PvP với người chơi ở nền tảng khác!</red>");
        Config.setIfMissing("message.cooldown", -1);
        Config.setIfMissing("enable-reload-command", true);

        // Lưu lại nếu có bổ sung mới
        plugin.saveConfig();

        FileConfiguration cfg = Config.get();
        plugin.getLogger().info("✅ Đã tải config: "
                + "bypass=" + cfg.getBoolean("enable-bypass-permission")
                + ", cooldown=" + cfg.getInt("message.cooldown")
                + ", reloadCmd=" + cfg.getBoolean("enable-reload-command"));
    }
}
