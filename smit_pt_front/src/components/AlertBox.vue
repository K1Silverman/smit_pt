<template>
  <div
    class="max-w-[40%] flex justify-end fixed z-[9999] px-5 transition-all duration-1000 ease-in-out top-4"
    :class="[show ? 'right-0' : '-right-full']"
  >
    <div
      role="alert"
      class="flex items-start text-sm w-full gap-2 bg-white shadow-lg justify-between"
      :class="[
        {
          'text-red-800 border border-red-500 ': variant === 'danger',
          'text-green-800 border border-green-500 ': variant === 'success'
        }
      ]"
    >
      <div class="flex h-full">
        <div
          class="h-full flex justify-center items-center text-white px-4"
          :class="[
            {
              'bg-red-600': variant === 'danger',
              'bg-green-600': variant === 'success'
            }
          ]"
        ></div>
        <div v-html="text" class="p-4 pe-0"></div>
      </div>
      <div class="p-3 cursor-pointer" @click="closeAlertBox">
        <a class="w-5 h-5 fa-solid fa-xmark"></a>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name: 'AlertBox',
  props: {
    variant: {
      type: String,
      default: 'success'
    },
    text: {
      type: String,
      default: ''
    },
    show: {
      type: Boolean,
      default: false
    },
    closeAlertBox: {
      type: Function,
      default: false
    }
  }
}
</script>
