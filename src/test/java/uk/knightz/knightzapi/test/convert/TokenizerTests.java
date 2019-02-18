package uk.knightz.knightzapi.test.convert;

import org.junit.Test;
import uk.knightz.knightzapi.menu.adapter.token.ObjectToken;
import uk.knightz.knightzapi.menu.adapter.token.factory.TokenFactory;
import uk.knightz.knightzapi.test.TestData;

public class TokenizerTests {

    @Test
    public void testTokenizer() {
        TestData child = new TestData("Samuel", 17);
        TestData parent = new TestData("David", 42, child);

        ObjectToken<TestData> token = new TokenFactory<TestData>().generate(parent);
        ObjectToken<TestData> childToken = new TokenFactory<TestData>().generate(child);

//        System.out.println(token);
//        System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(token));
    }

}
