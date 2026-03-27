package com.nynsrulers.lorepowers;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
public class PearlLink implements Listener {
    private final Map<UUID, UUID> pearlLink = new HashMap<>();
    @EventHandler
    public void pearlLinking(PlayerInteractEntityEvent event) {
        Player tper = event.getPlayer();
        if (!LorePowers.getInstance().checkPower(tper.getUniqueId(), Power.PEARL_LINK)) return;
        if (CooldownManager.getInstance().checkCooldown(tper.getUniqueId(), Power.PEARL_LINK)) {
            event.setCancelled(true);
            tper.sendMessage(CoreTools.getInstance().getPrefix() + ChatColor.RED + "You drained too much dimension power, you can't link anyone right now.");
            return; }
        if (!tper.isSneaking()) return;
        if (!(event.getRightClicked() instanceof Player tped)) return;
        if (pearlLink.containsKey(tper.getUniqueId())) {
            tper.sendMessage(CoreTools.getInstance().getPrefix() + ChatColor.RED + "You already have someone linked!");
            return; }
        event.setCancelled(true);
        pearlLink.put(tper.getUniqueId(), tped.getUniqueId());
        tper.sendMessage(CoreTools.getInstance().getPrefix() + ChatColor.GREEN + "You have linked " + tped.getName() + " to your next Ender Pearl!"); }
    @EventHandler
    public void teleportPerson(PlayerTeleportEvent event) {
        if (event.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL) return;
        Player tper = event.getPlayer();
        if (!pearlLink.containsKey(tper.getUniqueId())) return;
        Player tped = Bukkit.getPlayer(pearlLink.get(tper.getUniqueId()));
        if (tped == null) return;
        event.setCancelled(true);
        tped.teleport(event.getTo());
        tped.sendMessage(CoreTools.getInstance().getPrefix() + ChatColor.BLUE + "bro got bamboozled");
        CooldownManager.getInstance().addCooldown(tper.getUniqueId(), Power.PEARL_LINK, 1200L);
        pearlLink.remove(tper.getUniqueId()); } }