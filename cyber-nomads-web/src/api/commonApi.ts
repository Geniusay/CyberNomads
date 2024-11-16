import request from "@/utils/request";

export function getPlatforms(){
  return request({
    url: '/common/platforms',
    method:"get",
  })
}

export function queryLoginMachineInfo(id){
  return request({
      url: '/loginMachine/queryMachineInfo',
      method:"get",
      params:{
        id: id
      }
    })
}
