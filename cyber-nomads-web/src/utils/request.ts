import axios from "axios";
import { useSnackbarStore } from "@/stores/snackbarStore";
import {validRequestAuth} from "./authUtil"

const request = axios.create({
  baseURL: import.meta.env.VITE_APP_API_BASE_URL,
  timeout: 100000,
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
    console.log(response)
    let res = response.data;
    if (res.code === 114514) {
      snackbarStore.showErrorMessage(res.msg);
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
  }
);

export default request;
