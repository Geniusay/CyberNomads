import {noTokenUrl} from "./no-auth";

export const validRequestAuth = (config)=>{
  const token = localStorage.getItem("userStore") ? JSON.parse(localStorage.getItem("userStore")!).token : null;
  if(token){
    config.headers['Authorization'] = token
  }
  if(noTokenUrl.includes(config.url!)){
    return config
  }
  if(!token){
     return null
  }
  return config;
}
