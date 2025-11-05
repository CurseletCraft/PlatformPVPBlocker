package mino.dx.curseletcraft;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.geysermc.floodgate.api.FloodgateApi;

import java.util.HashMap;
import java.util.Map;

public final class PlatformPVPBlocker extends JavaPlugin implements Listener {

    private FloodgateApi floodgateApi;
    private Component blockMessage;

    private boolean enableBypass;
    private boolean messageEnabled;
    private int messageCooldown;
    private boolean soundEnabled;
    private boolean enableReloadCmd;

    private final MiniMessage mm = MiniMessage.miniMessage();
    private final Map<Player, Long> cooldown = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadSettings();

        this.floodgateApi = FloodgateApi.getInstance();
        getServer().getPluginManager().registerEvents(this, this);

        if (enableReloadCmd) registerCommands();

        getLogger().info("PlatformPVPBlocker by MinoMC_YTB enabled successfully!");
    }

    private void loadSettings() {
        enableBypass = getConfig().getBoolean("enable-bypass-permission", true);

        messageEnabled = getConfig().getBoolean("message.enable", true);
        soundEnabled = getConfig().getBoolean("message.enable-sound", true);
        messageCooldown = getConfig().getInt("message.cooldown", -1);

        String msg = getConfig().getString("message.format",
                "<red>Bạn không thể PvP với người chơi ở nền tảng khác!</red>");
        blockMessage = mm.deserialize(msg);

        enableReloadCmd = getConfig().getBoolean("enable-reload-command", true);
    }

    private void registerCommands() {
        LiteralCommandNode<CommandSourceStack> reloadCmdNode = ReloadCmd.build(this).build();
        String commandDescription = "Reload config ofPlatformPVPBlocker";

        this.getLifecycleManager().registerEventHandler(
                LifecycleEvents.COMMANDS,
                event -> event.registrar().register(reloadCmdNode, commandDescription));
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
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

    public void reloadConfigAndSettings() {
        reloadConfig();
        loadSettings();
    }
}
