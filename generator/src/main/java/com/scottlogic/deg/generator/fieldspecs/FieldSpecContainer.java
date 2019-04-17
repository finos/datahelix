package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.walker.reductive.Merged;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FieldSpecContainer {
    FieldSpecMerger fieldSpecMerger = new FieldSpecMerger();

    Map<Field, FieldSpec> fieldSpecMap;

    public FieldSpecContainer(){
        fieldSpecMap = new HashMap<>();
    }

    public FieldSpecContainer(Map<Field, FieldSpec> map) {
        this.fieldSpecMap = map;
    }

    public FieldSpec get(Field field){
        return fieldSpecMap.get(field);
    }

    public Merged<FieldSpecContainer> addOrUpdate(Field field, FieldSpec fieldSpec){
        if (fieldSpecMap.containsKey(field)){
            return update(field, fieldSpec);
        }
        return Merged.of(add(field, fieldSpec));
    }

    public FieldSpecContainer add(Field field, FieldSpec fieldSpec){
        if (fieldSpecMap.containsKey(field)){
            throw new UnsupportedOperationException("already contains field");
        }

        Map<Field, FieldSpec> newMap = new HashMap<>(fieldSpecMap);
        newMap.put(field, fieldSpec);
        return new FieldSpecContainer(newMap);
    }

    public Merged<FieldSpecContainer> update(Field field, FieldSpec fieldSpec){
        if (!fieldSpecMap.containsKey(field)){
            throw new UnsupportedOperationException("doesn't contain field");
        }

        FieldSpec original = fieldSpecMap.get(field);
        Optional<FieldSpec> merge = fieldSpecMerger.merge(original, fieldSpec);
        if (!merge.isPresent()){
            return Merged.contradictory();
        }

        Map<Field, FieldSpec> newMap = new HashMap<>(fieldSpecMap);
        newMap.put(field, merge.get());

        return Merged.of(new FieldSpecContainer(newMap));
    }


}
