<template>
  <Title>ListMan</Title>
  <main class="page">
    <div class="loader" v-if="isUserLoading"></div>
    <div class="login" v-if="!isUserLoading && !user">
      <div class="login__header">
        <p>You must login to use this app</p>
        <button @click="toggleColorMode">
          <Icon
            v-if="$colorMode.preference === 'dark'"
            :size="24"
            name="mdi:weather-sunny"
          />
          <Icon v-else name="mdi:weather-night" :size="24" />
        </button>
      </div>
      <form class="login__form" @submit.prevent="submitLoginForm">
        <input type="text" name="name" placeholder="Username" required />
        <input
          type="password"
          name="password"
          placeholder="Password"
          required
        />
        <button :class="{ 'login__submit--loading': isLoggingIn }">
          Submit
        </button>
      </form>
    </div>
    <div class="listman" v-if="!isUserLoading && user">
      <header>
        <h2>ListMan</h2>
        <nav>
          <button @click="toggleColorMode" class="listman__color-mode">
            <Icon
              v-if="$colorMode.preference === 'dark'"
              :size="24"
              name="mdi:weather-sunny"
            />
            <Icon v-else name="mdi:weather-night" :size="24" />
          </button>
        </nav>
      </header>
      <div class="listman__app">
        <form class="listman__item-form" @submit.prevent="addItem">
          <textarea
            required
            v-model="listItemInput.text"
            name="text"
            rows="3"
            placeholder="Type Here..."
            @keypress.enter.prevent="addItem"
          ></textarea>
          <div class="listman__tag-picker">
            <button
              @click="listItemInput.tag = 'teal'"
              type="button"
              class="listman__tag-teal"
              :class="{ 'listman__tag-active': listItemInput.tag === 'teal' }"
            ></button>
            <button
              @click="listItemInput.tag = 'red'"
              type="button"
              class="listman__tag-red"
              :class="{ 'listman__tag-active': listItemInput.tag === 'red' }"
            ></button>
            <button
              @click="listItemInput.tag = 'yellow'"
              type="button"
              class="listman__tag-yellow"
              :class="{ 'listman__tag-active': listItemInput.tag === 'yellow' }"
            ></button>
            <button
              @click="listItemInput.tag = 'green'"
              type="button"
              class="listman__tag-green"
              :class="{ 'listman__tag-active': listItemInput.tag === 'green' }"
            ></button>
            <button
              @click="listItemInput.tag = 'blue'"
              type="button"
              class="listman__tag-blue"
              :class="{ 'listman__tag-active': listItemInput.tag === 'blue' }"
            ></button>
          </div>
          <button>
            <Icon :size="16" name="mdi:send-outline" />
          </button>
        </form>
      </div>
      <footer>
        <p>Logged in as {{ user.name }}</p>
        <button @click="logout">Logout</button>
      </footer>
    </div>
  </main>
</template>

<script setup lang="ts">
const sleep = (seconds: number) =>
  new Promise((r) => setTimeout(r, seconds * 1000));

// model type declarations
type User = { name: string; id: number };
type ListItem = {
  id: number;
  text: string;
  tag: "red" | "green" | "yellow" | "blue" | "teal";
};

// color mode & toggle
const colorMode = useColorMode();
const toggleColorMode = () => {
  if (colorMode.preference === "dark") {
    colorMode.preference = "light";
  } else {
    colorMode.preference = "dark";
  }
};

// user loading
const isUserLoading = ref(true);
const user = ref<User | null>(null);

onMounted(async () => {
  const authToken = localStorage.getItem("auth_token");

  if (!authToken) {
    isUserLoading.value = false;
    return;
  }

  await sleep(1);
  isUserLoading.value = false;
  // TODO: hit user info api; if success set auth_token and user variable
});

// login logout
const isLoggingIn = ref(false);

const submitLoginForm = async (ev: Event) => {
  const form = ev.currentTarget;
  if (!(form instanceof HTMLFormElement)) return;

  const nameEl = form.elements.namedItem("name");
  const passwordEl = form.elements.namedItem("password");

  if (!(nameEl instanceof HTMLInputElement)) return;
  if (!(passwordEl instanceof HTMLInputElement)) return;

  isLoggingIn.value = true;
  await sleep(2);
  isLoggingIn.value = false;

  user.value = {
    name: nameEl.value,
    id: 1,
  };
};
const logout = () => {
  user.value = null;
  localStorage.removeItem("access_token");
};

// creating list item
const listItemInput = reactive({
  text: "",
  tag: "blue" as ListItem["tag"],
});
const items = ref<ListItem[]>([]);

const addItem = () => {
  if (!listItemInput.text) return;

  const newItem: ListItem = {
    id: items.value.length,
    text: listItemInput.text,
    tag: listItemInput.tag,
  };

  items.value.push(newItem);
  listItemInput.tag = "blue";
  listItemInput.text = "";
};
</script>

<style>
.page {
  /* COLORS */
  --bg-main: #fff;
  --bg-effect-1: #eee;
  --bg-effect-2: #e4e4e4;
  --text-main: #222;
  --text-muted: #888;

  --danger: #f44343;
  --red: #e41d1d;
  --blue: #8250ff;
  --green: #50ef0c;
  --yellow: #e5d34a;
  --teal: #0cd6f1;

  color: var(--text-main);

  background-color: var(--bg-effect-1);
  padding: 1rem;
  min-height: 100vh;

  background-image: repeating-linear-gradient(
      -45deg,
      transparent 0,
      transparent 80px,
      var(--bg-effect-2) 84px,
      transparent 88px
    ),
    repeating-linear-gradient(
      45deg,
      transparent 0,
      transparent 80px,
      var(--bg-effect-2) 84px,
      transparent 88px
    );
}
.dark-mode .page {
  --bg-effect-1: #171819;
  --bg-effect-2: #222427;
  --bg-main: #464955;
  --text-main: #eee;
}

.page .loader {
  width: 4rem;
  height: 4rem;
  margin: auto;
  margin-top: 4rem;
  border-radius: 1.5rem;
  border: 0.5rem solid lightcoral;
  animation: spin 0.5s linear infinite;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }

  100% {
    transform: rotate(calc(100 / 100 * 360deg));
  }
}

.page .login {
  background-color: var(--bg-main);
  max-width: 480px;
  margin: auto;
  margin-top: 4rem;
  padding: 1rem;
  border-radius: 1rem;
}

.login p {
  text-transform: uppercase;
  font-size: 1rem;
  margin-bottom: 1rem;
}
.login__form {
  display: grid;
}

.login__header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.login__header p {
  letter-spacing: 2px;
}
.login__header button {
  cursor: pointer;
  color: var(--text-main);
}
.login__form input,
.login button {
  padding: 0.75rem 1rem;
  font-size: 1rem;
  margin-bottom: 1rem;
  border-radius: 0.5rem;
  background: none;
  border: none;
}

.login__form input:focus,
.login__form button:focus {
  outline: none;
}

.login__form input {
  box-shadow: inset 0 -1px 6px -4px var(--text-main);
  color: var(--text-main);
}

.login__form button {
  --submit-width: calc(6ch + 1.5rem);
  cursor: pointer;
  background-color: var(--text-main);
  color: var(--bg-main);
  position: relative;
  width: var(--submit-width);
  text-align: left;
}

.login__form .login__submit--loading {
  --spinner-size: 16px;
  text-align: left;
  width: calc(var(--submit-width) + 0.75rem + var(--spinner-size));
  pointer-events: none;
}
.login__form .login__submit--loading::before {
  content: "";
  box-sizing: border-box;
  position: absolute;
  right: 0.75rem;
  top: calc(50% - var(--spinner-size) / 2);
  width: var(--spinner-size);
  height: var(--spinner-size);
  border: 2px solid var(--bg-main);
  border-radius: 50%;
  border-bottom-color: transparent;
  animation: spin 0.5s linear infinite;
}

.listman {
  --container-radius: 1rem;
  background-color: var(--bg-main);
  min-height: calc(100vh - 8rem);
  max-width: 720px;
  margin: auto;
  border-radius: var(--container-radius);
  display: flex;
  flex-direction: column;
}

.listman button {
  cursor: pointer;
  background: none;
  border: none;
}

.listman button:focus {
  outline: none;
}

.listman header {
  display: flex;
  padding: 1rem;
  justify-content: space-between;
  align-items: center;
}

.listman header button {
  width: 2rem;
  height: 2rem;
  border-radius: 50%;
  display: grid;
  place-content: center;
  color: var(--text-main);
}

.listman footer {
  display: flex;
  color: var(--text-muted);
  justify-content: center;
  align-items: center;
  padding: 0.5rem;
  margin: 0.5rem;
  border-radius: 0.25rem 0.25rem calc(var(--container-radius) - 0.25rem)
    calc(var(--container-radius) - 0.25rem);
  font-size: 0.75rem;
}

.listman footer button {
  color: var(--danger);
  font-size: 0.75rem;
  margin-left: 0.5rem;
}
.listman footer button:hover {
  text-decoration: underline;
}
.listman__app {
  flex-grow: 1;
  padding: 0 2rem;
  display: flex;
  flex-direction: column;
}

.listman__item-form {
  display: grid;
  grid-template-areas:
    "input input input input input input input ."
    "input input input input input input input button";
  column-gap: 0.5rem;
  margin-top: auto;
}

.listman__item-form textarea {
  grid-area: input;
  resize: none;
  font-size: 1rem;
  padding: 0.5rem;
  background-color: transparent;
  border: 1px solid var(--text-muted);
  color: var(--text-main);
  border-radius: 0.5rem;
}
.listman__item-form textarea:focus {
  outline: none;
}
.listman__tag-picker {
  grid-area: input;
  align-self: end;
  justify-self: end;
  margin: 0.5rem;
  display: grid;
  grid-auto-flow: column;
  grid-auto-columns: 1rem;
  column-gap: 0.25rem;
}

.listman__tag-picker > button {
  height: 1rem;
  border-radius: 50%;
}

.listman__tag-picker .listman__tag-red {
  background: var(--red);
}
.listman__tag-picker .listman__tag-yellow {
  background: var(--yellow);
}
.listman__tag-picker .listman__tag-green {
  background: var(--green);
}
.listman__tag-picker .listman__tag-blue {
  background: var(--blue);
}
.listman__tag-picker .listman__tag-teal {
  background: var(--teal);
}

.listman__tag-active {
  outline-offset: 1px;
  outline: 2px solid var(--text-main);
}

.listman__item-form > button {
  background-color: var(--text-main);
  grid-area: button;
  border-radius: 0.5rem;
  padding: 0.5rem 0;
  color: var(--bg-main);
}
</style>
