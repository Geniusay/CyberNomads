export interface AccountForm{
    username: string,
    platform: string
}

export interface RobotVO{
    id: string;
    platform: string;
    platformCode: number;
    platformCnZh: string;
    nickname: string;
    username: string;
    ban: boolean;
    createTime: string;
    hasCookie: boolean;
}
