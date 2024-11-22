package io.github.geniusay.crawler.po.bilibili;

import lombok.Data;

@Data
public class BilibiliVideoDetail {

    private String bvid;
    private long aid;
    private int tid;
    private String tname;
    private String title;
    private long mid;
    private String name;
    private int view;
    private int danmaku;
    private int reply;
    private int favorite;
    private int coin;
    private int share;
    private int like;

    /**
     * 从 VideoDetail 对象中提取需要的字段，构建 BilibiliVideoDetail
     */
    public static BilibiliVideoDetail fromVideoDetail(VideoDetail videoDetail) {
        BilibiliVideoDetail bilibiliVideoDetail = new BilibiliVideoDetail();
        VideoDetail.Data data = videoDetail.getData();

        bilibiliVideoDetail.setBvid(data.getBvid());
        bilibiliVideoDetail.setAid(data.getAid());
        bilibiliVideoDetail.setTid(data.getTid());
        bilibiliVideoDetail.setTname(data.getTname());
        bilibiliVideoDetail.setTitle(data.getTitle());
        bilibiliVideoDetail.setMid(data.getOwner().getMid());
        bilibiliVideoDetail.setName(data.getOwner().getName());
        bilibiliVideoDetail.setView(data.getStat().getView());
        bilibiliVideoDetail.setDanmaku(data.getStat().getDanmaku());
        bilibiliVideoDetail.setReply(data.getStat().getReply());
        bilibiliVideoDetail.setFavorite(data.getStat().getFavorite());
        bilibiliVideoDetail.setCoin(data.getStat().getCoin());
        bilibiliVideoDetail.setShare(data.getStat().getShare());
        bilibiliVideoDetail.setLike(data.getStat().getLike());

        return bilibiliVideoDetail;
    }

    public static BilibiliVideoDetail convertToBilibiliVideoDetail(VideoSearchResult.Data.Result result) {
        if (result == null) {
            return null;
        }
        BilibiliVideoDetail videoDetail = new BilibiliVideoDetail();
        videoDetail.setBvid(result.getBvid());
        videoDetail.setAid(result.getAid());
        videoDetail.setTitle(result.getTitle());
        videoDetail.setName(result.getAuthor());
        videoDetail.setMid(result.getMid());
        return videoDetail;
    }

    public String toJson() {
        return new com.google.gson.Gson().toJson(this);
    }

    public static BilibiliVideoDetail fromJson(String json) {
        return new com.google.gson.Gson().fromJson(json, BilibiliVideoDetail.class);
    }
}