import {driver} from "driver.js";
import {UserVO} from "@/types/userType";
import {useSnackbarStore} from "@/stores/snackbarStore";
const snackbarStore = useSnackbarStore();

const dashBoard = driver({
  showProgress: true,
  steps: [
    { element: '#dashboard', popover: { title: '欢迎来到CyberNomads', description: '欢迎来到CyberNomads，相信你面对该界面的时候充满了疑惑，但是不用担心接下来我会带你一一解答' } },
    { element: '#languageSwitcher', popover: { title: '语言切换', description: '在这里可以切换成自己熟悉的语言' } },
    { element: '#defaultMenu', popover: { title: '功能栏向导', description: '在这里有着CyberNomads众多功能例如：</br>赛博游民：平台账号管理</br>任务列表：创建任务让平台账号做任务' } },
    { element: '#logList', popover: { title: '任务日志', description: '这里将会展示每一个赛博游民做完任务后打印的内容，判断任务成功与否' } },
    { element: '#nomadsTablePage', popover: { title: '赛博游民列表', description: '在这里你可以添加你的各个平台的账号作为赛博游民，来为其分配任务，更多详情可以点入功能栏了解' } },
    { element: '#tutorial', popover: { title: '教程指导', description: '你可以点击此处，重新观看教程。不同的工具栏页面点击该按钮将有不同的教程哦~' } },
  ],
  onDestroyStarted: () => {
    if (!dashBoard.hasNextStep() || !isNewComer() || confirm("您确定要跳过新人指导吗？")) {
      saveComerGuidance()
      console.log("record guidance operation")
      dashBoard.destroy();
    }
  },
});

const comerKey = (email:string)=>{
  return email+"|newcomer"
}

export const newcomerGuidance = ()=> {
  const userInfo = JSON.parse(localStorage.getItem('cyberUser') || '{}')["userInfo"] as UserVO;
  if(userInfo){
    const newcomer = localStorage.getItem(comerKey(userInfo.email))
    if(!newcomer){
      snackbarStore.showSuccessMessage("🌈欢迎来到CyberNomads "+userInfo.email+" 请开始你的新世界之旅吧~")
      setTimeout(()=>{
        dashBoard.drive();
      },1000)

    }
  }
}

const saveComerGuidance =()=>{
  const userInfo = JSON.parse(localStorage.getItem('cyberUser') || '{}')["userInfo"] as UserVO;
  if(userInfo){
    localStorage.setItem(comerKey(userInfo.email),"hello!")
  }
}

const isNewComer = () =>{
  const userInfo = JSON.parse(localStorage.getItem('cyberUser') || '{}')["userInfo"] as UserVO;
  if(userInfo){
    return !localStorage.getItem(comerKey(userInfo.email))
  }else{
    return true
  }
}

export const tutorial = {
  newcomerGuidance,
  dashBoard
}
