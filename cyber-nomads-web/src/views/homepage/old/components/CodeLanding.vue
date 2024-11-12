<template>
  <div class="container">
    <div class="loader">
      <div class="counter">0</div>
    </div>
    <nav>
      <div class="logo-container">
        <div class="logo">
          <h1>Rvc</h1>
        </div>
        <div class="logo-name">
          <p>Welcome to RVC ///////</p>
        </div>
      </div>
      <div class="nav-buttons">
        <div class="btn">
          <button>our team</button>
        </div>
        <div class="btn">
          <button>info</button>
        </div>
      </div>
    </nav>

    <div class="sub-nav">
      <p class="intro">
        The RVC community is a platform dedicated to sharing and exchanging RVC
        (Retrieval based Voice Conversion) technology. RVC is an open-source
        tool based on the VITS speech synthesis system, which can achieve
        real-time sound transformation and is suitable for scenarios such as
        live streaming and video recording
      </p>

      <p class="primary">Create 2023</p>
    </div>

    <div class="hero-img">
      <div class="mask"></div>
      <div class="mask"></div>
      <div class="mask"></div>
      <div class="mask"></div>
      <div class="mask"></div>
      <div class="mask"></div>
      <div class="mask"></div>
      <div class="mask"></div>
      <div class="mask"></div>
      <div class="mask"></div>
    </div>

    <footer>
      <div class="slide-copy">
        <div class="slide-index">
          <p>v / 01</p>
        </div>
        <div class="slide-name">
          <p>Rvc Community</p>
        </div>
        <button>visit</button>
      </div>

      <div class="slide-info">
        <div class="year">
          <p><span>.</span>year</p>
          <p>2023</p>
        </div>
        <div class="agency">
          <p><span>.</span>team</p>
          <p>Next MileStone Studio</p>
        </div>
        <div class="webRole">
          <p><span>.</span>role</p>
          <p>front end developer</p>
        </div>
        <div class="awards">
          <p><span>.</span>description</p>
          <p>// Use saved searches to filter your results more quickly /</p>
          <p>
            Voice data &lt;= 10 mins can also be used to train a good VC model!
            License. /
          </p>
        </div>
      </div>
    </footer>
  </div>
</template>

<script setup lang="ts">
import { gsap } from "gsap";

onMounted(() => {
  const counter = document.querySelector(".counter");
  const loader = document.querySelector(".loader");
  const elementsToAnimate = document.querySelectorAll(
    "p:not(.intro), .logo h1"
  );
  const introTag = document.querySelector(".intro");
  let animationsInitialized = false;

  //在duration时间内，随机替换一段文本，执行完后并回调
  function shuffleText(finalText, duration, callback) {
    let i = 0;
    const shuffleInterval = setInterval(() => {
      if (i < duration) {
        counter.innerHTML = Math.random().toString(36).substring(2, 8);
        i++;
      } else {
        clearInterval(shuffleInterval);
        counter.innerHTML = finalText;
        if (callback) callback();
      }
    }, 100);
  }

  //不断将文字消失，直到结束为止
  function removeLetters() {
    let text = counter.innerHTML;
    const removeInterval = setInterval(() => {
      if (text.length > 0) {
        text = text.substring(0, text.length - 1);
        counter.innerHTML = text;
      } else {
        clearInterval(removeInterval);
        if (!animationsInitialized) {
          animateElements();
          animateIntroTag();
        }
        fadeOutLoader();
      }
    }, 100);
  }

  //将字符的字母依次替换为乱序字符再替换回来
  function animateElements() {
    if (animationsInitialized) return;
    animationsInitialized = true;

    elementsToAnimate.forEach((element) => {
      let originalText = element.textContent;
      let index = 0;

      const shuffleElement = setInterval(() => {
        if (index < originalText.length) {
          let shuffledText = "";
          for (let i = 0; i <= index; i++) {
            shuffledText +=
              i < index ? originalText[i] : Math.random().toString(36)[2];
          }
          element.textContent =
            shuffledText + originalText.substring(index + 1);
          index++;
        } else {
          clearInterval(shuffleElement);
          element.textContent = originalText;
        }
      }, 100);
    });
  }

  function animateIntroTag() {
    let originalText = introTag.textContent;
    let currentText = "";
    let index = 0;

    const revealText = setInterval(() => {
      if (index < originalText.length) {
        currentText += originalText[index];
        introTag.textContent = currentText;
        index++;
      } else {
        clearInterval(revealText);
      }
    }, 25);
  }

  function animateMasks() {
    const masks = document.querySelectorAll(".hero-img .mask");
    const cliipPathValues = [];
    for (let i = 0; i <= 9; i++) {
      let number = 10 + 10 * i + "%";
      let preNumber = 10 * i + (i == 0 ? "" : "%");
      let str = `polygon(${number} 0,${preNumber} 0,${preNumber} 100%,${number} 100%)`;
      cliipPathValues.push(str);
    }
    setTimeout(() => {
      masks.forEach((mask, index) => {
        gsap.to(mask, {
          clipPath: cliipPathValues[index % cliipPathValues.length],
          duration: 1,
          delay: index * 0.1,
        });
      });
    });
  }

  gsap.to(counter, {
    innerHTML: 100 + "%",
    duration: 3,
    snap: "innerHTML",
    ease: "none",
    onComplete: () => {
      setTimeout(
        () =>
          shuffleText("RVC/23", 20, () => {
            setTimeout(removeLetters, 500);
          }),
        500
      );
    },
  });

  function fadeOutLoader() {
    gsap.to(loader, {
      opacity: 0,
      pointerEvents: "none",
      duration: 1,
      onComplete: () => {
        animateMasks();
      },
    });
  }
});
</script>

<style>
:root {
  --color-bg: #1a1b1e;
  --color-accent: #ab3429;
  --color-text: #bbb9b9e1;
}
</style>

<style scoped>
@import "@/styles/view/homepage/old/_code-landing.scss";
</style>
