`
<template>
  <div class="w-full mt-5">
    <div class="w-[60%] mx-auto text-center">
      <div class="flex items-center justify-center mx-auto">
        <Logo class="w-20 h-20 fill-red-800 mr-5" />
        <div class="flex-grow-0 self-center text-3xl font-extrabold text-red-800">
          REHVIVAHETUSE BRONEERIMINE
        </div>
      </div>
      <div class="w-full border-2 rounded-2xl border-red-800 mx-auto mt-5 justify-center p-2">
        <div>
          <Filter @filter="getAvailableTimes" :workshops="workshops" ref="filterRef"></Filter>
        </div>
      </div>
      <div class="mt-2">
        <AvailableTimesTable
          :workshopsWithAvailableTimes="workshopsWithAvailableTimes"
          :pageSize="pageSize"
          @toggleModal="toggleModal"
        ></AvailableTimesTable>
      </div>
    </div>
    <BookingModal
      v-model="showModal"
      :availableTime="selectedAvailableTime"
      :workshop="selectedWorkshop"
      :showForm="showForm"
      @closeModal="toggleModal"
      @setShowFormToFalse="showForm = false"
      @MakeBooking="bookAvailableTime"
    ></BookingModal>
  </div>
</template>

<script>
import Filter from '@/components/Filters.vue'
import AvailableTimesTable from '@/components/AvailableTimesTable.vue'
import BookingModal from '@/components/BookingModal.vue'
import { format } from 'date-fns'
import { ModalsContainer, VueFinalModal } from 'vue-final-modal'

export default {
  name: 'HomeView',
  components: { Filter, AvailableTimesTable, BookingModal, ModalsContainer, VueFinalModal },
  inject: ['eventBus'],
  data: () => {
    return {
      workshops: [],
      workshopsWithAvailableTimes: [],
      pageSize: Number,
      showModal: false,
      showForm: true,
      selectedAvailableTime: {},
      selectedWorkshop: {}
    }
  },
  methods: {
    getAvailableTimes(filterSettings) {
      this.pageSize = filterSettings.pageSize
      this.$http
        .post('/api/available-time-filter', filterSettings)
        .then((response) => {
          this.workshopsWithAvailableTimes = response.data
        })
        .catch((error) => {
          this.eventbus.emit('show-alert', {
            alertType: 'danger',
            alertText: error.message + '<br />' + error.response.data.message
          })
        })
    },
    toggleModal(availableTime, workshop) {
      this.showModal = !this.showModal
      this.showForm = true
      this.selectedAvailableTime = availableTime
      this.selectedWorkshop = workshop
    },
    bookAvailableTime(requestBody) {
      this.$http
        .post('/api/book-time', requestBody)
        .then((response) => {
          this.showForm = false
          this.$refs.filterRef.submitSearch()
        })
        .catch((error) => {
          this.eventBus.emit('show-alert', {
            alertType: 'danger',
            alertText: error.message + '<br />' + error.response.data.message
          })
        })
    }
  },

  created() {
    this.$http
      .get('/api/workshops')
      .then((response) => {
        this.workshops = response.data
      })
      .catch((error) => {
        this.eventBus.emit('show-alert', {
          alertType: 'danger',
          alertText: error.message + '<br />' + error.response.data.message
        })
      })
  }
}
</script>
<script setup>
import Logo from '../styles/assets/car-with-spare-tire-svgrepo-com.svg?component'
</script>
