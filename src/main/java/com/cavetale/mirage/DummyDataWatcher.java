package com.cavetale.mirage;

import java.util.List;
import net.minecraft.server.v1_15_R1.DataWatcher;

/**
 * A quick and dirty replacement for DataWatcher which returns
 * whichever list of Items it is given.  This way, we can create
 * custom data packets without reflection.
 */
final class DummyDataWatcher extends DataWatcher {
    final List<DataWatcher.Item<?>> list;

    DummyDataWatcher(List<DataWatcher.Item<?>> list) {
        super(null);
        this.list = list;
    }

    // Original: Fetch all dirty values and set undirty
    @Override
    public List<DataWatcher.Item<?>> b() {
        return list;
    }

    // Original: Fetch all values
    @Override
    public List<DataWatcher.Item<?>> c() {
        return list;
    }

    // Original: Set all values undirty
    @Override
    public void e() { }
};
