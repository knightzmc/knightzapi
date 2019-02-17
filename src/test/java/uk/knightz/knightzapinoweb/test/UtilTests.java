/*
 * MIT License
 *
 * Copyright (c) 2018 Alexander Leslie John Wood
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package uk.knightz.knightzapinoweb.test;

import com.google.gson.Gson;
import org.junit.Test;
import uk.knightz.knightzapi.utils.*;

import java.util.*;

import static junit.framework.Assert.assertEquals;

public class UtilTests {
    @Test
    public void testAllUtils() {
        testListBuilder();
        testEnumUtils();
        testFunctions();
        testJsonUtils();
        testMapUtils();
        testMathUtils();
    }

    private void testMathUtils() {
        double test_d = 0.0013;
        assertEquals(0.00, MathUtils.truncateDouble(test_d));
        double test_d_2 = 0.1345;
        assertEquals(0.13, MathUtils.truncateDouble(test_d_2));


        int size = 12;
        assertEquals(18, MathUtils.roundUp(size));
    }

    private void testMapUtils() {
        Map<String, Tests> map = new HashMap<>();
        map.put("1", Tests.TEST_ENUM_1);
        map.put("2", Tests.TEST_ENUM_2);
        map.put("22", Tests.TEST_ENUM_2);
        map.put("3", Tests.TEST_ENUM_3);
        assertEquals("2", MapUtils.firstKeyOf(map, Tests.TEST_ENUM_2));
    }

    private void testJsonUtils() {
        String json = new Gson().toJson(new Struct<>(Tests.TEST_ENUM_1, "lol"));
        assertEquals("{\n" +
                "  \"a\": \"TEST_ENUM_1\",\n" +
                "  \"b\": \"lol\"\n" +
                "}", JsonUtils.prettifyJson(json));
    }

    public void testListBuilder() throws AssertionError {
        List<String> testList = Arrays.asList("Test 1", "Test 2", "Test 3");
        ListBuilder<String> builder = new ListBuilder<>();
        testList.forEach(builder::append);
        assertEquals(testList, builder.toList());

        List<String> testList2 = Arrays.asList("Test 1", "Test 2");
        assertEquals(testList2, builder.backspace().toList());
    }

    public void testEnumUtils() throws AssertionError {
        assertEquals("Test enum 1", EnumUtils.getFriendlyName(Tests.TEST_ENUM_1));
        assertEquals(EnumSet.allOf(Tests.class), EnumUtils.getRandom(Tests.class, 100));
        assertEquals(Tests.TEST_ENUM_1, EnumUtils.getRandom(Tests.class, Tests.TEST_ENUM_2, Tests.TEST_ENUM_3));
    }

    public void testFunctions() {
        assertEquals("lol", Functions.emptyFunction("lol").apply(null));
    }

    private enum Tests {
        TEST_ENUM_1,
        TEST_ENUM_2,
        TEST_ENUM_3,
    }

}
