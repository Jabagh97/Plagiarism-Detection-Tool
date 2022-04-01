package com.company.HTML;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Parser {

    public String getText() throws IOException {
        Document doc = Jsoup.parse(new File("result.html"), "utf-8");
        Elements divTag = doc.select("TABLE");
        String outPut = divTag.text();
        outPut = outPut.substring(28);
        return outPut;
    }
    public List<String> getFilesLink() throws IOException {
        Document doc = Jsoup.parse(new File("result.html"), "utf-8");
        Elements link = doc.select("A");
        String relHref = link.attr("href"); // == "/"
        List<String> absHref = link.eachAttr("href");
        for(int i = 1 ; i<7 ;i++){ absHref.remove(0);}
        return absHref;
    }

}