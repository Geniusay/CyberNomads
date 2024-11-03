import axios from "axios";
import { useSnackbarStore } from "@/stores/snackbarStore";
import {validRequestAuth} from "./authUtil"

const request = axios.create({
  baseURL: import.meta.env.VITE_APP_API_BASE_URL,
  timeout: 5000,
});

request.interceptors.request.use(
  (config) => {
    config = validRequestAuth(config)
    if(config){
      return config
    }else{
      useSnackbarStore().showErrorMessage("请登录后再操作!!")
    }
    return Promise.reject(new Error(config.url + "❌❌not Token"));
  },
  (error) => {
    return Promise.reject(error);
  }
);

// response 拦截器
// 可以在接口响应后统一处理结果
request.interceptors.response.use(
  (response) => {
    const snackbarStore = useSnackbarStore();
    if (response.status != 200) {
      snackbarStore.showSuccessMessage("服务异常!");
    }
    let res = response.data;
    if (res.code !== "200") {
      console.log(res)
      return Promise.reject({
        response,
        message: res.msg || "请求异常",
      });
    }
    // 如果是返回的文件
    if (response.config.responseType === "blob") {
      return res;
    }
    // 兼容服务端返回的字符串数据
    if (typeof res === "string") {
      res = res ? JSON.parse(res) : res;
    }
    return res;
  },
  (error) => {
    // 如果 error.response 存在，表示服务端有返回响应
    if (error.response) {
      // 返回整个 response 对象以便后续代码捕获和处理
      return Promise.reject(error.response);
    } else {
      // 处理网络错误或请求未到达服务器的情况
      return Promise.reject(error);
    }
  }
);

export default request;
