import { defineStore } from "pinia";
export const useStepStore = defineStore({
    id: "stepStore",
    state: () => ({
        stepValid: ref([true, false, 'next', 'next']),
        step: ref(1),
        stepList: ['校验登号器令牌','自动获取配置信息','本地浏览器配置','选择登录方式'],
        steps:4
    }),

    getters: {
        disabled(){
            return this.stepValid[this.step-1]
        }
    },
    actions: {
      changeValid(step: number){
          if(step==1){
              this.stepValid[step-1] ='prev'
          }else if(step<3){
              this.stepValid[step-1] = false
          }else{
              this.stepValid[step-1] = false
          }
      },
    },
});
