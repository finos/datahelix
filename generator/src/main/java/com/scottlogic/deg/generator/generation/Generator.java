package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.outputs.TestCaseDataRow;
import com.scottlogic.deg.generator.utils.ProjectingIterable;

import java.util.stream.Collectors;

public class Generator implements IGenerator {
    @Override
    public Iterable<TestCaseDataRow> generateData(
        ProfileFields fields,
        IDataBagSource dataBagSource,
        GenerationConfig generationConfig) {

        return new ProjectingIterable<>(
            dataBagSource.generate(generationConfig),
            dataBag -> new TestCaseDataRow(
                fields.stream()
                    .map(dataBag::get)
                    .collect(Collectors.toList())));

//        // Populate rowData with all of the possible values for each field.
//        List<List<Object>> rowData = new ArrayList<>();
//        for (Field field : dataBagSource.getFields()) {
//            FieldSpec fieldSpec = dataBagSource.getSpecForField(field);
//            FieldSpecFulfiller fulfiller = new FieldSpecFulfiller(fieldSpec, generationConfig);
//            List<Object> fieldPossibilities = new ArrayList<>();
//            IFieldSpecIterator fulfilmentIterator = (IFieldSpecIterator)fulfiller.iterator();
//            if (fulfilmentIterator.isInfinite()) {
//                fieldPossibilities.add(fulfilmentIterator.next());
//            }
//            else {
//                while (fulfilmentIterator.hasNext()) {
//                    fieldPossibilities.add(fulfilmentIterator.next());
//                }
//            }
//            rowData.add(fieldPossibilities);
//        }
//
//        // Permute the members of RowData to generate the possible rows.
//        ArrayList<TestCaseDataRow> output = new ArrayList<>();
//        int[] indices = new int[rowData.size()];
//        int[] maxima = new int[rowData.size()];
//        for (int i = 0; i < rowData.size(); ++i) {
//            indices[i] = 0;
//            maxima[i] = rowData.get(i).size();
//
//            // if any fields have zero possible values, we cannot generate a valid row.
//            if (maxima[i] == 0)
//                return output;
//        }
//        do {
//            output.add(generateRowFrom(rowData, indices));
//        } while (incrementIndices(indices, maxima));
//
//        return output;
    }

//    private TestCaseDataRow generateRowFrom(List<List<Object>> rowData, int[] indices) {
//        ArrayList<Object> row = new ArrayList<>();
//        for (int i = 0; i < rowData.size(); ++i) {
//            row.add(rowData.get(i).get(indices[i]));
//        }
//        return new TestCaseDataRow(row);
//    }
//
//    private boolean incrementIndices(int[] indices, int[] maxima) {
//        int pos = indices.length - 1;
//        do {
//            indices[pos]++;
//            if (indices[pos] < maxima[pos])
//                return true;
//            indices[pos] = 0;
//            pos--;
//        } while (pos >= 0);
//        return false;
//    }
}
