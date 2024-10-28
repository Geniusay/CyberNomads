package io.github.geniusay.crawler.po.bilibili;

import lombok.Data;

import java.util.List;

@Data
public class VideoDetail {
    private int code;
    private String message;
    private int ttl;
    private Data data;

    @lombok.Data
    public static class Data {
        private String bvid;
        private long aid;
        private int videos;
        private int tid;
        private String tname;
        private int copyright;
        private String pic;
        private String title;
        private long pubdate;
        private long ctime;
        private String desc;
        private int state;
        private int duration;
        private Owner owner;
        private Stat stat;
        private Rights rights;
        private List<Page> pages;

        @lombok.Data
        public static class Owner {
            private long mid;
            private String name;
            private String face;
        }

        @lombok.Data
        public static class Stat {
            private long aid;
            private int view;
            private int danmaku;
            private int reply;
            private int favorite;
            private int coin;
            private int share;
            private int like;
        }

        @lombok.Data
        public static class Rights {
            private int bp;
            private int elec;
            private int download;
            private int movie;
            private int pay;
            private int hd5;
            private int no_reprint;
        }

        @lombok.Data
        public static class Page {
            private long cid;
            private int page;
            private String from;
            private String part;
            private int duration;
        }
    }
}