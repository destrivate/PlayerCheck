package ru.dev.event; 


import java.util.Calendar;
import java.util.Date;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;

import ru.dev.manager.CheckData;
import ru.dev.manager.CheckManager;
import ru.dev.util.ConfigUtil;

public class AllEvent implements Listener {
    private boolean checkAndCancel(Player player, Cancellable event) {
        if(CheckManager.isChecking(player)){
            event.setCancelled(true);
            for(String s : ConfigUtil.getList("on-check")){
                player.sendMessage(s);
            }
            return true;
        }
        return false;
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        
        if (!player.isOnGround()) {
            return;
        }
        
        checkAndCancel(player, event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        String msg = event.getMessage();
        Player player = event.getPlayer();
        String lowerMsg = msg.toLowerCase();

        if (lowerMsg.startsWith("/cc ") || lowerMsg.equals("/cc")) {
            event.setCancelled(true);
            
            String[] args = msg.split(" ", 2);
            if (args.length < 2 || args[1].trim().isEmpty()) {
                player.sendMessage("§cВы не ввели сообщение! Используйте: /cc [текст]");
                return;
            }

            String message = args[1].trim();

            if (CheckManager.isChecking(player)) {
                Player inspector = CheckManager.getInspector(player);
                if (inspector != null && inspector.isOnline()) {
                    inspector.sendMessage("§7[Подозреваемый -> Вы]: §f" + message);
                    player.sendMessage("§7[Вы -> Проверяющему]: §f" + message);
                } else {
                    player.sendMessage("§cИнспектор сейчас не в сети или не найден.");
                }
                return;
            }

            if (CheckManager.isInspector(player)) {
                Player suspect = CheckManager.getSuspect(player);
                if (suspect != null && suspect.isOnline()) {
                    suspect.sendMessage("§7[Проверяющий -> Вы]: §f" + message);
                    player.sendMessage("§7[Вы -> Подозреваемому]: §f" + message);
                } else {
                    player.sendMessage("§cИгрок, которого вы проверяли, не в сети.");
                }
                return;
            }
            
            return;
        }

        checkAndCancel(player, event);
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            checkAndCancel((Player) event.getWhoClicked(), event);
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        checkAndCancel(event.getPlayer(), event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getPlayer() instanceof Player) {
            checkAndCancel((Player) event.getPlayer(), event);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDrop(PlayerDropItemEvent event) {
        checkAndCancel(event.getPlayer(), event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPickup(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player) {
            checkAndCancel((Player) event.getEntity(), event);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreak(BlockBreakEvent event) {
        for (CheckData cd : CheckManager.checks) {
            Player suspect = Bukkit.getPlayer(cd.suspect);
            
            if (suspect == null || !suspect.isOnline()) {
                continue;
            }

            if (!suspect.getWorld().equals(event.getBlock().getWorld())) {
                continue;
            }

            int playerX = suspect.getLocation().getBlockX();
            int playerY = suspect.getLocation().getBlockY();
            int playerZ = suspect.getLocation().getBlockZ();

            int blockX = event.getBlock().getX();
            int blockY = event.getBlock().getY();
            int blockZ = event.getBlock().getZ();

            if (playerX == blockX && playerZ == blockZ && (playerY - 1 == blockY)) {
                event.setCancelled(true);
                return;
            }
        }

        checkAndCancel(event.getPlayer(), event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlace(BlockPlaceEvent event) {
        checkAndCancel(event.getPlayer(), event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent event) { 
        checkAndCancel(event.getPlayer(), event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteractEntity(PlayerInteractEntityEvent event) {
        checkAndCancel(event.getPlayer(), event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            checkAndCancel(player, event);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            checkAndCancel((Player) event.getDamager(), event);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSwapItems(PlayerSwapHandItemsEvent event) {
        checkAndCancel(event.getPlayer(), event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onArmorStand(PlayerArmorStandManipulateEvent event) {
        checkAndCancel(event.getPlayer(), event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFish(PlayerFishEvent event) {
        checkAndCancel(event.getPlayer(), event);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if(CheckManager.isChecking(player)){
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, 25);
            Date expireDate = cal.getTime();
            Bukkit.getBanList(BanList.Type.NAME).addBan(
                player.getName(),        
                "§cВыход с проверки",     
                expireDate,              
                "Console"            
            );
            CheckManager.getInspector(event.getPlayer()).sendMessage("§cИгрок покинул проверку (" + event.getPlayer().getName() + ")");
            CheckManager.kill(player);
        }
    }

}
