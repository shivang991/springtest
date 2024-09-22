<template>
  <Title> Home </Title>
  <div class="hero">
    <h1 class="hero__title">
      {{ currentText }} <span class="hero__cursor"></span>
    </h1>
    <div class="hero__design-right">
      <NuxtImg
        class="hero__img"
        src="/img/shunsui.png"
        :width="200"
        :height="200"
      />
      <h2>Lorem ipsum dolor sit.</h2>
      <p>
        Lorem ipsum dolor sit amet consectetur adipisicing elit. Minima, ipsum
        sit? Assumenda veritatis fuga unde porro facilis accusantium obcaecati
        ipsam.
      </p>
      <div class="hero__btns">
        <div class="hero__btns-dots">
          <span></span>
          <span></span>
        </div>
        <button class="hero__btn" @click="toggleColorMode">
          {{ $colorMode.preference }} Mode
        </button>
        <NuxtLink class="hero__btn" href="/about"> About Page </NuxtLink>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
const lines = [
  "World Class Website",
  "Highly Animated",
  "Blazingly Fast",
  "Statically Generated",
];

export default {
  data() {
    return {
      currentLineIndex: 0,
      currentText: "",
      intervalWaitSecs: 0,
      intervalId: null as NodeJS.Timeout | null,
    };
  },

  methods: {
    incrementCurrentLine() {
      const newLineIndex = this.currentLineIndex + 1;
      if (newLineIndex >= lines.length) this.currentLineIndex = 0;
      else this.currentLineIndex = newLineIndex;
    },

    animate() {
      let newTextSize = this.currentText.length + 1;

      if (this.intervalWaitSecs === 2) {
        this.intervalWaitSecs = 0.05;
        newTextSize = 1;
      }
      const currentLine = lines[this.currentLineIndex];
      this.currentText = currentLine.slice(0, newTextSize);
      if (this.currentText.length >= currentLine.length) {
        this.incrementCurrentLine();
        this.intervalWaitSecs = 2;
      }
    },

    toggleColorMode() {
      if (this.$colorMode.preference === "dark") {
        this.$colorMode.preference = "light";
      } else {
        this.$colorMode.preference = "dark";
      }
    },
  },

  mounted() {
    this.intervalWaitSecs = 0.05;
  },

  watch: {
    intervalWaitSecs(newVal: number) {
      if (this.intervalId) clearInterval(this.intervalId);
      this.intervalId = setInterval(() => this.animate(), newVal * 1000);
    },
  },

  unmounted() {
    if (this.intervalId) clearInterval(this.intervalId);
  },
};
</script>

<style scoped>
.hero {
  --light-primary: #f9d8f0;
  --primary: #ff006f;
  --text: #222;
  padding: 4rem;
  padding-top: 4rem;
  border-top: 1rem solid var(--primary);
  display: grid;
}

.dark-mode .hero {
  --light-primary: #410c1b;
  --text: white;
}
.hero > * {
  grid-column-start: 1;
  grid-row-start: 1;
}
.hero__title {
  font-size: 16ch;
  line-height: 144px;
  font-weight: semibold;
  word-break: break-all;
  color: var(--text);
  max-width: 512px;
  z-index: 1;
}
.hero__cursor {
  display: inline-block;
  background-color: var(--primary);
  width: 40px;
  height: 144px;
  animation: cursor 500ms ease-out infinite;
  transform-origin: left;
  margin-left: -4rem;
}
@keyframes cursor {
  0% {
    transform: scaleX(0);
  }
  25% {
    transform: scaleX(1);
  }
  75% {
    transform: scaleX(1);
  }
  100% {
    transform: scaleX(0);
  }
}

.hero__design-right {
  width: 512px;
  height: 712px;
  justify-self: end;
  transform: skewY(-4deg);
  box-shadow: 4px 8px 0 var(--text), 10px 24px 0 var(--light-primary);
  background-image: repeating-linear-gradient(
      45deg,
      var(--light-primary) 0,
      var(--light-primary) 10px,
      transparent 10px,
      transparent 12px
    ),
    repeating-linear-gradient(
      -45deg,
      var(--light-primary) 0,
      var(--light-primary) 10px,
      transparent 10px,
      transparent 18px
    );
  transition: box-shadow 0.5s ease-out, transform 0.25s ease-out;
  padding: 2rem;
}

.hero__design-right:hover {
  box-shadow: 2px 2px 0 var(--text), 4px 8px 0 var(--light-primary);
  transform: skew(0);
}
.hero__design-right h2 {
  font-size: 2rem;
  color: var(--text);
  letter-spacing: 2px;
  text-transform: uppercase;
}
.hero__design-right p {
  font-size: 1rem;
  line-height: 1.5;
  color: var(--text);
  letter-spacing: 2px;
}
.hero__img {
  border-radius: 50%;
  margin-bottom: 2rem;
  box-shadow: 2rem 2rem 2px var(--light-primary);
}

.hero__btns {
  margin-top: 4rem;
  display: grid;
  grid-template-columns: repeat(3, 1fr);
}
.hero__btns-dots {
  display: grid;
  grid-auto-columns: 1rem;
  grid-auto-flow: column;
  column-gap: 0.5rem;
  grid-column: 1 / span 3;
}
.hero__btns-dots span {
  width: 1rem;
  height: 1rem;
  background-color: var(--primary);
  border-radius: 50%;
  margin-bottom: 1rem;
}

.hero__btn {
  padding: 0.5rem 1rem;
  margin-right: 0.25rem;
  font-size: 0.875rem;
  text-transform: uppercase;
  letter-spacing: 2px;
  border: none;
  cursor: pointer;
  background: none;
  border: 1px solid var(--primary);
  color: var(--primary);
  text-decoration: none;
  display: inline-block;
  transition: transform 0.1s ease-out;
  position: relative;
  overflow: hidden;
}

.hero__btn::before {
  content: "";
  position: absolute;
  inset: 0;
  background-color: var(--bg);
  z-index: -10;
  transform: scale(0);
  transition: transform 0.1s ease-out;
  transform-origin: bottom;
  box-shadow: 0 8px 8px 10px var(--light-primary) inset;
}

.hero__btn:hover::before {
  transform: scale(1);
}

.hero__btn:hover {
  transform: scale(1.05);
}
</style>
