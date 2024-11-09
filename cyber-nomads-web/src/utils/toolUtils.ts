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
