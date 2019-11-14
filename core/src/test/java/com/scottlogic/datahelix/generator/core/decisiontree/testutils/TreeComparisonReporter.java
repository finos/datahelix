/*
 * Copyright 2019 Scott Logic Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scottlogic.datahelix.generator.core.decisiontree.testutils;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TreeComparisonReporter {
    public void reportMessages(TreeComparisonContext context) {
        System.out.println(String.format("--- Looking for: %s --- ", context.getExpectedTree()));

        for (TreeComparisonContext.Error error : context.getErrors()){
            writeError(error);
        }

        System.out.println("");
    }


    public void writeError(TreeComparisonContext.Error error){
        System.out.println(" " + error.expected.tree + ": " + this.getExpectedMessage(error));
        System.out.println(" Path: " + getExpectedPath(error.stack));
        System.out.println(" " + error.actual.tree + ": " + this.getActualMessage(error));
        System.out.println(" Path: " + getActualPath(error.stack));
        System.out.println("");
    }

    private String getActualPath(TreeComparisonContext.StackEntry[] stack) {
        return getPathMessage(Arrays.stream(stack).map(se -> se.actual));
    }

    private String getExpectedPath(TreeComparisonContext.StackEntry[] stack) {
        return getPathMessage(Arrays.stream(stack).map(se -> se.expected));
    }

    private String getPathMessage(Stream<Object> path){
        return "\\" + String.join(
            "\\",
            path.map(e -> e == null ? "<null>" : e.getClass().getSimpleName()).collect(Collectors.toList()));
    }

    private String getExpectedMessage(TreeComparisonContext.Error error){
        return getMessage(error.type, "Expected", error.expected.value);
    }

    private String getActualMessage(TreeComparisonContext.Error error){
        return getMessage(error.type, "Got", error.actual.value);
    }

    private String getMessage(TreeComparisonContext.TreeElementType type, String prefix, Object value){
        return String.format("%s %s %s", prefix, type.toString(), value);
    }
}
