export interface RobotVO{
  id: string;
  platform: string;
  platformCode: number;
  platformCnZh: string;
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
}


const defaultRobotForm: RobotForm = {
  id: -1,
  platform: 1,
  nickname: '',
  username: '',
}

export const defaultValue = {
  defaultRobotForm
}
