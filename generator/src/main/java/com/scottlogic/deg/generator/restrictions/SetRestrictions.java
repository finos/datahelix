package com.scottlogic.deg.generator.restrictions;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class SetRestrictions {
    public Set<?> whitelist;
    public Set<?> blacklist;

    /**
     * reconcile whitelist and blacklist of set membership
     * @return whitelist without blacklist
     */
    public Set<?> getReconciledWhitelist() {
        if (whitelist == null) {
            return null;
        }
        if (blacklist == null) {
            return whitelist;
        }
        final Set<?> among = new HashSet<>(whitelist);
        among.removeAll(blacklist);
        return among;
    }

    @Override
    public String toString() {
        return String.format(
                "wList: %s, bList: %s",
                Objects.toString(whitelist, "-"),
                Objects.toString(blacklist, "-")
        );
    }
}
