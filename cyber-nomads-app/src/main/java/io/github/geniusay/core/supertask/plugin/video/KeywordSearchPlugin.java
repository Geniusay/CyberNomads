package io.github.geniusay.core.supertask.plugin.video;

import io.github.geniusay.core.supertask.task.RobotWorker;
import io.github.geniusay.core.supertask.task.Task;
import io.github.geniusay.core.supertask.task.TaskNeedParams;
import io.github.geniusay.crawler.api.bilibili.BilibiliSearchApi;
import io.github.geniusay.crawler.po.bilibili.BilibiliVideoDetail;
import io.github.geniusay.crawler.po.bilibili.VideoSearchResult;
import io.github.geniusay.crawler.util.bilibili.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static io.github.geniusay.constants.PluginConstant.*;
import static io.github.geniusay.core.supertask.config.PluginConstant.KEYWORD_SEARCH_PLUGIN;
import static io.github.geniusay.core.supertask.task.TaskNeedParams.InputTypeEnum.TEXTAREA;
import static io.github.geniusay.crawler.test.bilibili.TestVideoAPI.imgKey;
import static io.github.geniusay.crawler.test.bilibili.TestVideoAPI.subKey;
import static io.github.geniusay.utils.BilibiliFormatUtil.parseKeywords;

@Slf4j
@Scope("prototype")
@Component(KEYWORD_SEARCH_PLUGIN)
public class KeywordSearchPlugin extends AbstractGetVideoPlugin implements GetHandleVideoPlugin {

    private static final Map<String, String> KEYWORD_SEARCH_ORDER_MAPPING = Map.of(
            "综合排序", "totalrank",
            "最多播放", "click",
            "最新发布", "pubdate"
    );

    private static final String DEFAULT_KEYWORD_SEARCH = "#性感猫娘#变态教父#嗨丝诱惑";
    private static final int PAGE_SIZE = 10; // 每页数据条数
    private static final int ALL_SIZE = 20; // 每个关键词最大数量

    private String order;
    private List<String> keywords;
    private Map<String, Integer> keywordPageMap; // 记录每个关键词当前的页码
    private List<VideoSearchResult.Data.Result> cachePool; // 缓存池改为 List
    private int cachePoolLimit; // 缓存池最大限制
    private int currentKeywordIndex; // 当前处理的关键词索引
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Random random = new Random(); // 用于随机取数据

    @Override
    public void init(Task task) {
        super.init(task);

        // 排序规则解析
        order = KEYWORD_SEARCH_ORDER_MAPPING.getOrDefault(
                getValue(this.pluginParams, KEYWORD_SEARCH_ORDER, String.class),
                "pubdate"
        );

        // 获取用户输入的关键词字符串
        String keywordSearchText = getValue(this.pluginParams, KEYWORD_SEARCH, String.class);

        // 如果关键词为空，使用默认关键词
        if (keywordSearchText == null || keywordSearchText.trim().isEmpty()) {
            log.warn("关键词为空，使用默认关键词！");
            keywordSearchText = DEFAULT_KEYWORD_SEARCH;
        }

        // 解析关键词：按照规则提取以 '#' 分隔的关键词
        keywords = parseKeywords(keywordSearchText);

        if (keywords.isEmpty()) {
            log.warn("解析后的关键词为空，添加默认关键词！");
            keywords = parseKeywords(DEFAULT_KEYWORD_SEARCH);
        }

        if (keywords.size() > 5) {
            log.warn("关键词数量超过限制，截取前 5 个关键词！");
            keywords = keywords.subList(0, 5);
        }

        keywordPageMap = new HashMap<>();
        for (String keyword : keywords) {
            keywordPageMap.put(keyword, 1);
        }

        cachePool = new LinkedList<>();

        cachePoolLimit = keywords.size() * ALL_SIZE;
        log.info("缓存池最大限制设置为: {}", cachePoolLimit);

        currentKeywordIndex = 0;
    }

    @Override
    public BilibiliVideoDetail getHandleVideo(RobotWorker robot, Task task) {
        synchronized (this) {
            // 如果缓存池为空，立即同步获取一批数据
            if (cachePool.isEmpty()) {
                log.info("缓存池为空，立即同步获取一批数据...");
                fetchDataForKeyword(true);
            }

            // 从缓存池中随机取出一条数据
            VideoSearchResult.Data.Result result = null;
            if (!cachePool.isEmpty()) {
                int randomIndex = random.nextInt(cachePool.size());
                result = cachePool.remove(randomIndex);
                log.info("从缓存池中随机取出视频数据，当前缓存池大小: {}", cachePool.size());
            } else {
                log.warn("缓存池仍为空，无法提供视频数据！");
            }

            if (cachePool.size() < cachePoolLimit) {
                log.info("缓存池未达到最大限制{}，启动后台渐进式获取任务...", cachePoolLimit);
                executorService.submit(() -> fetchDataForKeyword(false));
            }

            // 返回从缓存池中取出的视频数据
            return BilibiliVideoDetail.convertToBilibiliVideoDetail(result);
        }
    }

    /**
     * 通用方法：获取数据并填充缓存池
     */
    private void fetchDataForKeyword(boolean isSynchronous) {
        String currentKeyword = keywords.get(currentKeywordIndex);
        int currentPage = keywordPageMap.get(currentKeyword);

        String fetchType = isSynchronous ? "同步" : "异步";

        ApiResponse<VideoSearchResult> response = BilibiliSearchApi.searchVideos(
                currentKeyword,
                order,
                0, // 视频时长筛选，0表示全部
                0, // 视频分区筛选，0表示全部
                currentPage,
                PAGE_SIZE,
                imgKey, // imgKey
                subKey  // subKey
        );

        if (response.isSuccess() && response.getData() != null) {
            List<VideoSearchResult.Data.Result> results = response.getData().getData().getResult();

            if (results != null && !results.isEmpty()) {
                synchronized (cachePool) {
                    cachePool.addAll(results);
                    log.info("{}获取的关键词 [{}] 第 [{}] 页数据已加入缓存池，当前缓存池大小: {}", fetchType, currentKeyword, currentPage, cachePool.size());
                }

                // 更新当前关键词的页码
                keywordPageMap.put(currentKeyword, currentPage + 1);

                // 移动到下一个关键词
                currentKeywordIndex = (currentKeywordIndex + 1) % keywords.size();
            } else {
                log.warn("{}获取的关键词 [{}] 第 [{}] 页无数据！", fetchType, currentKeyword, currentPage);
            }
        } else {
            log.warn("{}获取的关键词 [{}] 第 [{}] 页获取失败: {}", fetchType, currentKeyword, currentPage, response.getMsg());
        }
    }

    @Override
    public List<TaskNeedParams> supplierNeedParams() {
        return List.of(
                TaskNeedParams.ofKV(KEYWORD_SEARCH, DEFAULT_KEYWORD_SEARCH, "关键词编写").setInputType(TEXTAREA).setExtendDesc(KEYWORD_SEARCH_DESC),
                TaskNeedParams.ofSelection(KEYWORD_SEARCH_ORDER, "最新发布", "视频排序规则 (默认:最新发布)", List.of(
                        TaskNeedParams.ofK("最新发布", String.class, "最新发布"),
                        TaskNeedParams.ofK("综合排序", String.class, "综合排序"),
                        TaskNeedParams.ofK("最多播放", String.class, "最多播放")
                ), KEYWORD_SEARCH_ORDER_DESC, false)
        );
    }
}