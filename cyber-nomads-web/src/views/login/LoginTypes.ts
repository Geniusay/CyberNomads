export interface LoginForm{
  email: string;
  code: string;
  password: string
}

const defaultLoginForm: LoginForm = {
  email: "",
  code: "",
  password:"",
}

export interface RegisterForm{
  email: string;
  code: string;
  password: string
}

const defaultRegisterForm: RegisterForm = {
  email: "",
  code: "",
  password:"",
}

export const defaultValue = {
  defaultLoginForm,
  defaultRegisterForm,
}
