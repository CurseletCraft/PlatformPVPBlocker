package mino.dx.curseletcraft.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import mino.dx.curseletcraft.PlatformPVPBlocker;
import mino.dx.curseletcraft.config.Config;
import net.kyori.adventure.text.minimessage.MiniMessage;

import static io.papermc.paper.command.brigadier.Commands.literal;

public class ReloadCmd {
    public static LiteralArgumentBuilder<CommandSourceStack> build(PlatformPVPBlocker plugin) {
        // Usage: "/platformpvp reload"
        return literal("platformpvp")
                .requires(src -> src.getSender().hasPermission("platformpvp.admin"))
                .then(literal("reload")
                        .executes(ctx -> {
                            Config.reload();
                            ctx.getSource().getSender().sendMessage(
                                    MiniMessage.miniMessage().deserialize("<green>✅ Config reloaded successfully!")
                            );
                            return 1;
                        }));
    }
}
