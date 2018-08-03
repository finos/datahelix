package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.outputs.TestCaseDataRow;
import com.scottlogic.deg.generator.restrictions.FieldSpec;
import com.scottlogic.deg.generator.restrictions.RowSpec;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Generator implements IGenerator {
    @Override
    public Collection<TestCaseDataRow> generateData(RowSpec spec) {
        return generateData(spec, GenerationStrategy.Exhaustive);
    }

    @Override
    public Collection<TestCaseDataRow> generateData(RowSpec spec, GenerationStrategy strategy) {
        // Populate rowData with all of the possible values for each field.
        List<List<Object>> rowData = new ArrayList<>();
        for (Field field : spec.getFields()) {
            FieldSpec fieldSpec = spec.getSpecForField(field);
            FieldSpecFulfiller fulfiller = new FieldSpecFulfiller(fieldSpec, strategy);
            List<Object> fieldPossibilities = new ArrayList<>();
            IFieldSpecIterator fulfilmentIterator = (IFieldSpecIterator)fulfiller.iterator();
            if (fulfilmentIterator.isInfinite()) {
                fieldPossibilities.add(fulfilmentIterator.next());
            }
            else {
                while (fulfilmentIterator.hasNext()) {
                    fieldPossibilities.add(fulfilmentIterator.next());
                }
            }
            rowData.add(fieldPossibilities);
        }

        // Permute the members of RowData to generate the possible rows.
        ArrayList<TestCaseDataRow> output = new ArrayList<>();
        int[] indices = new int[rowData.size()];
        int[] maxima = new int[rowData.size()];
        for (int i = 0; i < rowData.size(); ++i) {
            indices[i] = 0;
            maxima[i] = rowData.get(i).size();

            // if any fields have zero possible values, we cannot generate a valid row.
            if (maxima[i] == 0)
                return output;
        }
        do {
            output.add(generateRowFrom(rowData, indices));
        } while (incrementIndices(indices, maxima));

        return output;
    }

    private TestCaseDataRow generateRowFrom(List<List<Object>> rowData, int[] indices) {
        ArrayList<Object> row = new ArrayList<>();
        for (int i = 0; i < rowData.size(); ++i) {
            row.add(rowData.get(i).get(indices[i]));
        }
        return new TestCaseDataRow(row);
    }

    private boolean incrementIndices(int[] indices, int[] maxima) {
        int pos = indices.length - 1;
        do {
            indices[pos]++;
            if (indices[pos] < maxima[pos])
                return true;
            indices[pos] = 0;
            pos--;
        } while (pos >= 0);
        return false;
    }
}
