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
    icon:"mdi-arrow-right-drop-circle",
    color:"green",
    content:"workplace.task.running",
    value:"running",
    next:"paused",
    nextMsg:"暂停任务"
  },
  exception:{
    icon:"mdi-alert-circle",
    color:"orange",
    content:"workplace.task.exception",
    value:"exception",
    next:"paused",
    nextMsg:"暂停任务"
  },
  paused:{
    icon:"mdi-pause-circle",
    color:"yellow",
    content:"workplace.task.pause",
    value:"paused",
    next:"running",
    nextMsg:"重启任务"
  },
  finish:{
    icon:"mdi-check-circle",
    color:"green",
    content:"workplace.task.finish",
    value:"finish",
    next:"running",
    nextMsg:"重启任务"
  },
  error:{
    icon: "mdi-application-variable",
    color:"red",
    content:"workplace.task.error",
    value:"error",
    next:"running",
    nextMsg:"重启任务"
  }
}

export const isStatusIn = (itemStatus: string, statuses: string[]): boolean => {
  return statuses.includes(itemStatus);
}

export const buttonStatus = {
  ready:{
    icon: "mdi-play",
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
  reset:{
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
  if(isStatusIn(itemStatus, [status.error.value, status.finish.value])){
    return buttonStatus.reset
  }
}

export const images = [
  "https://images.unsplash.com/photo-1585829365295-ab7cd400c167?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8NXx8YXJ0aWNsZXxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=600&q=60"
]
