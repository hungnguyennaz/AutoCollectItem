package me.thinh_geographical.autocollect;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class EventListener implements Listener {
   private boolean hasenchant1_11 = Enchantment.getByName("VANISHING_CURSE") != null;
   private Map<String, BlockAndTime> check = new HashMap<>();

   @EventHandler
   public void onDeath(EntityDeathEvent event) {
      LivingEntity e = event.getEntity();
      List<ItemStack> list = event.getDrops();
      Player p = e.getKiller();
      if (e.getKiller() != null) {
         p = e.getKiller();
      }

      Inventory in = p.getInventory();

      for (ItemStack item : list) {
         if (!this.hasenchant1_11 || !item.containsEnchantment(Enchantment.VANISHING_CURSE)) {
            ItemStack SmeltSupplies = AutoCollect.getSmeltItem().ItemSmelt(item.clone(), p, AutoCollect.enablekillmobssmelt);
            HashMap<Integer, ItemStack> add = in.addItem(new ItemStack[]{SmeltSupplies});
            int number = 0;

            for (ItemStack more : add.values()) {
               number += more.getAmount();
            }

            if (!add.isEmpty()) {
               ItemStack clone = item.clone();
               clone.setAmount(number);
               p.getWorld().dropItem(e.getLocation(), clone);
               return;
            }
         }
      }

      list.clear();
      if (event.getDroppedExp() > 0) {
         int i = event.getDroppedExp();
         p.giveExp(i);
         event.setDroppedExp(0);
      }
   }

   @EventHandler
   public void onItemSpawn(ItemSpawnEvent e) {
      if (!e.isCancelled() && e.getEntity() != null && e.getEntity().getItemStack() != null) {
         Location loc = e.getLocation();
         long time = loc.getWorld().getTime();
         if (!e.getEntity().hasMetadata("NotPlayerPickUp")) {
            for(Entry<String, BlockAndTime> list : this.check.entrySet()) {
               if (list.getValue().IsCheckCanPickUp(loc, time)) {
                  Player p = Bukkit.getPlayer(list.getKey());
                  ItemStack item = e.getEntity().getItemStack();
                  item = AutoCollect.getSmeltItem().ItemSmelt(item.clone(), p, AutoCollect.enablebreakblocksmelt && !list.getValue().IsChest());
                  HashMap<Integer, ItemStack> add = p.getInventory().addItem(new ItemStack[]{item});
                  int number = 0;

                  for(ItemStack more : add.values()) {
                     number += more.getAmount();
                  }

                  if (!add.isEmpty()) {
                     item.setAmount(number);
                  } else {
                     e.getEntity().remove();
                  }
                  break;
               }
            }
         }
      }
   }

   @EventHandler
   public void onBreed(EntityBreedEvent event) {
      if (!event.isCancelled()) {
         if (event.getBreeder() instanceof Player) {
            Player p = (Player)event.getBreeder();
               if (event.getExperience() > 0) {
                  int i = event.getExperience();
                  p.giveExp(i);
                  event.setExperience(0);
               }
            }
         }
      }

   @EventHandler
   public void onFish(PlayerFishEvent event) {
      Player p = event.getPlayer();
         if (event.getState() == State.CAUGHT_FISH && event.getCaught() != null && event.getCaught() instanceof Item) {
            if (event.getExpToDrop() > 0) {
               int i = event.getExpToDrop();
               p.giveExp(i);
               event.setExpToDrop(0);
            }

            Item item = (Item)event.getCaught();
            ItemStack itemstack = item.getItemStack();
            HashMap<Integer, ItemStack> n = p.getInventory().addItem(new ItemStack[]{itemstack});
            if (!n.isEmpty()) {
               for(ItemStack list : n.values()) {
                  p.getWorld().dropItemNaturally(p.getLocation(), list);
               }
            }

            item.remove();
         }
      }

   @EventHandler
   public void onDrop(PlayerDropItemEvent drop) {
      if (!drop.isCancelled()) {
         if (drop.getItemDrop() != null) {
            drop.getItemDrop().setMetadata("NotPlayerPickUp", new FixedMetadataValue(AutoCollect.pl, true));
         }
      }
   }

   @EventHandler
   public void onBreakBlock(BlockBreakEvent event) {
      Player p = event.getPlayer();
      Block b = event.getBlock();
         if (p.getGameMode() == GameMode.SURVIVAL || p.getGameMode() == GameMode.ADVENTURE) {
            this.check.put(p.getName(), new BlockAndTime(b.getLocation(), b.getWorld().getTime()));
            if (event.getExpToDrop() > 0) {
               int i = event.getExpToDrop();
               p.giveExp(i);
               event.setExpToDrop(0);
         }
      }
   }
}
