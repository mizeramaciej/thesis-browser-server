package com.koczy.kurek.mizera.thesisbrowser.downloader.HTTPRequest;

import com.koczy.kurek.mizera.thesisbrowser.service.DownloadService;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class HTTPRequest {

    private static final Logger logger = Logger.getLogger(DownloadService.class.getName());

    public HTTPRequest() {
    }

    public InputStream getInputStreamFromPostRequest(String urlString, String urlParametersString) throws IOException {
        byte[] postData = urlParametersString.getBytes(StandardCharsets.UTF_8);

        URL url = new URL(urlString);
        HttpURLConnection httpURLconnection = (HttpURLConnection) url.openConnection();

        httpURLconnection.setDoOutput(true);
        httpURLconnection.setRequestMethod("POST");
        httpURLconnection.setRequestProperty("User-Agent", "Java client");
        httpURLconnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        try (DataOutputStream wr = new DataOutputStream(httpURLconnection.getOutputStream())) {
            wr.write(postData);
        }

        return httpURLconnection.getInputStream();
    }

    public String getPageContentFromInputStream(InputStream inputStream){
        StringBuilder content = null;

        try (BufferedReader in = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            content = new StringBuilder();

            while ((line = in.readLine()) != null) {
                content.append(line);
                content.append(System.lineSeparator());
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, e.toString());
        }
        return content.toString();
    }
}
