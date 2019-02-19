/*
 * MIT License
 *
 * Copyright (c) 2019 Alexander Leslie John Wood
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

package uk.knightz.knightzapi.menu.page;

import lombok.Getter;
import uk.knightz.knightzapi.menu.Menu;

import java.util.List;

/**
 * Subclass of Menu that represents a Page in a Menu. It behaves slightly differently to a normal Menu object.
 */
@Getter
public class Page extends Menu {

    private final Menu parent;

    public Page(int rows, String title, Menu parent) {
        super(title, rows);
        this.parent = parent;
        if (rows > 6) throw new IndexOutOfBoundsException("Page rows cannot be more than 6");
    }

    @Override
    public List<Page> getPages() {
        return parent.getPages();
    }

    @Override
    public int firstEmpty() {
        return inventory.firstEmpty();
    }
}
