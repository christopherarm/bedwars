package net.trainingsoase.bedwars.utils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import io.sentry.Sentry;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

/**
 * @author byCrypex
 * @version 1.0.0
 * @since
 **/

public class ActionbarAPI {

    public static void setActionBarFor(Player player, WrappedChatComponent text) {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        PacketContainer chatPacket = protocolManager.createPacket(PacketType.Play.Server.CHAT);
        chatPacket.getChatComponents().write(0, text);
        chatPacket.getBytes().write(0, (byte) 2);
        try {
            protocolManager.sendServerPacket(player, chatPacket);
        } catch (InvocationTargetException e) {
            Sentry.captureException(e);
        }
    }
}
