package com.undeadlydev.UTA.utils;

import com.undeadlydev.UTA.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


public class BossBarUtils {

    private static final Map<String, Object> bossbar = new HashMap<>();

    private static Constructor<?> packetPlayOutSpawnEntityLiving;
    private static Constructor<?> packetPlayOutEntityDestroy;
    private static Constructor<?> entityWither;
    private static Method getId;
    private static Method setLocation;
    private static Method setCustomName;
    private static Method setHealth;
    private static Method setInvisible;
    private static Method getWorldHandle;
    private static Method getPlayerHandle;
    private static Field playerConnection;
    private static Method sendPacket;
    private static Method getDataWatcher;
    private static Constructor<?> packetPlayOutEntityTeleport;
    private static Constructor<?> packetPlayOutEntityMetadata;

    static {
        try {
            packetPlayOutSpawnEntityLiving = getNMSClass("PacketPlayOutSpawnEntityLiving").getConstructor(getNMSClass("EntityLiving"));
            packetPlayOutEntityDestroy = getNMSClass("PacketPlayOutEntityDestroy").getDeclaredConstructor(int[].class);
            entityWither = getNMSClass("EntityWither").getConstructor(getNMSClass("World"));
            getId = getNMSClass("Entity").getMethod("getId");
            setLocation = getNMSClass("EntityWither").getMethod("setLocation", double.class, double.class, double.class, float.class, float.class);
            setCustomName = getNMSClass("EntityWither").getMethod("setCustomName", String.class);
            setHealth = getNMSClass("EntityWither").getMethod("setHealth", float.class);
            setInvisible = getNMSClass("EntityWither").getMethod("setInvisible", boolean.class);
            getWorldHandle = getBukkitClass("CraftWorld").getMethod("getHandle");
            getPlayerHandle = getBukkitClass("entity.CraftPlayer").getMethod("getHandle");
            playerConnection = getNMSClass("EntityPlayer").getDeclaredField("playerConnection");
            sendPacket = getNMSClass("PlayerConnection").getMethod("sendPacket", getNMSClass("Packet"));
            getDataWatcher = getNMSClass("EntityWither").getMethod("getDataWatcher");
            packetPlayOutEntityTeleport = getNMSClass("PacketPlayOutEntityTeleport").getConstructor(getNMSClass("Entity"));
            packetPlayOutEntityMetadata = getNMSClass("PacketPlayOutEntityMetadata").getConstructor(int.class, getNMSClass("DataWatcher"), boolean.class);
        } catch (Exception e) {
            Main.get().sendLogMessage("An error occurred during NMS class initialization");
        }
    }

    public static void addBossBar(Player player, String text, float health) {
        try {
            if (contains(player)) return;

            Object world = getWorldHandle.invoke(player.getWorld());
            Object wither = entityWither.newInstance(world);

            Location loc = player.getLocation().clone();
            setLocation.invoke(wither, loc.getX(), loc.getY(), loc.getZ(), 0.0F, 0.0F);
            setCustomName.invoke(wither, text);
            setHealth.invoke(wither, health);
            setInvisible.invoke(wither, true);

            bossbar.put(player.getName(), wither);
            sendPacketToPlayer(player, packetPlayOutSpawnEntityLiving.newInstance(wither));
        } catch (Exception e) {
            Main.get().sendLogMessage("Failed to spawn a BossBar");
        }
    }

    public static void removeBossBar(Player player) {
        try {
            Object wither = bossbar.remove(player.getName());
            if (wither == null) return;

            int id = (int) getId.invoke(wither);
            Object destroyPacket = packetPlayOutEntityDestroy.newInstance(new int[]{id});
            sendPacketToPlayer(player, destroyPacket);
        } catch (Exception e) {
            Main.get().sendLogMessage("Failed to destroy a BossBar");
        }
    }

    public static void updateTitle(Player player, String text) {
        try {
            Object wither = bossbar.get(player.getName());
            if (wither == null) return;

            setCustomName.invoke(wither, text);
            int id = (int) getId.invoke(wither);
            Object metadataPacket = packetPlayOutEntityMetadata.newInstance(id, getDataWatcher.invoke(wither), true);
            sendPacketToPlayer(player, metadataPacket);
        } catch (Exception e) {
            Main.get().sendLogMessage("Failed to update BossBar title");
        }
    }

    public static void updateHealth(Player player, float health) {
        try {
            Object wither = bossbar.get(player.getName());
            if (wither == null) return;

            setHealth.invoke(wither, health);
            int id = (int) getId.invoke(wither);
            Object metadataPacket = packetPlayOutEntityMetadata.newInstance(id, getDataWatcher.invoke(wither), true);
            sendPacketToPlayer(player, metadataPacket);
        } catch (Exception e) {
            Main.get().sendLogMessage("Failed to update BossBar health");
        }
    }

    public static void move(Player player) {
        try {
            Object wither = bossbar.get(player.getName());
            if (wither == null) return;

            Location loc = player.getLocation().clone();
            setLocation.invoke(wither, loc.getX(), loc.getY(), loc.getZ(), 0.0F, 0.0F);
            Object teleportPacket = packetPlayOutEntityTeleport.newInstance(wither);
            sendPacketToPlayer(player, teleportPacket);
        } catch (Exception e) {
            Main.get().sendLogMessage("Failed to move BossBar");
        }
    }

    public static boolean contains(Player player) {
        return bossbar.containsKey(player.getName());
    }

    private static void sendPacketToPlayer(Player player, Object packet) {
        try {
            Object playerHandle = getPlayerHandle.invoke(player);
            Object connection = playerConnection.get(playerHandle);
            sendPacket.invoke(connection, packet);
        } catch (Exception e) {
            Main.get().sendLogMessage("Failed to send packet to player ");
        }
    }

    private static Class<?> getNMSClass(String name) throws ClassNotFoundException {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        return Class.forName("net.minecraft.server." + version + "." + name);
    }

    private static Class<?> getBukkitClass(String name) throws ClassNotFoundException {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        return Class.forName("org.bukkit.craftbukkit." + version + "." + name);
    }
}

