package jsonstructure.export;

import com.mendix.core.Core;
import com.mendix.systemwideinterfaces.core.IMendixObject;
import com.mendix.systemwideinterfaces.core.meta.IMetaObject;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InheritanceChainProvider {
    private static Map<String, IMetaObject> metaObjectMap = new HashMap<>();
    private static Map<String, List<IMetaObject>> inheritanceChainMap = new HashMap<>();

    private static IMetaObject getMetaObject(IMendixObject mendixObject) {
        String startingType = mendixObject.getType();

        if (!InheritanceChainProvider.metaObjectMap.containsKey(startingType)) {
            InheritanceChainProvider.metaObjectMap.put(startingType, Core.getMetaObject(startingType));
        }

        return InheritanceChainProvider.metaObjectMap.get(startingType);
    }
    public static List<IMetaObject> getInheritanceChain(IMendixObject mendixObject) {
        String startingType = mendixObject.getType();

        if (!InheritanceChainProvider.inheritanceChainMap.containsKey(startingType)) {
            IMetaObject baseMetaObject = InheritanceChainProvider.getMetaObject(mendixObject);

            List<IMetaObject> inheritanceChain = Stream.concat(Stream.of(baseMetaObject), baseMetaObject.getSuperObjects().stream())
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            Collections.reverse(inheritanceChain);

            InheritanceChainProvider.inheritanceChainMap.put(startingType, inheritanceChain);
        };

        return InheritanceChainProvider.inheritanceChainMap.get(startingType);
    }
}
