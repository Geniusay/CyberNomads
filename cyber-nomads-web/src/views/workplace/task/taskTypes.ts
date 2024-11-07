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
}

export interface TaskForm{
  taskName: string;
  platform: string;
  taskType: string;
  params: Record<string, any>;
  robots: number[];
}

const defaultTaskForm: TaskForm = {
  taskName: "",
  platform: "",
  taskType: "",
  params: {},
  robots: []
}

export const defaultValue = {
  defaultTaskForm
}
