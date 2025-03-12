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

export function addRobot(addRobotForm){
  return request.post('/robot/add',addRobotForm)
}

export function getCookie(id){
  return request.post('/robot/getCookie',{id:id})
}

export function changeCookie(id:string, cookie:string){
  return request.post('/robot/changeCookie',{
    id:id,
    cookie:cookie
  })
}

export function generateLoginMachine(){
  return request.post('/loginMachine/code')
}

export function shareRobot(req){
  return request.post('/robot/share', {
    robotId: req.robotId,
    flag: req.flag,
    focusTask: req.focusTask
  })
}

export function getShareRobotList(req){
  return request({
    url: '/robot/sharedRobotPage',
    method:"get",
    params:{
      page: req.page,
      taskType: req.taskType,
      platform: req.platform
    }
  })
}

export function recommendRobot(req){
  return request({
    url: '/robot/recommend',
    method:"get",
    params:{
      page: req.page,
      taskType: req.taskType,
      platform: req.platform
    }
  })
}
