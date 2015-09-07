package com.mobileforming.news.streamer.rotoworld;

import com.fasterxml.jackson.databind.JsonNode;
import com.mobileforming.news.streamer.data.model.PlayerNews;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.integration.transformer.GenericTransformer;

import java.io.IOException;

/**
 * Created by jeffreypthomas on 9/7/15.
 */
public class RotoWorldNewsEnricher implements GenericTransformer<JsonNode, PlayerNews> {

    private static final org.slf4j.Logger LOG =
        org.slf4j.LoggerFactory.getLogger(RotoWorldNewsEnricher.class);

    @Override public PlayerNews transform(JsonNode jsonNode) {

        final String url = jsonNode.get("link").asText();

        try {
            final Document playerNewsDetails = Jsoup.connect(url).get();
            return PlayerNewsHtmlAnalyzer.getPlayerNews(playerNewsDetails);
        } catch (IOException e) {
            LOG.error("Unable to get player news details", e);
            return PlayerNews.empty();
        }

    }

}
