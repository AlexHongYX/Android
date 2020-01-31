package com.example.myfirstkotlinapp.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollectionTest {
    public Collection<String> doSomethingStrangeWithCollection(
            Collection<String> collection
    ) {
        Map<Integer, List<String>> groupsByLength = new HashMap();
        for (String s : collection) {
            List<String> strings = groupsByLength.get(s.length());
            if (strings == null) {
                strings = new ArrayList();
                groupsByLength.put(s.length(), strings);
            }
            strings.add(s);
        }

        int maximumSizeOfGroup = 0;
        for (List<String> group : groupsByLength.values()) {
            if (group.size() > maximumSizeOfGroup) {
                maximumSizeOfGroup = group.size();
            }
        }

        for (List<String> group : groupsByLength.values()) {
            if (group.size() == maximumSizeOfGroup) {
                return group;
            }
        }
        return null;
    }
}
