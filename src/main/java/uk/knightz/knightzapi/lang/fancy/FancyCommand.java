/*
 * MIT License
 *
 * Copyright (c) 2018 Alexander Leslie John Wood
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package uk.knightz.knightzapi.lang.fancy;

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.UUID;

public class FancyCommand extends BukkitCommand {
    private static final UUID uuid = UUID.randomUUID();
    public static final String COMMAND = uuid.toString().replace("-", "");

    public FancyCommand() {
        super(COMMAND);
        this.description = "Required to handle Fancy Messages correctly";
        this.setPermission("/"); //Hopefully invalid so other plugins won't include it in their help pages
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length < 2) {
                return true;
            }
            UUID messageUUID = UUID.fromString(args[0]);
            String page = args[1];
            SuperFancyMessage message = SuperFancyMessages.getInstance().fromUUID(messageUUID);
            final int i = Integer.parseInt(page);
            if (i == -1)
                p.spigot().sendMessage(message.toBukkitText());
            else
                p.spigot().sendMessage(message.toBukkitText(i));
        }
        return true;
    }
}
