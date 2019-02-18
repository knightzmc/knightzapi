package uk.knightz.knightzapi.menu.adapter.options;

import org.apache.commons.lang.Validate;
import uk.knightz.knightzapi.menu.adapter.iface.DefaultObjectTokenToItemStackAdapter;
import uk.knightz.knightzapi.menu.adapter.iface.DefaultUnfriendlyFilter;
import uk.knightz.knightzapi.menu.adapter.iface.ObjectTokenToItemStackAdapter;
import uk.knightz.knightzapi.menu.adapter.iface.UnfriendlyFilter;

public class OptionsBuilder {
    private int modifierBlacklist;
    private ObjectTokenToItemStackAdapter objectTokenToItemStackAdapter = new DefaultObjectTokenToItemStackAdapter();
    private UnfriendlyFilter unfriendlyFilter = new DefaultUnfriendlyFilter();
    private int settings = 0;


    public OptionsBuilder unfriendlyFilter(UnfriendlyFilter filter) {
        Validate.notNull(filter);
        this.unfriendlyFilter = filter;
        return this;
    }

    public OptionsBuilder objectTokenToItemStackAdapter(ObjectTokenToItemStackAdapter adapter) {
        Validate.notNull(adapter);
        this.objectTokenToItemStackAdapter = adapter;
        return this;
    }

    public OptionsBuilder includeFields() {
        this.settings |= Settings.INCLUDE_FIELDS;
        return this;
    }

    public OptionsBuilder includeUnfriendly() {
        this.settings |= Settings.UNFRIENDLY;
        return this;
    }

    public OptionsBuilder includeStatic() {
        this.settings |= Settings.INCLUDE_STATIC;
        return this;
    }

    public OptionsBuilder modifierBlacklist(int modifierBlacklist) {
        this.modifierBlacklist = modifierBlacklist;
        return this;
    }

    public Options build() {
        return new Options(modifierBlacklist, objectTokenToItemStackAdapter, unfriendlyFilter, settings);
    }


}