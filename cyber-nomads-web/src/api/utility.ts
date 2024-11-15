import request from '@/utils/request';

export function  getQAList(){
    return request({
        url: '/faq/get',
        method: "get"
    })
}
