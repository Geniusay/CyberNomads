import request from '@/utils/request';

export function createTask(createTaskForm){
  return request.post('/task/create',createTaskForm)
}

export function changeTaskStatus(taskId:string, action:string){
  return request.post('/task/changeStatus',{
    taskId: taskId,
    action: action
  })
}

export function updateTask(updateTask){
  return request.post('/task/update',updateTask)
}

export function deleteTask(taskId){
  return request.post('/task/delete/'+taskId)
}

export function getTaskList(){
  return request({
    url: '/task/list',
    method:"get",
  })
}

export function getRecentLogs(taskId, limit){
  return request({
    url: '/task/getRecentLogs',
    method:"get",
    params:{
      taskId: taskId,
      limit: limit
    }
  })
}

export function getUserRecentLogs(){
  return request({
    url: '/task/userAllRecentLogs',
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
