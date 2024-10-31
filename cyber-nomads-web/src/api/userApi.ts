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

