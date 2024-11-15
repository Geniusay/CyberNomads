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
        drivePath: drivePath
    })
}
