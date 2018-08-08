package com.scottlogic.deg.generator.generation.iterators;

import com.scottlogic.deg.generator.restrictions.NumericRestrictions;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class NumericIterator implements IFieldSpecIterator {
     private enum Strategy {
         UpFromMin,
         DownFromMax,
         MinToMax,
         OutFromMiddle
     }

     private final NumericRestrictions restrictions;
     private final Set<Object> blacklist;
     private Strategy strategy;
     private BigDecimal nextValue;
     private boolean hasNext = true;
     private BigDecimal stepSize;

     public NumericIterator(NumericRestrictions restrictions, Set<Object> blacklist) {
         this.restrictions = restrictions;
         chooseStrategy();
         if (blacklist != null) {
             this.blacklist = blacklist;
         }
         else {
             this.blacklist = new HashSet<>();
         }
         setInitialValue();
     }

    @Override
    public Object next() {
         if (!hasNext) {
             return null;
         }
         BigDecimal rv = nextValue;
         hasNext = computeNextValue();
         return rv;
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }

    @Override
    public boolean isInfinite() {
        return true;
    }

    private void chooseStrategy() {
         if (restrictions.min == null) {
             if (restrictions.max == null) {
                 strategy = Strategy.OutFromMiddle;
                 stepSize = BigDecimal.ONE;
             }
             else {
                 strategy = Strategy.DownFromMax;
                 stepSize = BigDecimal.ONE;
             }
         }
         else {
             if (restrictions.max == null) {
                 strategy = Strategy.UpFromMin;
                 stepSize = BigDecimal.ONE;
             }
             else {
                 strategy = Strategy.MinToMax;
                 stepSize = decideStepSize(restrictions.min.getLimit(), restrictions.max.getLimit());
             }
         }
     }

     private BigDecimal decideStepSize(BigDecimal min, BigDecimal max) {
         BigDecimal diff = max.subtract(min).abs();
         if (diff.compareTo(BigDecimal.TEN) > 0) {
             return BigDecimal.ONE;
         }
         return diff.divide(BigDecimal.valueOf(100));
     }

     private void setInitialValue() {
         switch (strategy) {
             case OutFromMiddle:
                 nextValue = BigDecimal.ZERO;
                 break;
             case UpFromMin:
             case MinToMax:
                 nextValue = restrictions.min.isInclusive() ?
                         restrictions.min.getLimit() :
                         restrictions.min.getLimit().add(stepSize);
                 break;
             case DownFromMax:
                 nextValue = restrictions.max.isInclusive() ?
                         restrictions.max.getLimit() :
                         restrictions.max.getLimit().subtract(stepSize);
                 break;
         }
     }

     private boolean computeNextValue() {
         switch (strategy) {
             default:
             case UpFromMin:
                 nextValue = nextValue.add(BigDecimal.ONE);
                 return true;
             case MinToMax:
                 nextValue = nextValue.add(BigDecimal.ONE);
                 if ((nextValue.compareTo(restrictions.max.getLimit()) > 0) ||
                         (!restrictions.max.isInclusive() && nextValue.compareTo(restrictions.max.getLimit()) == 0)) {
                     return false;
                 }
                 return true;
             case DownFromMax:
                 nextValue = nextValue.subtract(stepSize);
                 return true;
             case OutFromMiddle:
                 if (nextValue.compareTo(BigDecimal.ZERO) > 0) {
                     nextValue = BigDecimal.ZERO.subtract(nextValue);
                 }
                 else {
                     nextValue = nextValue.abs().add(stepSize);
                 }
                 return true;
         }
     }
}
