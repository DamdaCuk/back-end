package com.cuk.damda.music.service;

import com.cuk.damda.music.controller.dto.ManiaDBDTO;
import com.cuk.damda.music.controller.dto.MusicSearchReq;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Service
@RequiredArgsConstructor
@Slf4j
public class MusicServiceImpl implements MusicService {

    @Value("music.api.key")
    private String apiKey;

    @Override
    public List<ManiaDBDTO> searchToManiaDB(MusicSearchReq musicSearchReq) {
        String keyword=musicSearchReq.keyword();
        String sr=musicSearchReq.sr();
        URL url = null;
        HttpURLConnection connection;
        try {
            url = new URL("http://www.maniadb.com/api/search/" + keyword + "/?sr=" + sr + "&display=30&key=" + apiKey);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/xml");
            if (connection.getResponseCode() == 200) {
                // XML 파싱
                InputStream xmlStream = connection.getInputStream();
                return parseXML(xmlStream);
            } else {
                log.warn("Failed to get data from API. Response code: "+connection.getResponseCode());
                return null;
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("검색 실패");
        }
    }

    public static List<ManiaDBDTO> parseXML(InputStream xmlStream) throws Exception {
        List<ManiaDBDTO> songs = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(xmlStream);

        NodeList itemList = document.getElementsByTagName("item");

        for (int i = 0; i < itemList.getLength(); i++) {
            Node itemNode = itemList.item(i);
            if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
                Element itemElement = (Element) itemNode;

                // 노래 ID(apiId)
                String songId = itemElement.getAttribute("id");

                // 노래 제목(title)
                String title = getTagValue("title", itemElement);

                // 앨범명(album)
                Element albumElement = (Element) itemElement.getElementsByTagName("maniadb:album").item(0);
                String albumTitle = getTagValue("title", albumElement);

                //앨범 이미지(albumCover)
                String albumCover=getTagValue("image", albumElement);

                //발매날짜(releaseDate)
                String pubDateStr = getTagValue("pubDate", itemElement);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
                ZonedDateTime zonedDateTime=ZonedDateTime.parse(pubDateStr, formatter);
                LocalDate releaseDate=zonedDateTime.toLocalDate();

                // 가수(artist)
                Element artistElement = (Element) itemElement.getElementsByTagName("maniadb:artist").item(0);
                String artistName = getTagValue("name", artistElement);

                // Song 객체 생성 후 리스트에 추가
                songs.add(ManiaDBDTO.from(songId, title, artistName, albumTitle, releaseDate, albumCover));
            }
        }
        return songs;
    }

    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node != null ? node.getNodeValue() : "";
    }
}
