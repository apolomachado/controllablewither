package pt.apolomachado.cw.commands;

import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import pt.apolomachado.cw.ControllableWither;

import java.util.Objects;

public class ControllableWitherCommand implements CommandExecutor {

    @Getter
    protected final ControllableWither plugin;

    public ControllableWitherCommand(ControllableWither plugin) {
        this.plugin = plugin;
    }

    public void register() {
        Objects.requireNonNull(getPlugin().getCommand("controllablewither")).setExecutor(this);
    }

    /**
     * Deprecated because:
     *  - Wither#setHealth
     *  - Wither#setPassenger
     * */

    @Deprecated
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(commandSender instanceof Player) {
            Player p = (Player) commandSender;
            if(p.hasPermission("controllablewither.use")) {
                if(ControllableWither.getSpawnedWithers().containsKey(p)) {
                    p.sendMessage(getPlugin().getMessage("AlreadySpawned"));
                } else {
                    Wither newWither = (Wither) p.getWorld().spawnEntity(p.getLocation(), EntityType.WITHER);
                    newWither.setPassenger(p);
                    newWither.setAI(false);
                    newWither.setHealth(newWither.getMaxHealth());
                    ControllableWither.getSpawnedWithers().put(p, newWither);
                    p.sendMessage(getPlugin().getMessage("Summoned"));
                }
            } else {
                p.sendMessage(getPlugin().getMessage("PermissionError"));
            }
        }
        return false;
    }
}