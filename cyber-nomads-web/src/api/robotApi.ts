import request from '@/utils/request';

export function getRobotList(){
  return request({
    url: '/robot/search',
    method:"get",
  })
}

export function uploadRobot(file){
  return request.post('/robot/update',file)
}

export function deleteRobot(id: bigint){
  return request({
    url: '/robot/delete',
    method:"get",
    params:{
      id: id
    }
  })
}

export function banRobot(id: bigint){
  return request({
    url: '/robot/ban',
    method:"post",
    params:{
      id: id
    }
  })
}


export function changeRobot(changeRobotForm){
  return request.post('/robot/change',changeRobotForm)
}

export function getPlatforms(){
  return request({
    url: '/robot/platforms',
    method:"get",
  })
}