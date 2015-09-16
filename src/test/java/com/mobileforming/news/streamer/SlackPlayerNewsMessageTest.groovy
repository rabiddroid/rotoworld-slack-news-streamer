package com.mobileforming.news.streamer

import com.mobileforming.news.streamer.data.model.Player
import com.mobileforming.news.streamer.data.model.PlayerBuilder
import com.mobileforming.news.streamer.data.model.PlayerNews
import com.mobileforming.news.streamer.data.model.PlayerNewsBuilder
import com.mobileforming.news.streamer.slack.PlayerNewsSlackMessageTransformer
import org.skyscreamer.jsonassert.JSONAssert

/**
 * Created by jeffreypthomas on 9/7/15.
 */
class SlackPlayerNewsMessageTest extends spock.lang.Specification {

    private PlayerNews playerNews;
    private String expectedSlackMessage = "{\n" +
            "    \"channel\": \"@jeffrey\",\n" +
            "    \"username\": \"RotoBot\",\n" +
            "    \"text\": \"*_ Test Headline _*\\n test body \\n <http://test.com|Click here>\",\n" +
            "    \"icon_emoji\": \":football:\"\n" +
            "}";

    void setup() {

        Player player = new PlayerBuilder().setName("Jonas Grey").setPosition("Running Back").setTeam("New England Patriots").createPlayer();
        playerNews = new PlayerNewsBuilder().setHeadline("Test Headline").setBody("test body").setLink(new URL("http://test.com")).setPlayer(player).createPlayerNews();


    }

    def "GetMessage"() {


        setup:
        PlayerNewsSlackMessageTransformer slackPlayerNewsMessage = new PlayerNewsSlackMessageTransformer(playerNews);

        when:
        String actualSlackMessage = slackPlayerNewsMessage.getMessage();

        then:
        actualSlackMessage != null;
        actualSlackMessage.length() > 0;

        JSONAssert.assertEquals(expectedSlackMessage, actualSlackMessage,true);


    }
}
