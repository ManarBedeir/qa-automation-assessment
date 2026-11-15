package com.manarbedeir.qa.ui.tests;

import com.manarbedeir.qa.commons.data.TestData;
import com.manarbedeir.qa.ui.data.UploadCase;
import com.manarbedeir.qa.ui.pages.UploadedFilePage;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class FileUploadTest extends BaseTest {

    @DataProvider(name = "uploadCases")
    public Object[][] uploadCases() {
        return TestData.asRows(TestData.readList("testdata/ui-test-data.json", "fileUploads", UploadCase.class));
    }

    @Test(dataProvider = "uploadCases")
    public void shouldUploadImageSuccessfully(UploadCase uploadCase) {
        UploadedFilePage result = openHomePage()
                .openFileUpload()
                .selectFile(uploadCase.path())
                .submit();

        assertEquals(result.heading(), uploadCase.expectedHeading(), "Upload confirmation heading");
        assertEquals(result.uploadedFileName(), uploadCase.fileName(), "Uploaded file name");
    }
}
