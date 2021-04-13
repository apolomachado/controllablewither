package pt.apolomachado.cw.listeners;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.spigotmc.event.entity.EntityDismountEvent;
import pt.apolomachado.cw.ControllableWither;

import java.util.Objects;

public class ControllableWitherListener implements Listener {

    @Getter
    protected final ControllableWither plugin;

    public ControllableWitherListener(ControllableWither plugin) {
        this.plugin = plugin;
    }

    public void register() {
        getPlugin().getServer().getPluginManager().registerEvents(this, getPlugin());
    }

    @EventHandler
    public void fire(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if(ControllableWither.getSpawnedWithers().containsKey(player)) {
            if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                Location location = player.getLineOfSight(null, 2).get(1).getLocation().setDirection(player.getLocation().getDirection());
                Objects.requireNonNull(location.getWorld()).spawn(location, Fireball.class);
            } else if(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
                Location location = player.getLineOfSight(null, 2).get(1).getLocation().setDirection(player.getLocation().getDirection());
                Objects.requireNonNull(location.getWorld()).spawn(location, WitherSkull.class);
            }
        }
    }

    @EventHandler
    public void quit(PlayerQuitEvent e) {
        if(ControllableWither.getSpawnedWithers().containsKey(e.getPlayer()))
            ControllableWither.getSpawnedWithers().get(e.getPlayer()).remove();
    }

    @EventHandler
    public void kicked(PlayerKickEvent e) {
        if(ControllableWither.getSpawnedWithers().containsKey(e.getPlayer()))
            ControllableWither.getSpawnedWithers().get(e.getPlayer()).remove();
    }

    @EventHandler
    public void death(PlayerDeathEvent e) {
        if(ControllableWither.getSpawnedWithers().containsKey(e.getEntity()))
            ControllableWither.getSpawnedWithers().get(e.getEntity()).remove();
    }

    @EventHandler
    public void teleport(PlayerTeleportEvent e) {
        if(ControllableWither.getSpawnedWithers().containsKey(e.getPlayer()))
            ControllableWither.getSpawnedWithers().get(e.getPlayer()).remove();
    }

    @EventHandler
    public void entityDismountEvent(EntityDismountEvent e) {
        if(e.getEntity() instanceof Player && e.getDismounted() instanceof Wither) {
            Wither casted = (Wither) e.getDismounted();
            Player player = (Player) e.getEntity();
            if(ControllableWither.getSpawnedWithers().containsValue(casted)) {
                casted.remove();
                ControllableWither.getSpawnedWithers().remove(player);
                player.sendMessage(getPlugin().getMessage("Dismounted"));
            }
        }
    }
}