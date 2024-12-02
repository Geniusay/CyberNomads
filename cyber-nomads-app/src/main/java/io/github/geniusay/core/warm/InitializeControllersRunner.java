//package io.github.geniusay.core.warm;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.context.ApplicationContext;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.method.HandlerMethod;
//import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
//import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
//
//import java.util.Map;
//
//@Component
//public class InitializeControllersRunner implements ApplicationRunner {
//
//    @Autowired
//    private ApplicationContext context;
//
//    private static HttpHeaders headers = new HttpHeaders();
//
//    private static HttpEntity<String> entity = new HttpEntity<>("", headers);
//
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        // 初始化所有 Controller
//        initializeControllers();
//    }
//
//    private Map<RequestMappingInfo, HandlerMethod> getAllControllers() {
//        RequestMappingHandlerMapping handlerMapping = context.getBean(RequestMappingHandlerMapping.class);
//        return handlerMapping.getHandlerMethods();
//    }
//
//    private void initializeControllers() {
//        RestTemplate restTemplate = new RestTemplate();
//        Map<RequestMappingInfo, HandlerMethod> controllers = getAllControllers();
//
//        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : controllers.entrySet()) {
//            RequestMappingInfo info = entry.getKey();
//
//            // 获取接口的 URL 和请求方法
//            String url = buildUrl(info);
//            RequestMethod[] methods = info.getMethodsCondition().getMethods().toArray(new RequestMethod[0]);
//
//            if (url != null) {
//                for (RequestMethod methodType : methods) {
//                    try {
//                        switch (methodType) {
//                            case GET:
//                                restTemplate.getForObject(url, String.class);
//                                break;
//                            case POST:
//                                restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
//                                break;
//                            case PUT:
//                                restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
//                                break;
//                            case DELETE:
//                                restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);
//                                break;
//                            default:
//                                break;
//                        }
//                    } catch (Exception ignore) {
//
//                    }
//                }
//            }
//        }
//    }
//
//    private String buildUrl(RequestMappingInfo info) {
//        StringBuilder url = new StringBuilder("http://localhost:9000");
//        if (info.getPatternsCondition() != null) {
//            for (String pattern : info.getPatternsCondition().getPatterns()) {
//                url.append(pattern);
//                return url.toString();
//            }
//        }
//        return null;
//    }
//}