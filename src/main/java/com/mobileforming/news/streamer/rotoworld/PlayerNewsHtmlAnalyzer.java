package com.mobileforming.news.streamer.rotoworld;

import com.mobileforming.news.streamer.data.model.Player;
import com.mobileforming.news.streamer.data.model.PlayerBuilder;
import com.mobileforming.news.streamer.data.model.PlayerNews;
import com.mobileforming.news.streamer.data.model.PlayerNewsBuilder;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by jeffreypthomas on 9/6/15.
 */
public class PlayerNewsHtmlAnalyzer {

    private static final org.slf4j.Logger LOG =
        org.slf4j.LoggerFactory.getLogger(PlayerNewsHtmlAnalyzer.class);

    public static PlayerNews getPlayerNews(Document playerNewsDetails)
        throws MalformedURLException {


        final Element playerElement = playerNewsDetails.select("div[id$=cp1_pnlPlayer]").first();
        final String[] playerNameWithPosition =
            playerElement.select("div[class$=playername]").text().split("\\|");

        final String playerName = playerNameWithPosition[0].trim();
        final String playerPosition = playerNameWithPosition[1].trim();

        final Element playerTableElement =
            playerElement.getElementById("cp1_ctl00_tblPlayerDetails");
        final String playerTeamName = playerTableElement.select("tbody tr td").eq(1).text();

        final Elements playerNewsElement = playerElement.select("div[class$=playernews]");
        final String newsBody = playerNewsElement.select("div[class$=impact]").first().text();
        final String newsHeadline = playerNewsElement.select("div[class$=report]").first().text();

        final Player player = new PlayerBuilder().setName(playerName).setPosition(playerPosition)
            .setTeam(playerTeamName).createPlayer();

        return new PlayerNewsBuilder().setPlayer(player).setBody(newsBody).setHeadline(newsHeadline)
            .setLink(new URL(playerNewsDetails.baseUri())).createPlayerNews();


    }
}
