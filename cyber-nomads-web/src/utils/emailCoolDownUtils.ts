// cooldownTimer.ts

import { ref } from 'vue';

interface EmailCoolDownObj {
  email: string;
  timer: number | null;
  count: number;
  nextTime: number;
}

const coolTimerMap = ref<Record<string, EmailCoolDownObj>>({});

const loadCoolDownTimer = () => {
  coolTimerMap.value = JSON.parse(localStorage.getItem('emailCoolDown') || '{}');

  for (const [email, timer] of Object.entries(coolTimerMap.value)) {
    const timeLeft = Math.floor((timer.nextTime - Date.now()) / 1000);
    if (timeLeft < 0) {
      delete coolTimerMap.value[email];
    } else {
      startCoolDown(email, timeLeft);
    }
  }

  saveCoolDownTime();
};

const startCoolDown = (email: string, time: number) => {
  let emailCoolDownObj = coolTimerMap.value[email];
  let timer;

  if (emailCoolDownObj) {
    emailCoolDownObj.count = time;
    timer = emailCoolDownObj.timer;
    if (timer) clearInterval(timer);

    timer = newCoolDownTimer(email);
    emailCoolDownObj.timer = timer;
  } else {
    const newEmailCoolDownObj: EmailCoolDownObj = {
      email: email,
      timer: 0,
      count: time,
      nextTime: Date.now() + time * 1000,
    };
    timer = newCoolDownTimer(email);
    newEmailCoolDownObj.timer = timer;
    coolTimerMap.value[email] = newEmailCoolDownObj;
  }

  saveCoolDownTime();
};

const newCoolDownTimer = (email: string) => {
  const timer = setInterval(() => {
    coolTimerMap.value[email].count -= 1;
    if (coolTimerMap.value[email].count < 0) {
      clearInterval(timer);
      delete coolTimerMap.value[email];
      saveCoolDownTime();
    }
  }, 1000);

  return timer;
};

const saveCoolDownTime = () => {
  localStorage.setItem('emailCoolDown', JSON.stringify(coolTimerMap.value));
};

const getCoolDownTime = (email: string): number => {
  const emailCoolDownTime = coolTimerMap.value[email];
  return emailCoolDownTime ? emailCoolDownTime.count : -1;
};

export default {
  loadCoolDownTimer,
  startCoolDown,
  getCoolDownTime,
  saveCoolDownTime,
};
