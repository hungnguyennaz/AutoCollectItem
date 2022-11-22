package me.thinh_geographical.autocollect;

import org.bukkit.Location;

public class BlockAndTime {
   protected Location loc;
   protected long time;

   public BlockAndTime(Location loc, long time) {
      this.loc = loc;
      this.time = time;
   }

   public boolean IsCheckCanPickUp(Location check, long timecheck) {
      return timecheck == this.time
         && check.getBlockX() == this.loc.getBlockX()
         && (check.getBlockY() == this.loc.getBlockY() || check.getBlockY() - 1 == this.loc.getBlockY())
         && check.getBlockZ() == this.loc.getBlockZ();
   }

   public boolean IsChest() {
      return this.loc.getBlock().getType().name().contains("CHEST");
   }
}
