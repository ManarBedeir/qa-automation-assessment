package com.manarbedeir.qa.ui.tests;

import com.manarbedeir.qa.commons.data.TestData;
import com.manarbedeir.qa.ui.data.DynamicLoadingCase;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class DynamicLoadingTest extends BaseTest {

    @DataProvider(name = "dynamicLoadingCases")
    public Object[][] dynamicLoadingCases() {
        return TestData.asRows(
                TestData.readList("testdata/ui-test-data.json", "dynamicLoading", DynamicLoadingCase.class));
    }

    @Test(dataProvider = "dynamicLoadingCases")
    public void shouldDisplayHelloWorldAfterLoading(DynamicLoadingCase testCase) {
        String text = openHomePage()
                .openDynamicLoading()
                .openExample(testCase.example())
                .start()
                .resultText();

        assertEquals(text, testCase.expectedText(), "Text shown after loading");
    }
}
