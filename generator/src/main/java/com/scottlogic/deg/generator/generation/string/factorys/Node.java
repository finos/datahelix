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

package com.scottlogic.deg.generator.generation.string.factorys;

import java.util.ArrayList;
import java.util.List;

import static com.scottlogic.deg.common.util.NumberUtils.addingNonNegativesIsSafe;
import static com.scottlogic.deg.common.util.NumberUtils.multiplyingNonNegativesIsSafe;

public class Node {
    private int nbrChar = 1;
    private List<Node> nextNodes = new ArrayList<>();
    private boolean isNbrMatchedStringUpdated;
    private long matchedStringIdx = 0;
    private char minChar;
    private char maxChar;

    int getNbrChar() {
        return nbrChar;
    }

    void setNbrChar(int nbrChar) {
        this.nbrChar = nbrChar;
    }

    List<Node> getNextNodes() {
        return nextNodes;
    }

    void setNextNodes(List<Node> nextNodes) {
        this.nextNodes = nextNodes;
    }

    void updateMatchedStringIdx() {
        if (isNbrMatchedStringUpdated) {
            return;
        }
        if (nextNodes.isEmpty()) {
            matchedStringIdx = nbrChar;
        } else {
            for (Node childNode : nextNodes) {
                childNode.updateMatchedStringIdx();
                long childNbrChar = childNode.getMatchedStringIdx();

                if(multiplyingNonNegativesIsSafe(nbrChar, childNbrChar) &&
                    addingNonNegativesIsSafe(matchedStringIdx, nbrChar * childNbrChar)) {
                    matchedStringIdx += nbrChar * childNbrChar;
                } else {
                    matchedStringIdx = Long.MAX_VALUE;
                }
            }
        }
        isNbrMatchedStringUpdated = true;
    }

    long getMatchedStringIdx() {
        return matchedStringIdx;
    }

    char getMinChar() {
        return minChar;
    }

    void setMinChar(char minChar) {
        this.minChar = minChar;
    }

    char getMaxChar() {
        return maxChar;
    }

    void setMaxChar(char maxChar) {
        this.maxChar = maxChar;
    }


}
