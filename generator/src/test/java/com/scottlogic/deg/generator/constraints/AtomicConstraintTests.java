package com.scottlogic.deg.generator.constraints;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.inputs.AtomicConstraintReaderLookup;
import com.scottlogic.deg.generator.inputs.IConstraintReader;
import com.scottlogic.deg.generator.inputs.InvalidProfileException;
import com.scottlogic.deg.schemas.v3.ConstraintDTO;
import com.scottlogic.deg.schemas.v3.AtomicConstraintType;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AtomicConstraintTests {

    AtomicConstraintReaderLookup atomicConstraintReaderLookup;
    ProfileFields profileFields;

    @BeforeAll
    public void before() {

        atomicConstraintReaderLookup = new AtomicConstraintReaderLookup();

        List<Field> fields = new ArrayList<>();

        fields.add(new Field("test"));

        profileFields = new ProfileFields(fields);
    }

    @Test
    public void shouldRecogniseAllAtomicConstraints() {

        List missingConstraints = new ArrayList<String>();

        for (AtomicConstraintType type : AtomicConstraintType.values()) {
            IConstraintReader reader = atomicConstraintReaderLookup.getByTypeCode(type.toString());
            // Assert.assertNotNull("No reader found for constraint type '" + type.toString() + "'", reader);
            if (reader == null) {
                missingConstraints.add(type.toString());
            }
        }

        String message = "No constraint reader is associated with the following constraints: " +
                String.join(", ", missingConstraints);

        Assert.assertTrue(
                message,
                missingConstraints.isEmpty());

    }

    private void readField(AtomicConstraintType type, ConstraintDTO dto, Class<?> constraintType){
        IConstraintReader reader = atomicConstraintReaderLookup.getByTypeCode(type.toString());

        try {
            IConstraint constraint = reader.apply(dto, profileFields);
            Assert.assertTrue("Expected " + constraintType.getName() +" but got " + constraint.getClass().getName(),
                    constraintType.isInstance(constraint));
        } catch (InvalidProfileException ex) {
            Assert.fail(ex.getMessage());
        }
    }

    @Test
    public void shouldReadEqualToConstraint() {
        ConstraintDTO dto = new ConstraintDTO();
        dto.field = "test";
        dto.value = "value";

        readField(AtomicConstraintType.ISEQUALTOCONSTANT, dto, IsEqualToConstantConstraint.class);
    }

    @Test
    public void shouldReadInSetConstraint() {
        ConstraintDTO dto = new ConstraintDTO();

        dto.field = "test";
        dto.values = new ArrayList<>();
        dto.values.add("Lorem");
        dto.values.add("Ipsum");

        readField(AtomicConstraintType.ISINSET, dto, IsInSetConstraint.class);
    }

    @Test
    public void shouldReadNullConstraint() {
        ConstraintDTO dto = new ConstraintDTO();
        dto.field = "test";
        dto.value = "value";

        readField(AtomicConstraintType.ISNULL, dto, IsNullConstraint.class);
    }

    @Test
    public void shouldReadOfTypeConstraint() {
        ConstraintDTO dto = new ConstraintDTO();
        dto.field = "test";
        dto.value = "string";

        readField(AtomicConstraintType.ISOFTYPE, dto, IsOfTypeConstraint.class);
    }

    @Test
    public void shouldReadMatchesRegexConstraint() {
        ConstraintDTO dto = new ConstraintDTO();
        dto.field = "test";
        dto.value = "string";

        readField(AtomicConstraintType.MATCHESREGEX, dto, MatchesRegexConstraint.class);
    }

    @Test
    public void shouldReadNotConstraint() {
        ConstraintDTO dto = new ConstraintDTO();
        dto.field = "test";
        dto.not = new ConstraintDTO();
        dto.not.field = "test";
        dto.not.is = "equalTo";
        dto.not.field = "lorem";

        readField(AtomicConstraintType.NOT, dto, NotConstraint.class);
    }

    @Test
    public void shouldReadHasLengthConstraint() {
        ConstraintDTO dto = new ConstraintDTO();
        dto.field = "test";
        dto.value = 1;

        readField(AtomicConstraintType.HASLENGTH, dto, StringHasLengthConstraint.class);
    }

    @Test
    public void shouldReadLongerThanConstraint() {
        ConstraintDTO dto = new ConstraintDTO();
        dto.field = "test";
        dto.value = 1;

        readField(AtomicConstraintType.ISSTRINGLONGERTHAN, dto, IsStringLongerThanConstraint.class);
    }

    @Test
    public void shouldReadShorterThanConstraint() {
        ConstraintDTO dto = new ConstraintDTO();
        dto.field = "test";
        dto.value = 10;

        readField(AtomicConstraintType.ISSTRINGSHORTERTHAN, dto, IsStringShorterThanConstraint.class);
    }

    @Test
    public void shouldReadGreaterThanConstraint() {
        ConstraintDTO dto = new ConstraintDTO();
        dto.field = "test";
        dto.value = 1;

        readField(AtomicConstraintType.ISGREATERTHANCONSTANT, dto, IsGreaterThanConstantConstraint.class);
    }

    @Test
    public void shouldReadGreaterThanOrEqualToConstraint() {
        ConstraintDTO dto = new ConstraintDTO();
        dto.field = "test";
        dto.value = 1;

        readField(AtomicConstraintType.ISGREATERTHANOREQUALTOCONSTANT, dto, IsGreaterThanOrEqualToConstantConstraint.class);
    }

    @Test
    public void shouldReadLessThanConstantConstraint() {
        ConstraintDTO dto = new ConstraintDTO();
        dto.field = "test";
        dto.value = 1;

        readField(AtomicConstraintType.ISLESSTHANCONSTANT, dto, IsLessThanConstantConstraint.class);
    }

    @Test
    public void shouldReadLessThanOrEqualToConstantConstraint() {
        ConstraintDTO dto = new ConstraintDTO();
        dto.field = "test";
        dto.value = 1;

        readField(AtomicConstraintType.ISLESSTHANOREQUALTOCONSTANT, dto, IsLessThanOrEqualToConstantConstraint.class);
    }

    @Test
    public void shouldReadAfterConstantDateTimeConstraint() {
        ConstraintDTO dto = new ConstraintDTO();
        dto.field = "test";
        dto.value = "2018-01-01";

        readField(AtomicConstraintType.ISAFTERCONSTANTDATETIME, dto, IsAfterConstantDateTimeConstraint.class);
    }

    @Test
    public void shouldReadAfterOrEqualToConstantDateTimeConstraint() {
        ConstraintDTO dto = new ConstraintDTO();
        dto.field = "test";
        dto.value = "2018-01-01";

        readField(AtomicConstraintType.ISAFTEROREQUALTOCONSTANTDATETIME, dto, IsAfterOrEqualToConstantDateTimeConstraint.class);
    }

    @Test
    public void shouldReadBeforeConstantDateTimeConstraint() {
        ConstraintDTO dto = new ConstraintDTO();
        dto.field = "test";
        dto.value = "2018-01-01";

        readField(AtomicConstraintType.ISBEFORECONSTANTDATETIME, dto, IsBeforeConstantDateTimeConstraint.class);
    }

    @Test
    public void shouldReadBeforeOrEqualToConstantDateTimeConstraint() {
        ConstraintDTO dto = new ConstraintDTO();
        dto.field = "test";
        dto.value = "2018-01-01";

        readField(AtomicConstraintType.ISBEFOREOREQUALTOCONSTANTDATETIME, dto, IsBeforeOrEqualToConstantDateTimeConstraint.class);
    }

}
