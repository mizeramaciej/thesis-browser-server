package com.koczy.kurek.mizera.thesisbrowser.downloader.Scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public interface HTMLScraper {

    String MOZILLA = "Mozilla/5.0";
    String UTF_8 = "UTF-8";
    String PDF = "pdf";

    default String findDownloadPdfLink(String url){
        Document doc = null;
        try {
            doc = Jsoup.connect(url).userAgent(MOZILLA).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(Objects.isNull(doc))
            return null;

        for (Element link : doc.select("a[href]")) {
            String downloadPdfLink = link.attr("abs:href");
            String urlText = link.text();
            if(urlText.toLowerCase().contains(PDF) || downloadPdfLink.toLowerCase().contains(PDF)){
                return downloadPdfLink;
            }
        }
        return null;
    }

    String findUrlToPdf(String pdfName);

    ArrayList<String> getListOfPublicationsByName(String firstName, String lastName);
}