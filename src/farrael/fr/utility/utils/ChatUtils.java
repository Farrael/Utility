package farrael.fr.utility.utils;

import java.lang.reflect.Field;

import net.minecraft.server.v1_8_R1.ChatSerializer;
import net.minecraft.server.v1_8_R1.IChatBaseComponent;
import net.minecraft.server.v1_8_R1.PacketPlayOutPlayerListHeaderFooter;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import farrael.fr.utility.Utility;
import farrael.fr.utility.configuration.Configuration;

public class ChatUtils {
	// Color
	private static String chars = "\u00A7";

	// Packet field
	private static Field bField;

	/**
	 * Send tablist to all players
	 */
	public static void sendTabListToServer() {
		IChatBaseComponent header_chat, footer_chat;
		PacketPlayOutPlayerListHeaderFooter packet;

		// Default parsing
		String header = parseString(Configuration.HEADER);
		String footer = parseString(Configuration.FOOTER);

		// Send to players
		for(Player player : Bukkit.getOnlinePlayers()) {
			header_chat = ChatSerializer.a(parseName(header, player.getName()));
			footer_chat = ChatSerializer.a(parseName(footer, player.getName()));
			packet = createTabPacket(header_chat, footer_chat);
			((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
		}
	}

	/**
	 * Send tablist to all players
	 */
	public static void clearTabList() {
		PacketPlayOutPlayerListHeaderFooter packet;

		// Clearing parsing
		IChatBaseComponent header = ChatSerializer.a("");
		IChatBaseComponent footer = ChatSerializer.a("");

		// Send to players
		for(Player player : Bukkit.getOnlinePlayers()) {
			packet = createTabPacket(header, footer);
			((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
		}
	}

	/**
	 * Send tablist to one player
	 */
	public static void sendTabList(Player player) {
		IChatBaseComponent header_chat, footer_chat;
		PacketPlayOutPlayerListHeaderFooter packet;

		// Default parsing
		String header = parseString(Configuration.HEADER);
		String footer = parseString(Configuration.FOOTER);

		// Send to player
		header_chat = ChatSerializer.a(parseName(header, ""));
		footer_chat = ChatSerializer.a(parseName(footer, ""));
		packet = createTabPacket(header_chat, footer_chat);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}

	public static PacketPlayOutPlayerListHeaderFooter createTabPacket(IChatBaseComponent header, IChatBaseComponent footer){
		PacketPlayOutPlayerListHeaderFooter headerFooter = new PacketPlayOutPlayerListHeaderFooter(header);
		try {
			if (bField == null) {
				bField = headerFooter.getClass().getDeclaredField("b");
				bField.setAccessible(true);
			}
			bField.set(headerFooter, footer);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return headerFooter;
	}

	/**
	 * Parse message 
	 * @param string - Format
	 */
	public static String parseString(String string) {
		string = string.replace("%server%", Utility.getInstance().getServer().getName());
		string = string.replace("%online%", Utility.getInstance().getServer().getOnlinePlayers().size() + "");
		string = ChatColor.translateAlternateColorCodes('&', string);
		return "{text:\"" + string + "\"}";
	}

	/**
	 * Parse player name with %player%
	 * @param string - Format
	 * @param name - Player name
	 */
	public static String parseName(String string, String name) {
		return string.replace("%player%", name);
	}

	/**
	 * Transformation des codes couleur pour le motd
	 * @param c - Symbole
	 * @param string - Message
	 */
	public static String parseMotdColor(char c, String string){
		char[] b = string.toCharArray();
		String result = "";
		int i;

		for (i = 0; i < b.length - 1; i++) {
			if ((b[i] == c) && ("0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[(i + 1)]) > -1))
				result += chars + Character.toLowerCase(b[(++i)]);
			else
				result += b[i];
		}
		
		result += b[b.length-1];

		return result;
	}
}
