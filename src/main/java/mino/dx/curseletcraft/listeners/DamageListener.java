package mino.dx.curseletcraft.listeners;

import mino.dx.curseletcraft.PlatformPVPBlocker;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.geysermc.floodgate.api.FloodgateApi;

import java.util.HashMap;
import java.util.Map;

public class DamageListener implements Listener {

    private final PlatformPVPBlocker plugin;

    private final Map<Player, Long> cooldown = new HashMap<>();
    private final FloodgateApi floodgateApi = FloodgateApi.getInstance();
    private final MiniMessage mm = MiniMessage.miniMessage();

    public DamageListener(PlatformPVPBlocker plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        // Load config
        FileConfiguration config = plugin.getConfig();
        boolean enableBypass = config.getBoolean("enable-bypass-permission", true);

        boolean messageEnabled = config.getBoolean("message.enable", true);
        boolean soundEnabled = config.getBoolean("message.enable-sound", true);
        int messageCooldown = config.getInt("message.cooldown", -1);

        String msg = config.getString("message.format",
                "<red>Bạn không thể PvP với người chơi ở nền tảng khác!</red>");
        Component blockMessage = mm.deserialize(msg);

        // Handle
        if(!(event.getEntity() instanceof Player victim)) return; // nếu nạn nhân không phải là player thì bỏ qua
        if(!((event.getDamager()) instanceof Player attacker)) return; // nếu người đáng không phải là player thì bỏ qua

        boolean victimIsBedrock = floodgateApi.isFloodgatePlayer(victim.getUniqueId());
        boolean attackerIsBedrock = floodgateApi.isFloodgatePlayer(attacker.getUniqueId());

        // Bypass permission
        if (enableBypass && attacker.hasPermission("platformpvp.bypass")) return;

        // Nếu nền tảng khác nhau
        if (victimIsBedrock != attackerIsBedrock) {
            event.setCancelled(true);

            if (messageEnabled) {
                if (messageCooldown > 0) {
                    long now = System.currentTimeMillis();
                    if (cooldown.getOrDefault(attacker, 0L) > now) return;
                    cooldown.put(attacker, now + (messageCooldown * 1000L));
                }

                victim.sendMessage(blockMessage);
                attacker.sendMessage(blockMessage);
            }

            if (soundEnabled) {
                attacker.playSound(attacker.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            }
        }
    }
}
