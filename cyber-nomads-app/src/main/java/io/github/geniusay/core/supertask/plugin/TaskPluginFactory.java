package io.github.geniusay.core.supertask.plugin;

import io.github.geniusay.core.supertask.plugin.comment.AICommentGenerate;
import io.github.geniusay.core.supertask.plugin.terminator.CooldownTerminator;
import io.github.geniusay.core.supertask.plugin.terminator.GroupCountTerminator;
import io.github.geniusay.core.supertask.task.Task;
import io.github.geniusay.core.supertask.task.TaskNeedParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class TaskPluginFactory {

    private ApplicationContext applicationContext;

    // plugin元数据，无实际初始化
    private final Map<String,BaseTaskPlugin> metaPluginInfo;

    @Autowired
    public TaskPluginFactory(ApplicationContext applicationContext){
        this.applicationContext = applicationContext;
        metaPluginInfo = applicationContext.getBeansOfType(BaseTaskPlugin.class);
    }

    /**
     * 根据插件名称，自动为你提供selection的插件参数提供
     * 例如：AiPlugin(AI)，Ai2Plugin(AI)，CommentPlugin(Comment)，Comment2Plugin(Comment)
     * 会将当前分为AI selection参数和comment selection参数.
     * res：即 [{name:AI,childParams:[AiPlugin.params, Ai2Plugin.params] ,{name:Comment,childParams:[Comment2Plugin.params, CommentPlugin.params]}
     * @param pluginNames
     * @return
     */
    public List<TaskNeedParams> pluginGroupParams(List<String> pluginNames){
        Map<String, TaskNeedParams> groupParamsMap = new HashMap<>();
        for (String pluginName : pluginNames) {
            if (metaPluginInfo.containsKey(pluginName)) {
                BaseTaskPlugin baseTaskPlugin = metaPluginInfo.get(pluginName);
                String pluginGroup = baseTaskPlugin.getPluginGroup();
                if (!groupParamsMap.containsKey(pluginGroup)) {
                    groupParamsMap.put(pluginGroup, TaskNeedParams.ofK(pluginGroup, String.class, pluginGroup));
                }
                groupParamsMap.get(pluginGroup).getSelection().addAll(baseTaskPlugin.supplierNeedParams());
            }
        }
        return new ArrayList<>(groupParamsMap.values());
    }

    @SafeVarargs
    public final List<TaskNeedParams> pluginGroupParams(Class<? extends BaseTaskPlugin>... pluginClasses){
        List<String> pluginNames = Arrays.stream(pluginClasses).map(BaseTaskPlugin::getPluginName).collect(Collectors.toList());
        return pluginGroupParams(pluginNames);
    }

    /**
     * 根据插件类构建插件
     * @param plugin 插件类
     * @param task
     * @return 构建plugin
     * @param <T>
     */
    public <T extends BaseTaskPlugin> T buildPlugin(Class<T> plugin, Task task){
        T bean = applicationContext.getBean(BaseTaskPlugin.getPluginName(plugin), plugin);
        pluginInit(bean, task);
        return bean;
    }

    /**
     * 根据插件类构建插件
     * @param pluginName 插件类
     * @param task 任务
     * @return 构建plugin
     * @param <T>
     */
    public <T extends BaseTaskPlugin> T buildPlugin(String pluginName, Task task){
        T bean = (T) applicationContext.getBean(pluginName);
        pluginInit(bean, task);
        return bean;
    }

    /**
     * 根据插件组，通过参数策略选择插件
     * @param groupName 插件类
     * @param task 任务
     * @return 构建plugin
     * @param <T>
     */
    public <T extends BaseTaskPlugin> T buildPluginWithGroup(String groupName, Task task){
        String paramValue = (String)task.getParams().get(groupName);
        T bean = (T) applicationContext.getBean(paramValue);
        pluginInit(bean, task);
        return bean;
    }

    /**
     * 强制调用初始化参数
     * @param baseTaskPlugin
     * @param task
     */
    private void pluginInit(BaseTaskPlugin baseTaskPlugin, Task task){
       baseTaskPlugin.init(task);
    }
}
