package uk.knightz.knightzapi.menuold;

import lombok.Data;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Material;
import org.junit.Test;
import uk.knightz.knightzapi.utils.EnumUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class CollectionToMenuAdapterTest {

    @Test
    public void generateMenu() {
        final List<TestObject> testObjects = new ArrayList<>();

        Random r = ThreadLocalRandom.current();
        for (int i = 0; i < 5; i++) {
            String randomString1 = RandomStringUtils.randomAscii(5);
            String randomString2 = RandomStringUtils.randomAscii(5);
            Material randomMaterial = EnumUtils.getRandom(Material.class);
            int randInt = r.nextInt();

            testObjects.add(new TestObject(randomString1, randomMaterial, new TestObject.SubClass(randomString2, randInt)));
        }

        testObjects.forEach(System.out::println);

    }


    @Data
    public static class TestObject {
        private final String name;
        private final Material type;
        private final SubClass subClass;

        @Data
        public static class SubClass {
            private final String subClassString;
            private final int amount;
        }
    }
}