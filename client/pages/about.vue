<template>
  <Title> About Us </Title>
  <div class="hero">
    <div class="hero__left">
      <div class="hero__left-img">
        <NuxtImg src="/img/renji.png" width="400" height="400" />
      </div>
      <div class="hero__left-img">
        <NuxtImg src="/img/toshirou.png" width="400" height="400" />
      </div>
    </div>
    <h1>
      AB
      <span>O</span>
      U<span>T</span> <span>U</span>S
    </h1>
    <div class="hero__text">
      <p>
        Lorem ipsum dolor, sit amet consectetur adipisicing elit. Corrupti nulla
        provident id deserunt adipisci cupiditate praesentium nihil labore error
        molestias!
      </p>
      <p>
        Lorem ipsum dolor, sit amet consectetur adipisicing elit. Corrupti nulla
        provident id deserunt adipisci cupiditate praesentium nihil labore error
        molestias!
      </p>
      <p>
        Lorem ipsum, dolor sit amet consectetur adipisicing elit. Rerum omnis
        ipsa nemo quae non veniam reiciendis recusandae exercitationem quos
        voluptas, dolorum blanditiis, eligendi consequatur cum earum maxime
        magni voluptatem distinctio id. Aliquam officia ab dignissimos nam velit
        nisi vel! Eaque!
      </p>
    </div>
    <div class="hero__right">
      <h4>WATCH THIS MUSIC VIDEO</h4>
      <video src="/video.mp4" autoplay />
    </div>
    <div class="hero__color-picker">
      <p>COLOR</p>
      <div class="hero__dot"></div>
      <button @click="$colorMode.preference = 'dark'">Dark</button>
      <button @click="$colorMode.preference = 'light'">Light</button>
    </div>
  </div>
</template>

<style scoped>
.hero {
  display: grid;
  --left-width: 400px;
  --top-padding: 4rem;
  --right-height: 720px;

  /* COLORS */
  --text: #444;
  --text-heavy: #000;
  --red: #d3003f;
  --lightred: #ffadd9;
  --lightblue: #81fdff;
  --lightyellow: #ffdcbe;
  --yellow: #ffc012;
}

.dark-mode .hero {
  --text: #eee;
  --lightred: #3d119c;
  --yellow: #00264e;
  --text-heavy: white;
  --lightblue: #143863;
  --lightyellow: #124c54;
  --red: #ff45b8;
}

.hero > * {
  grid-column: 1;
  grid-row: 1;
}

.hero h1,
.hero p {
  color: var(--text);
}
.hero__left {
  display: grid;
  width: var(--left-width);
  height: max-content;
  transform: translate(0, var(--top-padding));
}

.hero__left-img img {
  filter: invert();
}
.dark-mode .hero__left-img img {
  filter: none;
}
.hero__left-img:first-child {
  width: var(--left-width);
  background-color: var(--lightblue);
}
.hero__left-img:last-child {
  width: calc(var(--left-width) - 6rem);
  transform: translate(4rem, -2rem);
  background-color: var(--lightyellow);
  height: 512px;
}
.hero__left-img:last-child img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.hero h1 {
  font-size: 8rem;
  max-width: 16rem;
  word-break: break-word;
  transform: translate(calc(var(--left-width) - 2rem), var(--top-padding));
  line-height: 1;
}

.hero h1 span {
  background-color: var(--lightred);
}

.hero__right {
  justify-self: end;
  background-color: var(--lightred);
  height: var(--right-height);
  clip-path: polygon(25% 0, 100% 0, 100% 100%, 0% 100%);
  display: grid;
  box-shadow: -4rem 1rem 0 var(--yellow) inset;
}
.hero__right h4 {
  font-size: 3rem;
  transform: rotate(90deg) translate(8rem, -4rem);
  max-width: 20rem;
  text-align: right;
  color: var(--text-heavy);
}
.hero video {
  width: 400px;
  object-fit: cover;
  bottom: 0;
  align-self: end;
  clip-path: polygon(0 15%, 100% 0, 100% 100%, 0% 100%);
}

.hero__text {
  max-width: 32ch;
  transform: translate(
    calc(var(--left-width) + 12rem),
    calc(var(--top-padding) + 12rem)
  );
  letter-spacing: 2px;
  line-height: 2;
  font-size: 1rem;
}

.hero__color-picker {
  width: max-content;
  height: max-content;
  transform: translate(-6rem, calc(var(--right-height) - 10rem)) rotate(-90deg);
  display: flex;
  align-items: center;
}

.hero__color-picker .hero__dot {
  width: 0.5rem;
  height: 0.5rem;
  background-color: var(--red);
  margin: 0 1rem;
  border-radius: 50%;
}

.hero__color-picker button {
  width: 96px;
  height: 96px;
  font-size: 1rem;
  margin-right: 0.5rem;
  text-transform: uppercase;
  background: none;
  border: none;
  cursor: pointer;
  position: relative;
  color: var(--red);
}

.hero__color-picker button::before {
  content: "";
  position: absolute;
  inset: 0;
  border: 1px solid var(--red);
  transform: rotate(45deg) scale(0.5);
  transition: transform 0.1s ease-out;
}

.hero__color-picker button:hover::before {
  transform: scale(1) rotate(45deg);
}
</style>
