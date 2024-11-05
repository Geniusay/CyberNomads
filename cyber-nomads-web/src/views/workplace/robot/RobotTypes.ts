import {LoginForm} from "@/views/login/LoginTypes";

export interface RobotVO{
  id: string;
  plat: string;
  nickname: string;
  username: string;
  ban: boolean;
  createTime: string;
}


export interface RobotForm{
  id: number;
  platform: number;
  nickname: string;
  username: string;
  cookie: string;
}


const defaultRobotForm: RobotForm = {
  id: -1,
  platform: 1,
  nickname: '',
  username: '',
  cookie: ''
}

export const defaultValue = {
  defaultRobotForm
}
