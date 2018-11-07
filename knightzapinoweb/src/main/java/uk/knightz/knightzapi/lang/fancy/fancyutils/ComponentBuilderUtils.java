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

package uk.knightz.knightzapi.lang.fancy.fancyutils;

import com.google.common.base.Preconditions;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import uk.knightz.knightzapi.utils.VersionUtil;

import java.util.List;

public class ComponentBuilderUtils {

    public static ComponentBuilder append(ComponentBuilder builder, BaseComponent[] components, ComponentBuilder.FormatRetention retention) {
        Preconditions.checkArgument(components.length != 0, "No components to append");

        List<BaseComponent> parts = BasecomponentReflection.partsOf(builder);
        BaseComponent previous = BasecomponentReflection.currentOf(builder);
        BaseComponent current = previous.duplicate();

        for (BaseComponent component : components) {
            parts.add(component);
            BasecomponentReflection.setParts(builder, parts);

            BasecomponentReflection.setCurrent(builder, component.duplicate());
            if (VersionUtil.isNewerThan(VersionUtil.Version.v1_8)) {
                BasecomponentReflection.currentOf(builder).copyFormatting(previous, retention, false);
            }
        }
        return builder;
    }
}
