<template>
  <div class="grid grid-cols-2 gap-4">
    <div
      v-for="(workshop, index) in workshops"
      class="hover:bg-red-300 cursor-pointer active:bg-red-500"
      @click="toggleWorkshopSelect(workshop)"
      :class="{
        'rounded-tl-2xl': index == 0,
        'rounded-tr-2xl': index == 1,
        'bg-red-200': !filterForm.selectedWorkshops.includes(workshop),
        'bg-red-400': filterForm.selectedWorkshops.includes(workshop)
      }"
    >
      {{ workshop.name }}<br />
      {{ workshop.location }}<br />
      <span v-for="(vehicleType, index) in workshop.vehicleTypes"
        >{{ $t(`vehicleTypes.${vehicleType}`)
        }}<span v-if="index < workshop.vehicleTypes.length - 1">, </span>
      </span>
    </div>
    <div
      class="p-5 hover:bg-red-300"
      :class="{
        'bg-red-200': !filterForm.from && !filterForm.until,
        'bg-red-400': filterForm.from || filterForm.until
      }"
    >
      <div class="m-2">
        Alates:
        <input type="date" v-model="filterForm.from" :min="minFromDate" :max="maxUntilDate" />
      </div>
      <div class="m-2">
        Kuni:
        <input type="date" v-model="filterForm.until" :min="minFromDate" :max="maxUntilDate" />
      </div>
    </div>
    <div class="grid grid-cols-2 gap-4">
      <div
        v-for="(vehicleType, index) in availableVehicleTypes"
        class="hover:bg-red-300 cursor-pointer self-center h-full flex"
        :class="{
          'bg-red-400': filterForm.selectedVehicleTypes.includes(vehicleType),
          'bg-red-200': !filterForm.selectedVehicleTypes.includes(vehicleType)
        }"
        @click="toggleVehicleTypeSelect(vehicleType)"
      >
        <div class="m-auto">{{ $t(`vehicleTypes.${vehicleType}`) }}</div>
      </div>
    </div>
    <div class="col-span-2 bg-red-200 rounded-b-xl">
      Näita
      <select class="bg-red-200 m-1" v-model="filterForm.pageSize">
        <option selected>12</option>
        <option>24</option>
        <option>48</option>
      </select>
    </div>
  </div>
</template>
<script>
import { format, isAfter, isBefore, isPast, isToday } from 'date-fns'

export default {
  name: 'Filters',
  props: ['workshops'],
  emits: ['filter'],
  inject: ['eventBus'],
  data() {
    return {
      timer: null,
      filterForm: {
        selectedWorkshops: [],
        from: format(new Date(), 'yyyy-MM-dd'),
        until: '',
        selectedVehicleTypes: [],
        pageSize: 12,
        pageNumber: 0
      }
    }
  },
  methods: {
    toggleWorkshopSelect(item) {
      if (this.filterForm.selectedWorkshops.includes(item)) {
        this.filterForm.selectedWorkshops = this.filterForm.selectedWorkshops.filter(
          (workshop) => workshop !== item
        )
        this.filterForm.selectedVehicleTypes = []
        this.filterForm.selectedWorkshops.forEach((sWorkshop) => {
          this.filterForm.selectedVehicleTypes.push(...sWorkshop.vehicleTypes)
        })
      } else {
        this.filterForm.selectedWorkshops.push(item)
        item.vehicleTypes.forEach((vehicleType) => {
          if (this.filterForm.selectedVehicleTypes.indexOf(vehicleType) === -1) {
            this.filterForm.selectedVehicleTypes.push(vehicleType)
          }
        })
      }
    },
    toggleVehicleTypeSelect(vehicleType) {
      if (this.filterForm.selectedVehicleTypes.includes(vehicleType)) {
        this.filterForm.selectedVehicleTypes = this.filterForm.selectedVehicleTypes.filter(
          (selectedVehicleType) => {
            return selectedVehicleType !== vehicleType
          }
        )

        this.filterForm.selectedWorkshops = this.workshops.filter((workshop) => {
          return this.filterForm.selectedVehicleTypes.some((sVehicleType) => {
            return workshop.vehicleTypes.includes(sVehicleType)
          })
        })
      } else {
        this.filterForm.selectedVehicleTypes.push(vehicleType)
        this.filterForm.selectedWorkshops = this.workshops.filter((workshop) =>
          workshop.vehicleTypes.some((wsVehicleType) =>
            this.filterForm.selectedVehicleTypes.includes(wsVehicleType)
          )
        )
      }
    },
    submitSearch() {
      clearTimeout(this.timer)
      this.timer = setTimeout(() => {
        this.$emit('filter', this.filterForm)
      }, 500)
    },
    validateDates() {
      if (
        isAfter(this.filterForm.from, this.filterForm.until) ||
        (!isToday(this.filterForm.from) && isPast(this.filterForm.from))
      ) {
        return false
      }

      if (this.filterForm.until !== '') {
        if (
          isBefore(this.filterForm.until, this.filterForm.from) ||
          isPast(this.filterForm.until)
        ) {
          return false
        }
      }
      return true
    },
    nextPage(workshopId) {}
  },
  computed: {
    availableVehicleTypes() {
      let types = []
      this.workshops.forEach((workshop) => {
        workshop.vehicleTypes.forEach((vehicleType) => {
          if (!types.includes(vehicleType)) {
            types.push(vehicleType)
          }
        })
      })
      return types
    },
    minFromDate() {
      return format(new Date(), 'yyyy-MM-dd')
    },
    maxUntilDate() {
      let currentMaxDate = format(new Date(), 'yyyy-MM-dd')
      this.workshops.forEach((workshop) => {
        workshop.endpointSettings[0].queryParams.forEach((param) => {
          let paramDate = format(new Date(param.defaultValue), 'yyyy-MM-dd')
          if (param.name === 'until' && isAfter(paramDate, currentMaxDate)) {
            currentMaxDate = paramDate
          }
        })
      })
      return currentMaxDate
    }
  },
  watch: {
    filterForm: {
      handler() {
        if (this.validateDates()) {
          this.submitSearch()
        } else {
          this.eventBus.emit('show-alert', {
            alertType: 'danger',
            alertText:
              'Viga ajavahemikus. Kuupäevad ei tohi olla minevikus ning "Alates" kuupäev ei tohi olla varasem kui "Kuni" kuupäev.'
          })
        }
      },
      deep: true
    }
  }
}
</script>
