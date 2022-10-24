package jsonstructure.export;

import com.mendix.core.Core;

import java.util.Set;

public class MicroflowNameProvider {
    private static Set<String> microflowNames;

    public static Set<String> getMicroflowNames() {
        if (MicroflowNameProvider.microflowNames == null) {
            MicroflowNameProvider.microflowNames = Core.getMicroflowNames();
        }

        return MicroflowNameProvider.microflowNames;
    }
}