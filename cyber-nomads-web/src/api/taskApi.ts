import request from '@/utils/request';

export function createTask(createTaskForm){
  return request.post('/task/create',createTaskForm)
}

export function changeTaskStatus(taskId:string, status:string){
  return request.post('/task/changeStatus',{
    taskId: taskId,
    status: status
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

export function getPlatformTaskType(platform:string){
  return request({
    url: '/task/platforms/functions-params',
    method:"get",
    params:{
      platform: platform
    }
  })
}
