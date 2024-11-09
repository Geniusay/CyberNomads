export const status = {
  pending:{
    icon: "mdi-alert-circle",
    color:"grey",
    content:"workplace.task.pending",
    value:"pending",
    next:"running"
  },
  running:{
    icon:"mdi-arrow-right-drop-circle",
    color:"green",
    content:"workplace.task.running",
    value:"running",
    next:"paused"
  },
  exception:{
    icon:"mdi-alert-circle",
    color:"orange",
    content:"workplace.task.exception",
    value:"exception",
    next:"paused"
  },
  paused:{
    icon:"mdi-pause-circle",
    color:"yellow",
    content:"workplace.task.pause",
    value:"paused",
    next:"running"
  },
  finish:{
    icon:"mdi-check-circle",
    color:"green",
    content:"workplace.task.finish",
    value:"finish",
    next:"running"
  },
  error:{
    icon: "mdi-application-variable",
    color:"red",
    content:"workplace.task.error",
    value:"error",
    next:"running"
  }
}

export const isStatusIn = (itemStatus: string, statuses: string[]): boolean => {
  return statuses.includes(itemStatus);
}

export const buttonStatus = {
  ready:{
    icon: "mdi-play",
    color: "green",
  },
  running:{
    icon: "mdi-pause",
    color: "red",
  },
  pause:{
    icon: "mdi-reload",
    color: "grey",
  },
  stop:{
    icon: "mdi-reload",
    color: "grey",
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
    return buttonStatus.stop
  }
}

export const images = [
  "https://images.unsplash.com/photo-1585829365295-ab7cd400c167?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8NXx8YXJ0aWNsZXxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=600&q=60"
]
