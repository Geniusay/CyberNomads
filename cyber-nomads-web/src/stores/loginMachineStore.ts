import { defineStore } from "pinia";

export const useDialogStore = defineStore({
  id: "dialogStore",

  state: ()=>({
    dialogMap: ref({
      loginMachine: false
    })
  }),

  actions:{
    openDialog(dialogName){
      this.dialogMap[dialogName] = true
    },
    closeDialog(dialogName){
      this.dialogMap[dialogName] = false
    }
  }
})
