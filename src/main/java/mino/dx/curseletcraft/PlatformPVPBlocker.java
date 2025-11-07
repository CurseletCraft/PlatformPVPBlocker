package mino.dx.curseletcraft;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import mino.dx.curseletcraft.commands.ReloadCmd;
import mino.dx.curseletcraft.config.Config;
import mino.dx.curseletcraft.hook.FoliaSupport;
import mino.dx.curseletcraft.listeners.DamageListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class PlatformPVPBlocker extends JavaPlugin {

    private boolean isFoliaServer;

    @Override
    public void onEnable() {
        Config.initialize(this);
        this.isFoliaServer = FoliaSupport.isFolia();
        getServer().getPluginManager().registerEvents(new DamageListener(this), this);
        registerCommands();
        getLogger().info("PlatformPVPBlocker by MinoMC_YTB enabled successfully!");
    }

    private void registerCommands() {
        if(getConfig().getBoolean("enable-reload-command", true)) {
            LiteralCommandNode<CommandSourceStack> reloadCmdNode = ReloadCmd.build(this).build();
            String commandDescription = "Reload config of PlatformPVPBlocker";
            this.getLifecycleManager().registerEventHandler(
                    LifecycleEvents.COMMANDS,
                    event -> event.registrar().register(reloadCmdNode, commandDescription));
        }
    }

    public boolean isFoliaServer() {
        return isFoliaServer;
    }
}
