import { computed } from 'vue';

export function delay(ms: number): Promise<void> {
  return new Promise(resolve => setTimeout(resolve, ms));
}

export function getRandomColor(): string {
  let r, g, b;

  do {
    r = Math.floor(Math.random() * 256); // Red value
    g = Math.floor(Math.random() * 256); // Green value
    b = Math.floor(Math.random() * 256); // Blue value
  } while (r === 0 && g === 0 && b === 0); // Ensure color is not black

  return `rgb(${r}, ${g}, ${b})`;
}

export const buildImageUrl = (url:string)=>{
  return computed(() => new URL(url, import.meta.url).href);
}

export const joinQQGroup = () =>{
  window.open("https://qm.qq.com/cgi-bin/qm/qr?k=SHDEebnC8VRCVj3nECvNq0kUvR7ptMCp&jump_from=webapi&authKey=1vucxCZY9wtjzHtub89JRhyCNIVbikb3GFe+v++sfVcVX+sm2OOGdVDWIMlwOTR/","_blank")
}

export const formatDate = (input: string): string => {
  const date = new Date(input);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, "0"); // 月份从0开始
  const day = String(date.getDate()).padStart(2, "0");
  const hours = String(date.getHours()).padStart(2, "0");
  const minutes = String(date.getMinutes()).padStart(2, "0");
  const seconds = String(date.getSeconds()).padStart(2, "0");

  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
};
