import request from '@/utils/request';

export function sendPicCaptcha(){
  return request.post('/user/captcha')
}


export function emailLogin(loginForm){
  return request.post('/user/login',loginForm)
}

export function emailRegister(registerForm){
  return request.post('/user/register',registerForm)
}

export function sendCodeToEmail(email: string,pid: string,code: string){
  return request({
    url: '/user/sendCaptcha',
    method:"post",
    params:{
      email: email,
      pid: pid,
      code: code
    }
  })
}

export function getUserInfo(){
  return request.get('/user/getInfo')
}
