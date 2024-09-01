<template>
  <AlertBox
    :variant="alert.alertType"
    :text="alert.alertText"
    :show="alert.show"
    :closeAlertBox="closeAlertBox"
    class="z-[9999]"
  ></AlertBox>
  <RouterView />
</template>

<script>
import AlertBox from '@/components/AlertBox.vue'
export default {
  name: 'App',
  components: { AlertBox },
  inject: ['eventBus'],
  data() {
    return {
      alert: {
        show: false,
        alertText: 'Error',
        alertType: 'danger',
        timeout: 10000
      }
    }
  },
  methods: {
    closeAlertBox() {
      this.alert.show = false
    }
  },
  created() {
    this.eventBus.on('show-alert', (evt) => {
      this.alert = evt
      this.alert.show = true

      if (!this.alert.timeout) {
        this.alert.timeout = 5000
      }

      if (this.alert.timeout != -1) {
        setTimeout(() => {
          this.alert.show = false
        }, this.alert.timeout)
      }
    })
  }
}
</script>
