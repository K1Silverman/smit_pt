<template>
  <VueFinalModal overlay-transition="vfm-fade" content-transition="vfm-fade">
    <div v-if="showForm" class="mx-auto bg-white mt-24 w-[40%] p-10 rounded-2xl">
      <div class="mb-2">
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
          </div>
        </div>
        <div class="bg-red-300">
          {{ format(availableTime.time, 'EEEEE dd.MM.yyyy', { locale: et }) }} -
          {{ format(availableTime.time, 'HH:mm') }}
          {{ availableTime.id ?? availableTime.uuid }}
        </div>
      </div>
      <div>
        <div v-for="input in contactInformationForm" class="grid">
          <label
            >{{ input.label
            }}<span :class="{ 'text-red-500': input.required, hidden: !input.required }">
              *</span
            ></label
          >
          <input
            :key="input.key"
            :type="input.type"
            :placeholder="input.placholder"
            :required="input.required"
            :format="input.format"
            v-model="contactInformation[input.key]"
            class="border-b-2 border-gray-200"
          />
        </div>
      </div>
      <div class="flex mt-5 justify-between w-[50%] mx-auto text-white">
        <button class="bg-red-400 p-1 px-2 rounded-md hover:bg-red-500" @click="bookAvailableTime">
          Broneeri
        </button>
        <button class="bg-red-600 p-1 px-2 rounded-md hover:bg-red-700" @click="closeModal">
          Tühista
        </button>
      </div>
    </div>
    <div v-else class="mx-auto bg-white mt-24 w-[40%] p-8 rounded-2xl">
      <div class="flex justify-end">
        <button class="text-xl font-extrabold" @click="closeModal">X</button>
      </div>

      <div class="text-center">
        <div>
          <h2>Broneeritud</h2>
        </div>

        <div class="mt-2">
          {{ format(availableTime.time, 'EEEEE dd.MM.yyyy', { locale: et }) }} -
          {{ format(availableTime.time, 'HH:mm') }}
        </div>
      </div>
    </div>
  </VueFinalModal>
</template>
<script>
import { VueFinalModal } from 'vue-final-modal'

export default {
  name: 'BookingModal',
  components: { VueFinalModal },
  props: ['availableTime', 'workshop', 'showForm'],
  emits: ['closeModal', 'makeBooking'],
  data() {
    return {
      contactInformationForm: [
        {
          key: 'regNo',
          label: 'Auto Reg. No.',
          type: 'text',
          placholder: '123 ABC',
          format: '[0-9]{3} [A-Z]{3}',
          required: true
        },
        {
          key: 'name',
          label: 'Nimi',
          type: 'text',
          placholder: 'Mart Tamm',
          required: true
        },
        {
          key: 'phoneNo',
          label: 'Telefoni nr.',
          type: 'tel',
          placholder: '+372 5555 5555',
          required: true
        },
        {
          key: 'email',
          label: 'E-mail',
          type: 'text',
          placholder: 'mart.tamm@mail.com',
          required: false
        }
      ],
      contactInformation: {}
    }
  },
  methods: {
    closeModal() {
      this.$emit('closeModal')
    },
    testAlertBox() {},
    bookAvailableTime() {
      let requestBody = {
        availableTimeId: this.availableTime.id ? this.availableTime.id : this.availableTime.uuid,
        contactInformation: JSON.stringify(this.contactInformation),
        workshopId: this.workshop.id
      }

      this.$emit('makeBooking', requestBody)
    }
  }
}
</script>
<script setup>
import { format } from 'date-fns'

import et from 'date-fns/locale/et'
</script>
