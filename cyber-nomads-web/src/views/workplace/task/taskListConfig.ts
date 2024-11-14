import {Parameter} from "@/views/workplace/task/taskTypes";

export const status = {
  pending:{
    icon: "mdi-alert-circle",
    color:"grey",
    content:"workplace.task.pending",
    value:"pending",
    next:"running",
    nextMsg:"启动任务"
  },
  running:{
    icon:"line-md:cog-loop",
    color:"green",
    content:"workplace.task.running",
    value:"running",
    next:"paused",
    nextMsg:"暂停任务"
  },
  exception:{
    icon:"line-md:alert-circle-twotone-loop",
    color:"orange",
    content:"workplace.task.exception",
    value:"exception",
    next:"paused",
    nextMsg:"暂停任务"
  },
  paused:{
    icon:"line-md:cog-off-loop",
    color:"#d37c18",
    content:"workplace.task.pause",
    value:"paused",
    next:"running",
    nextMsg:"重启任务"
  },
  completed:{
    icon:"line-md:confirm-circle-filled",
    color:"#20b020",
    content:"workplace.task.finish",
    value:"completed",
    next:"pending",
    nextMsg:"重启任务"
  },
  error:{
    icon: "line-md:close-circle-filled",
    color:"red",
    content:"workplace.task.error",
    value:"error",
    next:"pending",
    nextMsg:"重启任务"
  }
}
export const InputType = {
  selection:"selection",
  input:"input",
  textarea:"textarea"
}

export const isStatusIn = (itemStatus: string, statuses: string[]): boolean => {
  return statuses.includes(itemStatus);
}

export const buttonStatus = {
  ready:{
    icon: "mdi-clipboard-text-play",
    color: "green",
    next: "start",
    nextMsg:"启动任务"
  },
  running:{
    icon: "mdi-pause",
    color: "red",
    next: "pause",
    nextMsg:"停止任务"
  },
  pause:{
    icon: "mdi-play-pause",
    color: "grey",
    next: "start",
    nextMsg:"继续任务"
  },
  finished:{
    icon: "mdi-reload",
    color: "grey",
    next: "reset",
    nextMsg:"重置任务"
  },
}

export const getButtonStatus =(itemStatus: string)=> {
  if (isStatusIn(itemStatus, [status.pending.value])) {
    return buttonStatus.ready
  }
  if(isStatusIn(itemStatus, [status.running.value, status.exception.value])){
    return buttonStatus.running
  }
  if(isStatusIn(itemStatus, [status.paused.value])){
    return buttonStatus.pause
  }
  if(isStatusIn(itemStatus, [status.error.value, status.completed.value])){
    return buttonStatus.finished
  }
}


export const images = [
  "https://geniusserve.oss-cn-shanghai.aliyuncs.com/cybernomads/taskImg/task1.png",
  "https://geniusserve.oss-cn-shanghai.aliyuncs.com/cybernomads/taskImg/task2.png",
  "https://geniusserve.oss-cn-shanghai.aliyuncs.com/cybernomads/taskImg/task3.png",
  "https://geniusserve.oss-cn-shanghai.aliyuncs.com/cybernomads/taskImg/task4.png",
  "https://geniusserve.oss-cn-shanghai.aliyuncs.com/cybernomads/taskImg/task5.png",
  "https://geniusserve.oss-cn-shanghai.aliyuncs.com/cybernomads/taskImg/task6.png",
  "https://geniusserve.oss-cn-shanghai.aliyuncs.com/cybernomads/taskImg/task7.png",
  "https://geniusserve.oss-cn-shanghai.aliyuncs.com/cybernomads/taskImg/task8.png",
]

function validateParam(name: string, value: any, param: Parameter): string | null {
  // 检查参数类型是否正确
  if (param.required && !value) {
    return `${name}参数是必需的`;
  }

  return null;
}

function selectionFlatToMap(params: Parameter): Record<string, Parameter> {
  return params.selection.reduce((acc, item) => {
    acc[item.name] = item;
    return acc;
  }, {} as Record<string, Parameter>);
}


export function validateAndGetParams(
  paramMap: Record<string, any>,
  params: Parameter[]
): string | null {
  for (const needParam of params) {
    const name = needParam.name;
    const value = paramMap[name] !== undefined ? paramMap[name] : needParam.defaultValue;
    let nextParams: Parameter[] = [];

    // 若为必填或存在值，则校验
    if (needParam.required || value !== null) {
      const errorMsg = validateParam(needParam.desc, value, needParam);
      if (errorMsg) return errorMsg;
    }
    if (needParam.inputType === 'selection') {
      nextParams.push(selectionFlatToMap(needParam)[value as string]);
    } else {
      nextParams = nextParams.concat(needParam.params);
    }
    if (nextParams.length > 0) {
      const error = validateAndGetParams(paramMap, nextParams);
      if (error) return error;
    }
  }

  return null;
}
