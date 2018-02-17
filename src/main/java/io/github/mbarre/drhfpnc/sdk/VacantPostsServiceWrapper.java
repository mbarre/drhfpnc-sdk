package io.github.mbarre.drhfpnc.sdk;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;



public class VacantPostsServiceWrapper {

    public static final String BASE_URL ="https://drhfpnc.gouv.nc/";
    public static final String VP_URL = "avis-vacances-postes-AVP";
    public static final String COPYRIGHT = "Direction des ressources humaines et de la fonction publique de Nouvelle-Calédonie";
    public static final String PAGE_NAME = "Consulter les avis de vacances de poste (AVP)";

    public static final String FORM_ID = "gnc-avp-filter-form";
    public static final String FILTER_BUTTON = "op";
    public static final String AVP_LIST = "avp-list";
    public static final String SELECT_SECTOR = "filiere";
    public static final String SELECT_CATEGORY = "categorie";


    final static Logger logger = LoggerFactory.getLogger(VacantPostsServiceWrapper.class);


    public static WebClient buildWebClient(){
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);

        WebClient webClient = new WebClient(BrowserVersion.BEST_SUPPORTED);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setDownloadImages(false);
        webClient.getOptions().setCssEnabled(false);//if you don't need css
        return webClient;
    }


    public static boolean checkPageisOK(HtmlPage htmlPage){

        if(!htmlPage.asText().contains(COPYRIGHT) || !htmlPage.asText().contains(PAGE_NAME))
            return false;

        return true;
    }


    /**
     * Get all job offers having specified sector, category and keywords
     * @param sector
     * @param category
     * @param keywords
     * @throws IOException
     */
    public static List<Post> filterAVP(Sector sector, Category category, List<String> keywords) throws IOException {

        WebClient webClient = buildWebClient();
        String url = BASE_URL + VP_URL;
        HtmlPage htmlPage = webClient.getPage(url);

        if(!checkPageisOK(htmlPage))
            throw new RuntimeException("Page incorrecte.");

        HtmlForm form = htmlPage.getHtmlElementById(FORM_ID);
        if (form == null)
            throw new RuntimeException("Formulaire introuvable");


        HtmlSelect selectFiliere = form.getSelectByName(SELECT_SECTOR);
        for (HtmlOption option : selectFiliere.getOptions()) {
            if (option.getText().equalsIgnoreCase(sector.toString())) {
                selectFiliere.setSelectedAttribute(option, true);
            }
        }

        HtmlSelect selectCategorie = form.getSelectByName(SELECT_CATEGORY);
        for (HtmlOption option : selectCategorie.getOptions()) {
            if (option.getText().equalsIgnoreCase(category.toString())) {
                selectCategorie.setSelectedAttribute(option, true);
            }
        }

        HtmlButton filterButton = form.getButtonByName(FILTER_BUTTON);
        HtmlPage resultPage = filterButton.click();

        List<Post> allPostes = new ArrayList<>();
        List<HtmlElement> listeCategories = resultPage.getElementById(AVP_LIST).getElementsByTagName("div");//table-responsive list

        for (HtmlElement posteCategorie : listeCategories) {

            HtmlTable listePoste = (HtmlTable) posteCategorie.getElementsByTagName("table").get(0);
            String filiereCaption = listePoste.getCaptionText();

            for (final HtmlTableBody body : listePoste.getBodies()) {
                final List<HtmlTableRow> rows = body.getRows();
                // now fetch each row
                Iterator<HtmlTableRow> rowIter = rows.iterator();
                HtmlTableRow theRow;
                Post poste;

                while (rowIter.hasNext()) {
                    theRow = rowIter.next();
                    poste = new Post();
                    poste.setCadre(theRow.getCell(0).asText());
                    poste.setPost(theRow.getCell(1).asText());
                    poste.setUrlPdf(theRow.getAttribute("data-pdf"));
                    poste.setCategory(theRow.getCell(2).asText());
                    poste.setEmployer(theRow.getCell(3).asText());
                    poste.setDirection(theRow.getCell(4).asText());
                    poste.setSector(filiereCaption);
                    try {
                        poste.setClosureDate(convertFromTextToDate(theRow.getCell(5).asText()));
                    } catch (ParseException e) {
                        logger.error("unable to convert " + theRow.getCell(5).asText() + " to date.");
                    }

                    if(keywords != null && keywords.size() != 0) {
                        for (String keyword : keywords) {
                            if (poste.getPost().toLowerCase().contains(keyword.toLowerCase())) {
                                allPostes.add(poste);
                                logger.debug("Added new post : " + poste.toString());
                                break;
                            }
                        }
                    }else {
                        allPostes.add(poste);
                        logger.debug("Added new post : " + poste.toString());
                    }

                }
            }
        }
        return allPostes;
    }

    private final static Date convertFromTextToDate(String dateInString) throws ParseException {
        // input format expected : DD-MM-YYYY
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date = formatter.parse(dateInString);
            return date;
        } catch (ParseException ex) {
            logger.error("Unable to parse date <" + dateInString + "> to date : " + ex.getMessage());
            throw ex;
        }
    }

}
