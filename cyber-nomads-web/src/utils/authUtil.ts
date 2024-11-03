import {noTokenUrl} from "./no-auth";
import {useSnackbarStore} from "@/stores/snackbarStore";

export const validRequestAuth = (config)=>{
  const token = JSON.parse(localStorage.getItem('cyberUser') || '{}')["token"];
  if(token){
    config.headers['Authorization'] = token
  }
  if(noTokenUrl.includes(config.url!)){
    return config
  }
  if(!token){
    useSnackbarStore().showErrorMessage("请登录后再操作!!")
    return Promise.reject(new Error(config.url + "❌❌not Token"));
  }
  return config;
}
