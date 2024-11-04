import request from '@/utils/request';

export function getRobotList(){
  return request({
    url: '/robot/search',
    method:"get",
  })
}
