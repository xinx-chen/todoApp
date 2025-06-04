// stores/sidebar.js
import { defineStore } from 'pinia'

export const useSidebarStore = defineStore('sidebar', {
    state: () => ({
        currentView: 'all',
        selectedDate: new Date()
    }),
    actions: {
        setView(view) {
            this.currentView = view
        },
        setSelectedDate(date) {
            this.selectedDate = date
        }
    }
})
