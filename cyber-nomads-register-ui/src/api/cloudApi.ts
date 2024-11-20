import request from '@/utils/request';

export function verifyCode(code: string){
    return request({
        url: '/loginMachine/verityCode',
        method:"get",
        params:{
            code: code
        }
    })
}
export function getPath(){
    return request({
        url: '/loginMachine/getPath',
        method:"get",
    })
}

export function savePath(drivePath, browserPath){
    return request.post('/loginMachine/addDriverPath',{
        browserPath: browserPath,
        driverPath: drivePath
    })
}


export function login(username, platform){
    return request.post('/loginMachine/login',{
        username: username,
        platform: platform
    })
}

export function getRobots(){
    return request({
        url: '/loginMachine/getRobots',
        method:"get",
    })
}

export function getSystemInfo(browser){
    return request({
        url: '/loginMachine/getSystemBrowser',
        method:"get",
        params:{
            browser: browser
        }
    })
}

export function download(){
    return request({
        url: '/loginMachine/download',
        method:"get",
    })
}

export function downloadStatus(){
    return request({
        url: '/loginMachine/downloadStatus',
        method:"get",
    })
}

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
