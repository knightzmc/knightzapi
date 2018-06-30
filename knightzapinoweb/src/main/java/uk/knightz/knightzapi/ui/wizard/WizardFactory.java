package uk.knightz.knightzapi.ui.wizard;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WizardFactory {
	private WizardFactory() {
	}


	public static Wizard<Player> createWizard(Player player) {
		return new PlayerWizard(player);
	}

	public static Wizard<CommandSender> createCommandSenderWizard(CommandSender sender) {
		return new CommandSenderWizard(sender);
	}
}
