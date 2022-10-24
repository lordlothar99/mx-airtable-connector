package jsonstructure.export;

import com.mendix.core.Core;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.systemwideinterfaces.core.IMendixObject;
import com.mendix.systemwideinterfaces.core.meta.IMetaObject;
import jsonstructure.proxies.JSONObject;

import java.util.List;
import java.util.Objects;

import static jsonstructure.proxies.microflows.Microflows.mergeJSONObjects;

public class MendixObjectExporter {
    private final IContext context;
    private String exportMicroflowPrefix = "JSONExport_";

    public MendixObjectExporter(IContext context) {
        this.context = context;
    }

    public MendixObjectExporter withMicroflowPrefix(String prefix) {
        this.exportMicroflowPrefix = prefix;
        return this;
    }

    public JSONObject exportMendixObject(IMendixObject mendixObject) {
        List<IMetaObject> inheritanceChain = InheritanceChainProvider.getInheritanceChain(mendixObject);

        return inheritanceChain.stream()
                .map(this::calculateExportMicroflowName)
                .map(microflowName -> this.exportMendixObjectUsingMicroflow(mendixObject, microflowName))
                .filter(Objects::nonNull)
                .filter(iMendixObject -> {
                    if (!iMendixObject.getType().equals(JSONObject.getType())) {
                        Core.getLogger("JsonStructure").error(String.format("Export result was of type %s, but it should be of type JSONObject. Ignoring...", iMendixObject.getType()));
                        return false;
                    }

                    return true;
                })
                .map(iMendixObject -> JSONObject.initialize(this.context, iMendixObject))
                .reduce(null, (baseObject, newObject) -> mergeJSONObjects(this.context, baseObject, newObject));
    }

    private IMendixObject exportMendixObjectUsingMicroflow(IMendixObject mendixObject, String microflowName) {
        if (microflowName == null || !MicroflowNameProvider.getMicroflowNames().contains(microflowName))
            return null;

        String entityName = microflowName.replace(exportMicroflowPrefix, "");

        return Core.microflowCall(microflowName).withParam(entityName, mendixObject).execute(context);
    }

    private String calculateExportMicroflowName(IMetaObject iMetaObject) {
        String[] nameParts = iMetaObject.getName().split("\\.");
        String moduleName = nameParts[0];
        String entityName = nameParts[1];
        return String.format("%s.%s%s", moduleName, this.exportMicroflowPrefix, entityName);
    }
}
