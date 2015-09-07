package com.mobileforming.news.streamer.tools;

import com.mobileforming.news.streamer.data.model.Player;
import com.mobileforming.news.streamer.data.model.PlayerNews;
import com.mobileforming.news.streamer.rotoworld.PlayerNewsHtmlAnalyzer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import static org.junit.Assert.*;

/**
 * Created by jeffreypthomas on 9/6/15.
 */
public class PlayerNewsHtmlAnalyzerTest {


    private Resource playerNewsResource = new ClassPathResource("player_news.html");
    private Document playerNewsDocument;

    @Before public void setUp() throws Exception {

        playerNewsDocument = Jsoup.parse(playerNewsResource.getInputStream(), "UTF-8",
            "http://www.rotoworld.com/player/nfl/4242/james-jones");

    }

    @Test public void testGetPlayerNews() throws Exception {

        final String url = "http://www.rotoworld.com/player/nfl/4242/james-jones";
        final PlayerNews playerNews = PlayerNewsHtmlAnalyzer.getPlayerNews(playerNewsDocument);

        assertNotNull(playerNews);
        final Player player = playerNews.getPlayer();
        assertNotNull(player);
        assertEquals("James Jones", player.getName());
        assertEquals("Wide Receiver", player.getPosition());
        assertEquals("Unsigned Free Agent", player.getTeam());

        assertEquals("Free agent WR James Jones is visiting the Packers on Sunday.",
            playerNews.getHeadline());
        assertEquals(
            "The Packers know Jones better than any team in the league, so a visit could quickly turn into a signing. The Pack clearly aren't comfortable heading into the season with just Ty Montgomery and Jeff Janis behind Randall Cobb and Davante Adams. Jones' game is in steep decline, but he's always had a nice chemistry with Aaron Rodgers. He wouldn't be on the WR3 radar, but could definitely help the Pack. Sep 6 - 2:35 PM",
            playerNews.getBody());
        assertEquals(url, playerNews.getLink().toString());

    }
}
