package com.scottlogic.deg.profile.reader.names;

import java.util.Objects;

public class NameHolder {

    private final String name;

    public NameHolder(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NameHolder that = (NameHolder) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "NameHolder{" +
            "name='" + name + '\'' +
            '}';
    }
}
