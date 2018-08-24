package com.scottlogic.deg.generator.restrictions;

public class FormatRestrictionsMerger {

    public FormatRestrictions merge(FormatRestrictions left, FormatRestrictions right){

        if(left != null && right != null){
            // We can't merge formats yet
            throw new UnsupportedOperationException();
        }

        return left != null ? left : right;

    }
}
