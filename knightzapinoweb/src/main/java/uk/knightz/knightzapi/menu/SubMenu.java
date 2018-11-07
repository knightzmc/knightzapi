/*
 *     This file is part of KnightzAPI
 *
 *     KnightzAPI - A cross server communication library and general utility API for Minecraft Servers
 *     Copyright (C) 2018 Alexander Leslie John Wood
 *
 *     KnightzAPI is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     KnightzAPI is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with KnightzAPI.  If not, see <https://www.gnu.org/licenses/>.
 *
 *     The author of this program, Alexander Leslie John Wood can be contacted at alexwood2403@gmail.com
 *
 */

package uk.knightz.knightzapi.menu;

import lombok.Getter;

/**
 * A child Menu that is usually opened by a parent Menu
 */
@Getter
public class SubMenu extends Menu {
    private final Menu parent;

    /**
     * Create a new SubMenu
     *
     * @param rows   The amount of rows in this SubMenu
     * @param title  The title of this SubMenu
     * @param parent The parent Menu of this SubMenu
     */
    public SubMenu(String title, int rows, Menu parent) {
        super(title, rows);
        this.parent = parent;
        parent.addSubMenu(this);
    }
}
