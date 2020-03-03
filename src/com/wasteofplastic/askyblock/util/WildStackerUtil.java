package com.wasteofplastic.askyblock.util;

import com.bgsoftware.wildstacker.api.WildStackerAPI;
import com.bgsoftware.wildstacker.api.objects.StackedSnapshot;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class WildStackerUtil {

    private static final Map<String, StackedSnapshot> chunkSnapshots = new HashMap<>();

    public static void cacheChunk(Chunk chunk){
        try {
            StackedSnapshot stackedSnapshot;
            try {
                stackedSnapshot = WildStackerAPI.getWildStacker().getSystemManager().getStackedSnapshot(chunk);
            } catch (Throwable ex) {
                //noinspection deprecation
                stackedSnapshot = WildStackerAPI.getWildStacker().getSystemManager().getStackedSnapshot(chunk, false);
            }
            if (stackedSnapshot != null)
                chunkSnapshots.put(getId(chunk), stackedSnapshot);
        }catch(Throwable ignored){}
    }

    public static void uncacheChunk(int x, int z){
        chunkSnapshots.remove(getId(x, z));
    }

    public static Pair<Integer, EntityType> getSpawner(Location location) {
        String id = getId(location);
        if(chunkSnapshots.containsKey(id)) {
            Map.Entry<Integer, EntityType> entry = chunkSnapshots.get(id).getStackedSpawner(location);
            return new Pair<>(entry.getKey(), entry.getValue());
        }

        throw new RuntimeException("Chunk " + id + " is not cached.");
    }

    public static Pair<Integer, ItemStack> getBlock(Location location) {
        String id = getId(location);
        if(chunkSnapshots.containsKey(id)) {
            StackedSnapshot stackedSnapshot = chunkSnapshots.get(id);
            Pair<Integer, ItemStack> pair;

            try{
                pair = new Pair<>(stackedSnapshot.getStackedBarrelItem(location));
            }catch(Throwable ex){
                Map.Entry<Integer, Material> barrelEntry = stackedSnapshot.getStackedBarrel(location);
                pair = new Pair<>(barrelEntry.getKey(), new ItemStack(barrelEntry.getValue()));
            }

            return pair.z.getType().name().contains("AIR") ? null : pair;
        }

        throw new RuntimeException("Chunk " + id + " is not cached. Location: " + location);
    }

    private static String getId(Location location){
        return getId(location.getBlockX() >> 4, location.getBlockZ() >> 4);
    }

    private static String getId(Chunk chunk){
        return getId(chunk.getX(), chunk.getZ());
    }

    private static String getId(int x, int z){
        return x + "," + z;
    }

}
