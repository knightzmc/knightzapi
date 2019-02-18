package uk.knightz.knightzapi.test;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;


@AllArgsConstructor
@RequiredArgsConstructor
public class TestData {
    private final String name;
    private final int age;
    private TestData child;

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public TestData getChild() {
        return child;
    }
}