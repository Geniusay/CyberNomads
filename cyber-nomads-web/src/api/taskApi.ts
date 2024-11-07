import request from '@/utils/request';

export function createTask(createTaskForm){
  return request.post('/task/create',createTaskForm)
}

export function getUserTask(id){
  return request.get('/task/create/' + id)
}

export function updateRobots(updateRobotsForm){
  return request.post('/task/robots/update',updateRobotsForm)
}

export function updateParams(updateParamsForm){
  return request.post('/task/params/update',updateParamsForm)
}

export function getTaskList(){
  return request({
    url: '/task/list',
    method:"get",
  })
}

export function getPlatformTaskType(platform:string){
  return request({
    url: '/task/platforms/functions-params',
    method:"get",
    params:{
      platform: platform
    }
  })
}
