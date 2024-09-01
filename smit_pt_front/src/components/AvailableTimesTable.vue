<template>
  <div v-for="workshop in workshopsWithAvailableTimes" class="text-red-100 mb-2" :key="workshop.id">
    <div class="bg-red-400 text-start p-2 rounded-t-md">
      <h2>{{ workshop.name }}</h2>
      <span v-for="(vehicleType, index) in workshop.vehicleTypes"
        >{{ $t(`vehicleTypes.${vehicleType}`)
        }}<spam v-if="index != workshop.vehicleTypes.length - 1"> | </spam>
      </span>
      <div class="flex justify-between">
        <div>
          {{ workshop.location }}
        </div>
        <div v-if="workshop.availableTimes.length > 0">
          <div class="mr-5">
            <span
              class="mx-1 bg-red-500 p-1 rounded-md h-1 cursor-pointer 'hover:bg-red-600'"
              :class="{ hidden: workshop.currentPage < 1 }"
              @click="previousPage(workshop)"
              ><</span
            >
            <span :class="{ 'mr-7': workshop.currentPage + 1 == workshop.lastPage }"
              >{{ workshop.currentPage + 1 }}/{{ workshop.lastPage }}</span
            >
            <span
              class="mx-1 bg-red-500 p-1 rounded-md h-1 cursor-pointer hover:bg-red-600"
              :class="{
                hidden: workshop.currentPage + 1 == workshop.lastPage
              }"
              @click="nextPage(workshop)"
              >></span
            >
          </div>
        </div>
      </div>
    </div>

    <div class="w-full h-full p-2 bg-red-300 rounded-b-md">
      <div v-if="workshop.availableTimes.length > 0" class="grid grid-cols-4 gap-4">
        <div
          v-for="availableTime in pageOfAvailableTimes(workshop)"
          class="bg-red-500 hover:bg-red-600 cursor-pointer self-center h-16 p-2"
          @click="toggleModal(availableTime, workshop)"
        >
          <div>
            {{ format(availableTime.time, 'EEEEE dd.MM.yyyy', { locale: et }) }}<br />

            {{ format(availableTime.time, 'HH:mm') }}
          </div>
        </div>
        <!-- <BookingModal
          v-model="showModal"
          :availableTime="selectedAvailableTime"
          :workshop="workshop"
          :showForm="showForm"
          @closeModal="toggleModal"
          @setShowFormToFalse="showForm = false"
        ></BookingModal> -->
      </div>
      <div v-else class="col-span-4 text-white">
        Vabasid aegasid ei ole!<br />
        <span class="text-sm">Proovige muuta ajavahemikku</span>
      </div>
    </div>
  </div>
</template>
<script>
import { format } from 'date-fns'
import BookingModal from './BookingModal.vue'
export default {
  name: 'AvailableTimesTable',
  props: ['workshopsWithAvailableTimes', 'pageSize'],
  components: { BookingModal },
  data() {
    return {
      showModal: false
    }
  },
  methods: {
    nextPage: function (workshop) {
      workshop.currentPage++
    },
    previousPage: function (workshop) {
      workshop.currentPage--
    },
    pageOfAvailableTimes(workshop) {
      return workshop.availableTimes.slice(
        workshop.currentPage * this.pageSize,
        (workshop.currentPage + 1) * this.pageSize
      )
    },
    toggleModal(availableTime, workshop) {
      this.$emit('toggleModal', availableTime, workshop)
    }
  },
  computed: {}
}
</script>
<script setup>
import { format } from 'date-fns'
import { VueFinalModal } from 'vue-final-modal'
import et from 'date-fns/locale/et'
</script>
