package pt.apolomachado.cw;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.plugin.java.JavaPlugin;
import pt.apolomachado.cw.commands.ControllableWitherCommand;
import pt.apolomachado.cw.listeners.ControllableWitherListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ControllableWither extends JavaPlugin {

    @Setter
    @Getter
    protected ControllableWitherCommand controllableWitherCommand;

    @Getter
    @Setter
    protected ControllableWitherListener controllableWitherListener;

    @Getter
    public static HashMap<Player, Wither> spawnedWithers = new HashMap<>();

    public void onEnable() {
        saveDefaultConfig();
        setControllableWitherCommand(new ControllableWitherCommand(this));
        getControllableWitherCommand().register();

        setControllableWitherListener(new ControllableWitherListener(this));
        getControllableWitherListener().register();

        startScheduling();
    }

    public void onDisable() {
        super.onDisable();
    }

    public String getMessage(String string) {
        return Objects.requireNonNull(getConfig().getString("Messages." + string)).replace('&', '§');
    }

    protected void startScheduling() {
        getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for(Map.Entry<Player, Wither> entrySet : getSpawnedWithers().entrySet()) {
                float dirX = (float) (0 - (Math.sin((entrySet.getKey().getLocation().getYaw() / 180) * Math.PI) * 3));
                float dirZ = (float) (Math.cos((entrySet.getKey().getLocation().getYaw() / 180) * Math.PI) * 3);
                entrySet.getValue().setVelocity(entrySet.getValue().getVelocity().setX(dirX));
                entrySet.getValue().setVelocity(entrySet.getValue().getVelocity().setZ(dirZ));
            }
        }, 0, 20);
    }
}