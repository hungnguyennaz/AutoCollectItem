package me.thinh_geographical.autocollect;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class AutoCollect extends JavaPlugin {
   public static AutoCollect pl;
   private static SmeltItem si;
   protected static boolean enablekillmobssmelt;
   protected static boolean enablebreakblocksmelt;

   public void onEnable() {
      pl = this;
      si = new SmeltItem();
      this.getConfig().options().copyDefaults(true);
      this.saveConfig();
      String path = "SmeltItem.Enable.";
      enablekillmobssmelt = this.getConfig().getBoolean(path + "KillMob");
      enablebreakblocksmelt = this.getConfig().getBoolean(path + "BreakBlock");
      Bukkit.getServer().getPluginManager().registerEvents(new EventListener(), this);
      Bukkit.getServer().getConsoleSender().sendMessage("§---Plugin làm bởi Thinh_Geographical---");
      Bukkit.getServer().getConsoleSender().sendMessage("§---Nếu có lỗi thì báo cáo với mình---");
   }

   public static SmeltItem getSmeltItem() {
      return si;
   }
}
