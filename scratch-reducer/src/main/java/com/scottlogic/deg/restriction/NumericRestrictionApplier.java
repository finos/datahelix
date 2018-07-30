package com.scottlogic.deg.restriction;

import com.scottlogic.deg.constraint.AmongConstraint;
import com.scottlogic.deg.constraint.IConstraint;
import com.scottlogic.deg.constraint.NumericLimitConstConstraint;

public class NumericRestrictionApplier implements IRestrictionApplier {
    @Override
    public void apply(IFieldRestriction restriction, IConstraint constraint) {
        if (!(restriction instanceof NumericFieldRestriction)) {
            throw new IllegalStateException();
        }
//        final var typedRestriction = (NumericFieldRestriction<? extends Number>) restriction;
//        if (Integer.class.isAssignableFrom(typedRestriction.getTypeToken())) {
//            this.apply((NumericFieldRestriction<Integer>)typedRestriction, constraint);
//        } else if (Double.class.isAssignableFrom(typedRestriction.getTypeToken())) {
//            this.apply(typedRestriction, constraint);
//        } else {
//            throw new IllegalStateException();
//        }
        apply((NumericFieldRestriction<? extends Number>)restriction, constraint);
    }

    private <T extends Number> void apply(NumericFieldRestriction<T> restriction, IConstraint constraint) {
//        if (constraint instanceof IHasNumericTypeToken) {
//            final var typedConstraint = (IHasNumericTypeToken<? extends Number>) constraint;
//            if (Integer.class.isAssignableFrom(typedConstraint.getNumericTypeToken())) {
//                this.<Integer>applyLimit(restriction, )
//            }
//        }

        if (constraint instanceof NumericLimitConstConstraint) {
            final var typedConstraint = (NumericLimitConstConstraint<? extends Number>) constraint;
//            if (Integer.class.isAssignableFrom(typedConstraint.getTypeToken())) {
//
//            }
            final var limit = (T) typedConstraint.getLimit();
            switch(typedConstraint.getLimitType()) {
                case Min:
                    restriction.setMin(limit);
                    break;
                case Max:
                    restriction.setMax(limit);
                    break;
                default:
                    throw new UnsupportedOperationException();
            }
        } else if (constraint instanceof AmongConstraint) {
            restriction.setAmong(((AmongConstraint<T>) constraint).getAmong());
        } else {
            throw new IllegalStateException();
        }
    }

//    private <T extends Number> void applyLimit(NumericFieldRestriction<T> restriction, NumericLimitConstConstraint<T> constraint) {
//        final var limit = constraint.getLimit();
//        switch(constraint.getLimitType()) {
//            case Min:
//                restriction.setMin(limit);
//                break;
//            case Max:
//                restriction.setMax(limit);
//                break;
//            default:
//                throw new UnsupportedOperationException();
//        }
//    }
}
