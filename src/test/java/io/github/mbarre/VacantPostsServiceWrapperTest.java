package io.github.mbarre;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import io.github.mbarre.drhfpnc.sdk.Category;
import io.github.mbarre.drhfpnc.sdk.Post;
import io.github.mbarre.drhfpnc.sdk.Sector;
import io.github.mbarre.drhfpnc.sdk.VacantPostsServiceWrapper;
import junit.framework.TestCase;

import java.io.IOException;
import java.util.List;

public class VacantPostsServiceWrapperTest extends TestCase {

    public void VacantPostsServiceWrapperTest(){

        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);

    }

    public void testVacantPostsPage() throws IOException {

        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);

        try (final WebClient webClient = new WebClient()) {
            final HtmlPage htmlPage = webClient.getPage(VacantPostsServiceWrapper.BASE_URL + VacantPostsServiceWrapper.VP_URL);
            assertTrue(htmlPage.asText().contains(VacantPostsServiceWrapper.COPYRIGHT));

            HtmlForm form = htmlPage.getHtmlElementById(VacantPostsServiceWrapper.FORM_ID);
            if (form == null)
                throw new RuntimeException("Formulaire introuvable");


            HtmlSelect selectSector = form.getSelectByName(VacantPostsServiceWrapper.SELECT_SECTOR);
            assertEquals(selectSector.getOptions().size(), Sector.listAll().size());
            for (HtmlOption option : selectSector.getOptions()) {
                assertTrue("Unknown sector " + option.getText(), Sector.listAllDescriptions().contains(option.getText()));
            }

            HtmlSelect selectCategory = form.getSelectByName(VacantPostsServiceWrapper.SELECT_CATEGORY);
            assertEquals(selectCategory.getOptions().size(), Category.listAll().size());
                for (HtmlOption categoryOption : selectCategory.getOptions()) {
                    assertTrue("Unknown category " + categoryOption.getText(), Category.listAllDescriptions().contains(categoryOption.getText()));
                }
            }
        }


    public void testFilterAVP() throws IOException {

        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);

        List<Post> posts = VacantPostsServiceWrapper.filterAVP(Sector.TOUTES, Category.TOUTES, null);
        assertNotNull(posts);
        assertTrue(!posts.isEmpty());

    }


}
