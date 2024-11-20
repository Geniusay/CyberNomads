import request from '@/utils/request';

export function createTaskTemplate(taskTemplateParams){
  return request({
    url: '/task/template/export',
    method:"post",
    params:{
      taskTemplateParams
    }
  })
}

export function queryTaskTemplate(param){
  return request({
    url: '/task/template/query',
    method: "post",
    params:{
      param
    }
  })
}

export function shareTaskTemplate(templateId){
  return request({
    url: '/task/template/share',
    method: "get",
    params:{
      templateId: templateId
    }
  })
}

export function importTaskTemplate(scipt){
  return request({
    url: '/task/template/share',
    method: "get",
    params:{
      scipt: scipt
    }
  })
}

export function editTaskTemplate(taskTemplateParams){
  return request({
    url: '/task/template/edit',
    method:"post",
    params:{
      taskTemplateParams
    }
  })
}

export function removeTaskTemplate(templateId){
  return request({
    url: '/task/template/delete',
    method: "get",
    params:{
      templateId: templateId
    }
  })
}
