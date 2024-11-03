import {noTokenUrl} from "./no-auth";

export const validRequestAuth = (config)=>{
  const token = JSON.parse(localStorage.getItem('cyberUser') || '')["token"];
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
