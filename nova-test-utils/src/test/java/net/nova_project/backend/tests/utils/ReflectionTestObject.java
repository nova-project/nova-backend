package net.nova_project.backend.tests.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class ReflectionTestObject {

    @Getter
    private String name;

    private ReflectionTestObject() {
    }

    private ReflectionTestObject(final String name, final String secondName) {
        this.name = name + secondName;
    }
}
