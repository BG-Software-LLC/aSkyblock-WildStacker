package com.wasteofplastic.askyblock.util;

import org.bukkit.Location;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.inventory.ItemStack;
import xyz.wildseries.wildstacker.api.WildStackerAPI;

public class WildStackerUtil {

    public static int getSpawnerAmount(Location location){
        return WildStackerAPI.getSpawnersAmount((CreatureSpawner) location.getBlock().getState());
    }

    public static int getBarrelAmount(Location location){
        return WildStackerAPI.getBarrelAmount(location.getBlock());
    }

    public static ItemStack getBarrelItem(Location location){
        return WildStackerAPI.getStackedBarrel(location.getBlock()).getBarrelItem(1);
    }

    public static boolean isStackedBarrel(Location location){
        return WildStackerAPI.getWildStacker().getSystemManager().isStackedBarrel(location.getBlock());
    }

}
