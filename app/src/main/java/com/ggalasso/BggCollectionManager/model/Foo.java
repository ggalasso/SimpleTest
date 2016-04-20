package com.ggalasso.BggCollectionManager.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by Edward on 3/10/2016.
 */
public class Foo<T> {
    private T t;

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public <T> ArrayList<T> SelectAll(Class<T> foo){
        ArrayList<T> list = new ArrayList<>();
        Field[] fields = foo.getFields();
        try {
            Constructor<T> constructor = foo.getConstructor(foo);
            list.add(constructor.newInstance(  ));
        }catch(Exception ex){
            return list;
        }
        return null;
    }
}
