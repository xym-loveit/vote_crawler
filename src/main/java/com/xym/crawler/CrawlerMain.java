package com.xym.crawler;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @version v1.0
 * @type：CrawlerMain.java
 * @description：TODO
 * @author：xym
 * @date：2020/4/1 17:12
 */
public class CrawlerMain {

    private static final String ROOT_WEBSITE = "https://www.moh.cc/";
    /**
     * 最佳社区
     */
    private static final String COMMUNITYURL = "https://www.moh.cc/user/voteimg/vote_log/id/14574303/token/fbdido1582903487/type/baobao/auth_type/1.html"
            .concat("?page=1");
    /**
     * 最美社区守护者
     */
    private static final String PROTECTORURL = "https://www.moh.cc/user/voteimg/vote_log/id/14574302/token/fbdido1582903487/type/baobao/auth_type/1.html".
            concat("?page=1");
    /**
     * 最美环保卫士
     */
    private static final String ENV_URL = "https://www.moh.cc/user/voteimg/vote_log/id/14574301/token/fbdido1582903487/type/baobao/auth_type/1.html".
            concat("?page=1");
    /**
     * 最美物业服务者
     */
    private static final String WY_URL = "https://www.moh.cc/user/voteimg/vote_log/id/14573715/token/fbdido1582903487/type/baobao/auth_type/1.html".
            concat("?page=1");

    /**
     * cookies
     */
    private static final Map COOKIES = Dict.create().set("logged_user", "isLd0Lz1aEMS2JyUSFxOvsY34").
            set("PHPSESSID", "boadah575r18ipechumlt3lp15").set("Hm_lpvt_6fd3d7369c7be3d9a141e9798c4bc66c", "1585744314");

    //private static final String[] URLS = new String[]{COMMUNITYURL, PROTECTORURL, ENV_URL, WY_URL};

    public static void main(String[] args) {
        CrawlerItem item1 = CrawlerItem.builder().title("最佳服务小区").type(1).url(COMMUNITYURL).build();
        CrawlerItem item2 = CrawlerItem.builder().title("最美社区守护者").type(2).url(PROTECTORURL).build();
        CrawlerItem item3 = CrawlerItem.builder().title("最美环保卫士").type(3).url(ENV_URL).build();
        CrawlerItem item4 = CrawlerItem.builder().title("最美物业服务者").type(4).url(WY_URL).build();
        ExcelWriter write = EasyExcel.write(FileUtil.newFile("最美物业人-" + DateUtil.format(LocalDateTime.now(), "(yyyy-MM-dd-HHmmss)") + ".xlsx")).build();
        AtomicInteger i = new AtomicInteger(0);
        Arrays.asList(item1, item2, item3, item4).stream().forEach(item -> {
            try {
                Document root = Jsoup.connect(item.getUrl()).cookies(COOKIES).get();
                ArrayList<Object> dataStore = new ArrayList<>();
                paraseBody(root, item, dataStore);
                Class clazz = item.getType() != 1 ? PersonModel.class : CommunityModel.class;
                WriteSheet sheet = EasyExcel.writerSheet(i.getAndIncrement(), item.getTitle()).head(clazz).build();
                //WriteSheet sheet = new WriteSheet();
                //sheet.setSheetNo(i.getAndIncrement());
                //sheet.setSheetName();
                write.write(dataStore, sheet);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        write.finish();
        //System.out.println(table);
        //root.getElementsByClass("getAllVoteNum").forEach(element -> {
        //    System.out.println(element.text());
        //});
        //System.out.println(getAllVoteNum);
        //System.out.println(root.body(s));
    }

    private static void paraseBody(Document root, CrawlerItem item, List<Object> dataStore) throws IOException {
        Elements trs = root.getElementsByTag("tbody");
        //分页
        Element pagination = root.select(".pagination").first();
        if (trs.size() > 0) {
            trs.first().select("tr").not(":first-child").forEach(el -> {
                //System.out.println(el);
                String no = el.select("td").get(0).text();
                String name = el.select("td").get(1).text();
                String voties = el.select(".getAllVoteNum").text();
                System.out.println("name=" + name + ",voties=" + voties);
                if (item.getType() == 1) {
                    dataStore.add(CommunityModel.builder().name(name).votes(Integer.parseInt(voties)).no(no).build());
                } else {
                    dataStore.add(PersonModel.builder().name(name).votes(Integer.parseInt(voties)).no(no).build());
                }
            });
        }
        if (pagination != null) {
            Element li = pagination.select("li").last();
            //说明到了
            if (li != null && StrUtil.isNotEmpty(li.attr("class")) && "disabled".equals(li.attr("class"))) {
                return;
            }
            //System.out.println(href);
            Document document = Jsoup.connect(li.selectFirst("a").attr("abs:href")).cookies(COOKIES).get();
            paraseBody(document, item, dataStore);
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    static class CrawlerItem {
        private String url;
        private String title;
        private int type;


        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

    }

}
