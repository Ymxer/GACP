package ac.grim.grimac.manager.init.start;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

import ac.grim.grimac.events.packets.*;
import ac.grim.grimac.events.packets.worldreader.BasePacketWorldReader;
import ac.grim.grimac.events.packets.worldreader.PacketWorldReaderEighteen;
import ac.grim.grimac.manager.init.Initable;
import ac.grim.grimac.utils.anticheat.LogUtil;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;


public class PacketManager implements Initable {
    @Override
    public void start() {
        LogUtil.info("Registering packets...");

        PacketEvents.getAPI().getEventManager().registerListener(new PacketPlayerJoinQuit());
        PacketEvents.getAPI().getEventManager().registerListener(new PacketPingListener());
        PacketEvents.getAPI().getEventManager().registerListener(new PacketPlayerDigging());
        PacketEvents.getAPI().getEventManager().registerListener(new PacketPlayerAttack());
        PacketEvents.getAPI().getEventManager().registerListener(new PacketEntityAction());
        PacketEvents.getAPI().getEventManager().registerListener(new PacketBlockAction());
        PacketEvents.getAPI().getEventManager().registerListener(new PacketSelfMetadataListener());
        PacketEvents.getAPI().getEventManager().registerListener(new PacketServerTeleport());
        PacketEvents.getAPI().getEventManager().registerListener(new PacketPlayerCooldown());
        PacketEvents.getAPI().getEventManager().registerListener(new PacketPlayerRespawn());
        PacketEvents.getAPI().getEventManager().registerListener(new CheckManagerListener());
        PacketEvents.getAPI().getEventManager().registerListener(new PacketPlayerSteer());


        if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_18)) {
            PacketEvents.getAPI().getEventManager().registerListener(new PacketWorldReaderEighteen());
        } else {
            PacketEvents.getAPI().getEventManager().registerListener(new BasePacketWorldReader());
        }

        File paperFile = new File("paper.yml");

        AlertPluginMessenger.setBungeeEnabled(YamlConfiguration.loadConfiguration(new File("spigot.yml")).getBoolean("settings.bungeecord") ||
                (paperFile.exists() && YamlConfiguration.loadConfiguration(paperFile).getBoolean("settings.velocity-support.enabled")));

        if (AlertPluginMessenger.isBungeeEnabled()) {
            PacketEvents.getAPI().getEventManager().registerListener(new AlertPluginMessenger());
        }

        LogUtil.info("Bungeecord " + (AlertPluginMessenger.isBungeeEnabled() ? "detected" : "not found") + "...");

        PacketEvents.getAPI().getEventManager().registerListener(new PacketSetWrapperNull());

        PacketEvents.getAPI().init();
    }
}
