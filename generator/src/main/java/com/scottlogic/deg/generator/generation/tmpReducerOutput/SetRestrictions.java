package com.scottlogic.deg.generator.generation.tmpReducerOutput;

import java.util.HashSet;
import java.util.Set;

public class SetRestrictions {
    public Set<Object> whitelist;
    public Set<Object> blacklist;

    /**
     * reconcile whitelist and blacklist of set membership
     * @return whitelist without blacklist
     */
    public Set<Object> getReconciledWhitelist() {
        if (whitelist == null) {
            return null;
        }
        if (blacklist == null) {
            return whitelist;
        }
        final Set<Object> among = new HashSet<>(whitelist);
        among.removeAll(blacklist);
        return among;
    }
}
