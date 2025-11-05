package mino.dx.curseletcraft;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.minimessage.MiniMessage;

import static io.papermc.paper.command.brigadier.Commands.literal;

public class ReloadCmd {
    public static LiteralArgumentBuilder<CommandSourceStack> build(PlatformPVPBlocker plugin) {
        // Usage: "/platformpvp reload"
        return literal("platformpvp")
                .requires(src -> src.getSender().hasPermission("platformpvp.admin"))
                .then(literal("reload")
                        .executes(ctx -> {
                            plugin.reloadConfigAndSettings();
                            ctx.getSource().getSender().sendMessage(
                                    MiniMessage.miniMessage().deserialize("<green>✅ Config reloaded successfully!")
                            );
                            return 1;
                        }));
    }
}
