export interface TaskVO {
  id: string | null;                // 使用 string | null 来替代 Java 的 Long
  uid: string;
  nickname: string;
  taskName: string;
  platform: string;
  taskType: string;
  taskStatus: string;
  robots: number[];                 // 数组中的类型为 number，代表 List<Long> 类型
  params: Record<string, any>;      // 对应 Java 的 Map<String, Object>
  platformCnZh: string;
  taskTypeCnZh: string;
  createTime: string;
  taskLogs: TaskLog[];
}

export interface TaskForm{
  taskId: string | null;
  taskName: string;
  platform: string;
  taskType: string;
  params: Record<string, any>;
  robotIds: number[];
}

export interface Parameter{
  name: string;
  type: string;
  desc: string;
  required: boolean;
  inputType: string;
  defaultValue: string;
  params: Parameter[];
  selection: Parameter[];
}

export interface TaskType{
  taskTypeKey: string;
  taskTypeValue: string;
  params: Parameter[];
}

export interface TaskLog{
  success: boolean;
  content: string;
  robotName: string;
  createTime: string;
}

const defaultTaskForm: TaskForm = {
  taskId:"",
  taskName: "",
  platform: "bilibili",
  taskType: "",
  params: {},
  robotIds: []
}

export function mapTaskVOToTaskForm(taskVO: TaskVO): TaskForm {
  return {
    taskId: taskVO.id,
    taskName: taskVO.taskName,
    platform: taskVO.platform,
    taskType: taskVO.taskType,
    params: taskVO.params,
    robotIds: taskVO.robots, // 假设 `robots` 是 `robotIds` 对应的属性
  };
}

export const defaultValue = {
  defaultTaskForm
}
