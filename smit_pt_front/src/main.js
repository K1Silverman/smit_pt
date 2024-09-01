import './styles/main.css'
import 'vue-final-modal/style.css'
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import mitt from 'mitt'
import VueAxios from 'vue-axios'
import axios from 'axios'
import { createI18n } from 'vue-i18n'
import en from './locales/en.json'
import et from './locales/et.json'
import { createVfm } from 'vue-final-modal'

const app = createApp(App)

const eventBus = mitt();
app.provide('eventBus', eventBus);

const vfm = createVfm();

const i18n = createI18n({
	locale: 'et',
	messages: {
		en,
		et
	}
})

app.provide('eventBus', eventBus);
app.use(VueAxios, axios);
app.use(i18n)
app.use(router)
app.use(vfm)

app.mount('#app')
