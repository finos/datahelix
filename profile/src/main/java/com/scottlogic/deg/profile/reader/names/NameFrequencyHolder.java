package com.scottlogic.deg.profile.reader.names;

import java.util.Objects;

public class NameFrequencyHolder {

    private final String name;

    private final int frequency;

    public NameFrequencyHolder(String name, int frequency) {
        this.name = name;
        this.frequency = frequency;
    }

    public String getName() {
        return name;
    }

    public int getFrequency() {
        return frequency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NameFrequencyHolder that = (NameFrequencyHolder) o;
        return frequency == that.frequency &&
            Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, frequency);
    }

    @Override
    public String toString() {
        return "NameFrequencyHolder{" +
            "name='" + name + '\'' +
            ", frequency=" + frequency +
            '}';
    }
}
