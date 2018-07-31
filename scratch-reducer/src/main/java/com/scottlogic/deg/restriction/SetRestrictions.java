package com.scottlogic.deg.restriction;

import java.util.HashSet;
import java.util.Set;

public class SetRestrictions<T> {
    private final Class<T> typeToken;

    public Set<T> whitelist;
    public Set<T> blacklist;

    public SetRestrictions(Class<T> typeToken) {
        this.typeToken = typeToken;
    }

    public Class<T> getTypeToken() {
        return typeToken;
    }

    /**
     * reconcile whitelist and blacklist of set membership
     * @return whitelist without blacklist
     */
    public Set<T> getReconciledWhitelist() {
        if (whitelist == null) {
            return null;
        }
        if (blacklist == null) {
            return whitelist;
        }
        final Set<T> among = new HashSet<>(whitelist);
        among.removeAll(blacklist);
        return among;
    }
}
