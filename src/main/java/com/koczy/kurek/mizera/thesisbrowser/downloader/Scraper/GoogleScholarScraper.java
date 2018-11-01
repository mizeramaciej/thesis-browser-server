package com.koczy.kurek.mizera.thesisbrowser.downloader.Scraper;

import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.koczy.kurek.mizera.thesisbrowser.downloader.Scraper.HTMLScraper.MOZILLA;
import static com.koczy.kurek.mizera.thesisbrowser.downloader.Scraper.HTMLScraper.UTF_8;
import static com.koczy.kurek.mizera.thesisbrowser.model.Constants.SCRAPER_TIMEOUT;
import static com.koczy.kurek.mizera.thesisbrowser.model.Constants.ONLY_NUMBERS;

@Component
public class GoogleScholarScraper {
    private static final Logger logger = Logger.getLogger(GoogleScholarScraper.class.getName());
    private static final String GOOGLE_SCHOLAR_SEARCH_URL = "https://scholar.google.pl/scholar?q=";

    public int getCitationNumber(String authorName, String title){
        String searchUrl = getSearchUrl(authorName, title);
        if(searchUrl.equals("")){
            logger.log(Level.WARNING, "Couldn't get url");
            return 0;
        }
        try {
             return Integer.parseInt(Jsoup.connect(searchUrl)
                     .userAgent(MOZILLA)
                     .timeout(SCRAPER_TIMEOUT)
                     .get()
                     .select("a:contains(Cited by)")
                     .first()
                     .text()
                     .replaceAll(ONLY_NUMBERS,""));
        } catch (IOException e) {
            logger.log(Level.WARNING, e.toString());
            logger.log(Level.WARNING, "Couldn't get citation number");
            return 0;
        }
    }

    private String getSearchUrl(String authorName, String title){
        try {
            return GOOGLE_SCHOLAR_SEARCH_URL + URLEncoder.encode(authorName + " " +title, UTF_8);
        } catch (UnsupportedEncodingException e) {
            logger.log(Level.WARNING, e.toString());
            logger.log(Level.WARNING, "Couldn't create url");
            return "";
        }
    }

}
