<script setup lang="ts">
import {onMounted} from "vue";
import { gsap } from "gsap";

const rows = 6;
const cols = 6;
const block_size =50;
const cooldown = 100;
let isFlipped = false;

onMounted(async ()=>{
  function createTiles(row, col){
    const tile = document.createElement("div");
    tile.className = "tile";
    tile.innerHTML = '<div class="tile-face tile-front"></div>' +
      '<div class="tile-face tile-back"></div>'
    const bgPosistion = (col*20)+'% '+(row*20)+'%'
    tile.querySelector(".tile-front").style.backgroundPosition = bgPosistion;
    tile.querySelector(".tile-back").style.backgroundPosition  = bgPosistion;

    return tile;
  }

  function createBoard(){
    const board = document.querySelector(".header-board")
    for(let i=0;i<rows;i++){
      const row = document.createElement("div");
      row.className = "header-row";
      for(let j=0;j<cols;j++){
        row.appendChild(createTiles(i,j));
      }
      board.appendChild(row)
    }
  }

  function initializeTileAnimation(){
    const tiles = document.querySelectorAll(".tile");
    tiles.forEach((tile,index)=>{
      let lastEnterTime = 0;

      tile.addEventListener("mouseenter", ()=>{
        const currentTime = Date.now()
        if(currentTime - lastEnterTime > cooldown){
          lastEnterTime = currentTime;

          let tiltY;
          if(index % 6===0){
            tiltY = -40
          }else if(index % 6 === 5){
            tiltY = 40;
          }else if(index %6 ===1){
            tiltY = -20;
          }else if(index % 6=== 4){
            tiltY = 20;
          }else if(index % 6===2){
            tiltY = -10;
          }else{
            tiltY = 10;
          }

          animateTile(tile , tiltY)
        }
      })
    })

    const flipButton = document.querySelector("[data-logo]");
    flipButton.addEventListener("click", ()=>flipAllTiles(tiles));
  }

  function animateTile(tile, tiltY){
    gsap
      .timeline()
      .set(tile, {rotateX: isFlipped?180:0, rotateY: 0})
      .to(tile, {
        rotateX: isFlipped?450:270,
        rotateY: tiltY,
        duration: 0.5,
        ease: "power2.out",
      })
      .to(tile,{
        rotateX: isFlipped?540:360,
        rotateY: 0,
        duration: 0.5,
        ease: "power2.out"
      },"-=0.25")
  }

  function flipAllTiles(tiles){
    isFlipped = !isFlipped;
    gsap.to(tiles,{
      rotateX: isFlipped? 180:0,
      duration: 1,
      stagger:{
        amount:0.5,
        from:"random"
      },
      ease: "power2.inOut"
    })
  }

  function highlightBlock(event){
    const {numCols} = window.blockInfo;
    const blocksContainer = document.getElementById("blocks");
    const rect = blocksContainer.getBoundingClientRect();
    const x = event.clientX - rect.left;
    const y = event.clientY - rect.top;
    const col = Math.floor(x / block_size);
    const row = Math.floor( y/block_size);
    const index = row * numCols + col;

    const block = blocksContainer.children[index];
    if(block){
      block.classList.add("highlight");
      setTimeout(()=>{
        block.classList.remove("highlight");
      }, 250);
    }
  }

  function createBlocks(){
    const blocksContainer = document.getElementById("blocks")
    const screenWidth = window.innerWidth;
    const screenHeight = window.innerHeight;
    const numCols = Math.ceil(screenWidth/block_size);
    const numRows = Math.ceil(screenHeight/block_size);
    const numBlocks = numCols * numRows;

    for(let i = 0;i<numBlocks;i++){
      const block = document.createElement("div");
      block?.classList.add("header-block");
      block.dataset.index = i;
      blocksContainer.appendChild(block)
    }
    return {numCols, numBlocks};
  }

  function init(){
    createBoard();
    initializeTileAnimation();
    window.blockInfo = createBlocks();
    document.addEventListener("mousemove", highlightBlock)
  }
  init()
})

</script>

<template>
<div class="header-container">
  <nav class="header-nav">
  </nav>

  <section class="header-board"></section>

  <div class="blocks-container">
    <div id="blocks"></div>
  </div>
</div>

</template>

<style>
@import "@/styles/view/homepage/_new-header.scss";
</style>
