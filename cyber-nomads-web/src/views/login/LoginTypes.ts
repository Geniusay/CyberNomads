export interface LoginForm{
  email: string;
  code: string;
  password: string
}

export interface PicCode{
  pid:string;
  img:string;
}

export interface RegisterForm{
  email: string;
  code: string;
  password: string
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
}

const defaultPicCode: PicCode = {
  pid:"",
  img:"",
}

export const defaultValue = {
  defaultLoginForm,
  defaultRegisterForm,
  defaultPicCode
}

