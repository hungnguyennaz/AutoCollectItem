package me.thinh_geographical.autocollect;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

public class SmeltItem {
   private List<FurnaceRecipe> re = new ArrayList();

   SmeltItem() {
      Iterator<Recipe> br = Bukkit.getServer().recipeIterator();

      while(br.hasNext()) {
         Recipe rn = (Recipe)br.next();
         if (rn instanceof FurnaceRecipe) {
            this.re.add((FurnaceRecipe)rn);
         }
      }
   }

   public ItemStack ItemSmelt(ItemStack item, Player p, boolean check) {
      if (item != null && item.getType() != Material.AIR && check) {
         FurnaceRecipe fr = this.LayRecipeTuList(item);
         if (fr != null && this.hasPermission(p, fr)) {
            ItemStack result = fr.getResult();
            item.setType(result.getType());
            item.setDurability(result.getDurability());
            return item;
         } else {
            return item;
         }
      } else {
         return item;
      }
   }

   private boolean hasPermission(Player p, FurnaceRecipe fr) {
      return p.hasPermission("autocollectitem.smeltitem.*") || p.hasPermission("autocollectitem.smeltitem." + fr.getResult().getType().name());
   }

   public FurnaceRecipe LayRecipeTuList(ItemStack nguon) {
      for(FurnaceRecipe fr : this.re) {
         if (fr.getInput().getType().equals(nguon.getType()) && fr.getInput().getDurability() == nguon.getDurability() && fr.getResult() != null) {
            return fr;
         }
      }

      return null;
   }
}
