package com.company.HTML;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;

public class Parser {

    public String run() throws IOException {
        Document doc = Jsoup.parse(new File("result.html"), "utf-8");
        Elements divTag = doc.select("TABLE");
        String outPut = divTag.text();
        outPut = outPut.substring(28);
        return outPut;
    }

}