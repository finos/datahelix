Copyright 2019 Scott Logic Ltd /
/
Licensed under the Apache License, Version 2.0 (the \"License\");/
you may not use this file except in compliance with the License./
You may obtain a copy of the License at/
/
    http://www.apache.org/licenses/LICENSE-2.0/
/
Unless required by applicable law or agreed to in writing, software/
distributed under the License is distributed on an \"AS IS\" BASIS,/
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied./
See the License for the specific language governing permissions and/
limitations under the License.
package com.scottlogic.deg.generator.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.walker.reductive.IterationVisualiser;
import com.scottlogic.deg.generator.walker.reductive.NoOpIterationVisualiser;
import com.scottlogic.deg.generator.walker.reductive.ReductiveIterationVisualiser;


public class IterationVisualiserProvider implements Provider<IterationVisualiser> {
    private final GenerationConfigSource source;
    private final NoOpIterationVisualiser noOpIterationVisualiser;
    private final ReductiveIterationVisualiser reductiveIterationVisualiser;

    @Inject
    public IterationVisualiserProvider(
        GenerationConfigSource source,
        NoOpIterationVisualiser noOpIterationVisualiser,
        ReductiveIterationVisualiser reductiveIterationVisualiser) {
        this.source = source;
        this.noOpIterationVisualiser = noOpIterationVisualiser;
        this.reductiveIterationVisualiser = reductiveIterationVisualiser;
    }

    @Override
    public IterationVisualiser get() {
        if (source.visualiseReductions()){
            return reductiveIterationVisualiser;
        }

        return noOpIterationVisualiser;
    }
}
