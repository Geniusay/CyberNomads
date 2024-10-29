package io.github.geniusay.crawler.po.bilibili;

import com.google.gson.JsonObject;
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

    /**
     * 从JsonObject中构建一个VideoDetail对象
     *
     * @param video JsonObject表示的视频数据
     * @return VideoDetail对象
     */
    public static VideoDetail fromJson(JsonObject video) {
        VideoDetail videoDetail = new VideoDetail();
        Data videoData = new Data();

        // 填充视频基本信息
        videoData.setBvid(video.get("bvid").getAsString());
        videoData.setAid(video.get("aid").getAsLong());
        videoData.setVideos(video.get("videos").getAsInt());
        videoData.setTid(video.get("tid").getAsInt());
        videoData.setTname(video.get("tname").getAsString());
        videoData.setCopyright(video.get("copyright").getAsInt());
        videoData.setPic(video.get("pic").getAsString());
        videoData.setTitle(video.get("title").getAsString());
        videoData.setPubdate(video.get("pubdate").getAsLong());
        videoData.setCtime(video.get("ctime").getAsLong());
        videoData.setDesc(video.get("desc").getAsString());
        videoData.setState(video.get("state").getAsInt());
        videoData.setDuration(video.get("duration").getAsInt());

        // 填充Owner信息
        Data.Owner owner = new Data.Owner();
        JsonObject ownerJson = video.getAsJsonObject("owner");
        owner.setMid(ownerJson.get("mid").getAsLong());
        owner.setName(ownerJson.get("name").getAsString());
        owner.setFace(ownerJson.get("face").getAsString());
        videoData.setOwner(owner);

        // 填充Stat信息
        Data.Stat stat = new Data.Stat();
        JsonObject statJson = video.getAsJsonObject("stat");
        stat.setAid(statJson.get("aid").getAsLong());
        stat.setView(statJson.get("view").getAsInt());
        stat.setDanmaku(statJson.get("danmaku").getAsInt());
        stat.setReply(statJson.get("reply").getAsInt());
        stat.setFavorite(statJson.get("favorite").getAsInt());
        stat.setCoin(statJson.get("coin").getAsInt());
        stat.setShare(statJson.get("share").getAsInt());
        stat.setLike(statJson.get("like").getAsInt());
        videoData.setStat(stat);

        // 填充Rights信息
        Data.Rights rights = new Data.Rights();
        JsonObject rightsJson = video.getAsJsonObject("rights");
        rights.setBp(rightsJson.get("bp").getAsInt());
        rights.setElec(rightsJson.get("elec").getAsInt());
        rights.setDownload(rightsJson.get("download").getAsInt());
        rights.setMovie(rightsJson.get("movie").getAsInt());
        rights.setPay(rightsJson.get("pay").getAsInt());
        rights.setHd5(rightsJson.get("hd5").getAsInt());
        rights.setNo_reprint(rightsJson.get("no_reprint").getAsInt());
        videoData.setRights(rights);

        // 设置封装好的视频数据
        videoDetail.setData(videoData);

        return videoDetail;
    }
}