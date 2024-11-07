import request from "@/utils/request";

export function getPlatforms(){
  return request({
    url: '/common/platforms',
    method:"get",
  })
}
