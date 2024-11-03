export interface LoginForm{
  email: string;
  code: string;
  password: string
}

export interface PicCode{
  pid:string;
  img:string;
  code:string;
}

export interface RegisterForm{
  email: string;
  code: string;
  password: string;
  confirmPassword: string;
}

const defaultLoginForm: LoginForm = {
  email: "",
  code: "",
  password:"",
}

const defaultRegisterForm: RegisterForm = {
  email: "",
  code: "",
  password:"",
  confirmPassword:"",
}

const defaultPicCode: PicCode = {
  pid:"",
  img:"",
  code:""
}

export const defaultValue = {
  defaultLoginForm,
  defaultRegisterForm,
  defaultPicCode
}

