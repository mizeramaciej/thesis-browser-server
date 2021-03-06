package com.koczy.kurek.mizera.thesisbrowser.downloader.Scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.koczy.kurek.mizera.thesisbrowser.downloader.Scraper.HTMLScraper.MOZILLA;
import static com.koczy.kurek.mizera.thesisbrowser.downloader.Scraper.HTMLScraper.UTF_8;
import static com.koczy.kurek.mizera.thesisbrowser.model.Constants.ONLY_NUMBERS;
import static com.koczy.kurek.mizera.thesisbrowser.model.Constants.SCRAPER_TIMEOUT;

@Component
public class GoogleScholarScraper {
    private static final Logger logger = Logger.getLogger(GoogleScholarScraper.class.getName());
    private static final String GOOGLE_SCHOLAR_SEARCH_URL = "https://scholar.google.pl/scholar?hl=en&as_sdt=0%2C5&q=";
    private static final int YEAR = 10000;

    public int getCitationNumber(String authorName, String title) {
        String searchUrl = getSearchUrl(authorName, title);
        if (searchUrl.equals("")) {
            logger.log(Level.WARNING, "Couldn't get url for author: " + authorName + ", title: " + title);
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
                    .replaceAll(ONLY_NUMBERS, ""));
        } catch (Exception e) {
            logger.log(Level.WARNING, "Couldn't get citation number for author: " + authorName + ", title: " + title);
            return 0;
        }
    }
    public Integer getPublicationDate(String exampleAuthor, String title){
        String searchUrl = getSearchUrl(exampleAuthor, title);
        if (searchUrl.equals("")) {
            logger.log(Level.WARNING, "Couldn't get url for author: " + exampleAuthor + ", title: " + title);
            return 0;
        }
        try {
            return Integer.parseInt(Jsoup.connect(searchUrl)
                    .userAgent(MOZILLA)
                    .timeout(SCRAPER_TIMEOUT)
                    .get()
                    .select(".gs_a")
                    .first()
                    .text()
                    .replaceAll(ONLY_NUMBERS, ""))%YEAR;
        } catch (Exception e) {
            logger.log(Level.WARNING, "Couldn't get citation number for author: " + exampleAuthor + ", title: " + title);
            return 0;
        }

    }

    public List<String> getRelatedTheses(String authorName, String title) {
        int pagesNum = getCitationNumber(authorName, title) / 10 + 1;

        ArrayList<String> relatedTheses = new ArrayList<>();
        for (int pageNum = 0; pageNum < pagesNum; pageNum++) {
            String relatedArticlesPageUrl = getRelatedArticlesUrlFromPage(authorName, title, pageNum);
            relatedTheses.addAll(getRelatedThesesTitles(relatedArticlesPageUrl));
        }
        return relatedTheses;
    }

    private Set<String> getRelatedThesesTitles(String relatedArticlesPageUrl) {
        try {
            return Jsoup.connect(relatedArticlesPageUrl)
                    .userAgent(MOZILLA)
                    .timeout(SCRAPER_TIMEOUT)
                    .get()
                    .select("h3 > a")
                    .stream()
                    .map(Element::text)
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            logger.log(Level.WARNING, "Couldn't get related theses from " + relatedArticlesPageUrl);
            return Collections.emptySet();
        }
    }

    private String getRelatedArticlesUrlFromPage(String authorName, String title, int pageNum) {
        String searchUrl = getSearchUrl(authorName, title);
        if (searchUrl.equals("")) {
            logger.log(Level.WARNING, "Couldn't get url from author: " + authorName + ", title: " + title);
            return "";
        }
        try {
            return Jsoup.connect(searchUrl)
                    .userAgent(MOZILLA)
                    .timeout(SCRAPER_TIMEOUT)
                    .get()
                    .select("a:contains(Cited by)")
                    .first()
                    .attr("abs:href")
                    .concat("&start=" + pageNum * 10);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Couldn't get cited by url for author: " + authorName + ", title: " + title);
            return "";
        }
    }

    private String getSearchUrl(String authorName, String title) {
        try {
            return GOOGLE_SCHOLAR_SEARCH_URL + URLEncoder.encode(authorName + " " + title, UTF_8);
        } catch (UnsupportedEncodingException e) {
            logger.log(Level.WARNING, "Couldn't create url for author: " + authorName + ", title: " + title);
            return "";
        }
    }

}
