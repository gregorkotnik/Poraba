package com.example.gigi.poraba.Utils;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CollectionUtils
{
    public static final <T> List<T> where(List<T> list, Predicate predicate)
    {
        return (List<T>) list.stream().filter(predicate).collect(Collectors.toList());
    }
}
