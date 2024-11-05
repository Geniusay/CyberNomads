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
