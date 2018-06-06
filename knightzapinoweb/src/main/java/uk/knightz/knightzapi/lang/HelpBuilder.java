package uk.knightz.knightzapi.lang;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.*;

/**
 * This class was created by AlexL (Knightz) on 28/01/2018 at 20:12.
 * Copyright Knightz  2018 For assistance using this class, or for permission to use it in any way, contact @Knightz#0986 on Discord.
 * <p>
 * A useful Builder for making help messages.
 **/
public class HelpBuilder implements ConfigurationSerializable {
	private final List<HelpMessage> messages = new ArrayList<>();
	private String title = "";
	private ChatColor primary = ChatColor.WHITE;
	private ChatColor secondary = ChatColor.WHITE;

	public HelpBuilder() {}

	private HelpBuilder(String title, ChatColor primary, ChatColor secondary, List<HelpMessage> messages) {
		this.title = title;
		this.primary = primary;
		this.secondary = secondary;
		this.messages.addAll(messages);
	}

	public static Class<? extends HelpMessage> getHelpMessageClass() {
		return HelpMessage.class;
	}

	public static HelpBuilder deserialize(Map<String, Object> map) {
		ChatColor main = ChatColor.getByChar(((String) map.get("maincolor")).toCharArray()[0]);
		ChatColor secondary = ChatColor.getByChar(((String) map.get("secondarycolor")).toCharArray()[0]);
		String title = (String) map.get("title");
		List<HelpMessage> messages = map.containsKey("messages") ? (((List<HelpMessage>) map.get("messages")))
				: Collections.singletonList(new HelpMessage("Not found", "No Help Messages found in config file. Contact an admin."));
		return new HelpBuilder(title, main, secondary, messages);
	}


	public List<HelpMessage> getMessages() {
		return messages;
	}

	public String getTitle() {
		return title;
	}

	public HelpBuilder setTitle(String title) {
		this.title = title;
		return this;
	}

	public ChatColor getPrimary() {
		return primary;
	}

	public HelpBuilder setPrimary(ChatColor primary) {
		this.primary = primary;
		return this;
	}

	public ChatColor getSecondary() {
		return secondary;
	}

	public HelpBuilder setSecondary(ChatColor secondary) {
		this.secondary = secondary;
		return this;
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> temp = new LinkedHashMap<>();
		temp.put("maincolor", primary.getChar());
		temp.put("secondarycolor", secondary.getChar());
		temp.put("title", title);
		List<HelpMessage> tempMessages = new ArrayList<>(messages);
		temp.put("messages", tempMessages);
		return temp;
	}


	public HelpBuilder addHelpMessage(String main, String description) {
		this.messages.add(new HelpMessage(Chat.color(main), Chat.color(description)));
		return this;
	}

	public HelpBuilder addPermissionDependentHelpMessage(String name, String description, String permission) {
		this.messages.add(new HelpMessage(Chat.color(name), Chat.color(description), permission));
		return this;
	}

	/**
	 * Convert the HelpBuilder into an array of strings, that can then be printed.
	 * <p>
	 * Permission dependent messages are not applied with this
	 *
	 * @return The HelpBuilder as a String array
	 */
	public String[] build() {
		ArrayList<String> temp = new ArrayList<>();
		temp.add(Chat.center(this.primary + "" + ChatColor.BOLD + this.title));
		temp.add(this.primary + "" + ChatColor.STRIKETHROUGH + "-----------------------------------------------------");
		this.messages.forEach(s -> temp.add(this.primary + s.getMain() + " \u00a77-\u00a7r " + this.secondary + s.getDescription())
		);
		return temp.toArray(new String[0]);
	}


	/**
	 * Build this HelpBuilder into an array of Strings, and then send them to the given CommandSenders, applying permission dependent HelpMessages to each
	 *
	 * @param toSendTo The senders to send the help message to
	 */
	public void sendToPlayer(CommandSender... toSendTo) {
		Arrays.stream(toSendTo).forEachOrdered(p -> {
			ArrayList<String> temp = new ArrayList<>();
			temp.add(Chat.center(this.primary + "" + ChatColor.BOLD + this.title));
			temp.add(this.primary + "" + ChatColor.STRIKETHROUGH + "-----------------------------------------------------");
			this.messages.forEach(s -> {
						if (s.hasPermission()) {
							if (p.hasPermission(s.getPermission())) {
								temp.add(this.primary + s.getMain() + " §7-§r " + this.secondary + s.getDescription());
							}
						} else {
							temp.add(this.primary + s.getMain() + " §7-§r " + this.secondary + s.getDescription());
						}
					}
			);
			p.sendMessage(temp.toArray(new String[0]));
		});
	}

	@SerializableAs ("HelpMessage")
	public static final class HelpMessage implements ConfigurationSerializable {
		private final String main;
		private final String description;
		private String permission;

		HelpMessage(String mainString, String description) {
			this.main = mainString;
			this.description = description;
		}

		HelpMessage(String mainString, String description, String permission) {
			this.main = mainString;
			this.description = description;
			this.permission = permission;
		}

		public static HelpMessage deserialize(Map<String, Object> map) {
			String main = (String) map.get("name");
			String description = (String) map.get("description");
			if (map.containsKey("permission")) {
				return new HelpMessage(main, description, (String) map.get("permission"));
			}
			return new HelpMessage(main, description);
		}

		String getDescription() {
			return this.description;
		}

		String getMain() {
			return this.main;
		}

		boolean hasPermission() {
			return this.permission != null;
		}

		String getPermission() {
			return this.permission;
		}

		@Override
		public Map<String, Object> serialize() {
			Map<String, Object> temp = new LinkedHashMap<>();
			temp.put("name", main);
			temp.put("description", description);
			if (permission != null) {
				temp.put("permission", permission);
			}
			return temp;
		}
	}

}

