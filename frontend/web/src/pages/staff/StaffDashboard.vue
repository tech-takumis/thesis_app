<template>
  <div class="min-h-screen bg-gray-50">
    <!-- Header -->
    <header class="bg-white shadow-sm border-b">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between items-center h-16">
          <div class="flex items-center">
            <img 
              src="https://hebbkx1anhila5yf.public.blob.vercel-storage.com/pcic.jpg-ymdsA0RBXJ1O58Wx4oDrmGSD8rRBY0.jpeg" 
              alt="PCIC Logo" 
              class="h-10 w-auto"
            />
            <div class="ml-4">
              <h1 class="text-xl font-semibold text-gray-900">PCIC Staff Portal</h1>
              <p class="text-sm text-gray-500">{{ store.userRole }}</p>
            </div>
          </div>
          <div class="flex items-center space-x-4">
            <span class="text-sm text-gray-700">{{ store.userFullName }}</span>
            <button
              @click="handleLogout"
              class="bg-red-600 hover:bg-red-700 text-white px-4 py-2 rounded-md text-sm font-medium transition-colors"
            >
              Logout
            </button>
          </div>
        </div>
      </div>
    </header>

    <!-- Main Content -->
    <main class="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
      <div class="px-4 py-6 sm:px-0">
        <!-- Welcome Section -->
        <div class="bg-white overflow-hidden shadow rounded-lg mb-6">
          <div class="px-4 py-5 sm:p-6">
            <h2 class="text-2xl font-bold text-gray-900 mb-2">
              Welcome, {{ store.userFullName }}!
            </h2>
            <p class="text-gray-600 mb-4">
              You are logged in as: <span class="font-semibold text-green-600">{{ store.userRole }}</span>
            </p>
            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 text-sm">
              <div>
                <span class="font-medium text-gray-700">Department:</span>
                <span class="ml-2 text-gray-600">{{ store.userDepartment }}</span>
              </div>
              <div>
                <span class="font-medium text-gray-700">Position:</span>
                <span class="ml-2 text-gray-600">{{ store.userPosition }}</span>
              </div>
              <div>
                <span class="font-medium text-gray-700">Location:</span>
                <span class="ml-2 text-gray-600">{{ store.userLocation }}</span>
              </div>
            </div>
          </div>
        </div>

        <!-- Role-Specific Dashboard Message -->
        <div class="bg-blue-50 border border-blue-200 rounded-lg p-4 mb-6">
          <div class="flex">
            <Info class="h-5 w-5 text-blue-400" />
            <div class="ml-3">
              <h3 class="text-sm font-medium text-blue-800">
                Role-Specific Dashboard
              </h3>
              <div class="mt-2 text-sm text-blue-700">
                <p>This is a fallback dashboard. You should be redirected to your role-specific dashboard:</p>
                <ul class="list-disc list-inside mt-2 space-y-1">
                  <li v-if="store.isDivisionChief">Division Chief Dashboard</li>
                  <li v-if="store.isAdminOfficer">Administrative Officer Dashboard</li>
                  <li v-if="store.isUnderwriter">Insurance Underwriter Dashboard</li>
                  <li v-if="store.isInsuranceSpecialist">Insurance Specialist Dashboard</li>
                  <li v-if="store.isClaimsProcessor">Claims Processor Dashboard</li>
                  <li v-if="store.isInsuranceAdjuster">Insurance Adjuster Dashboard</li>
                  <li v-if="store.isTeller">Teller Dashboard</li>
                </ul>
              </div>
            </div>
          </div>
        </div>

        <!-- Debug Information -->
        <div class="bg-gray-50 border border-gray-200 rounded-lg p-4">
          <h3 class="text-lg font-medium text-gray-900 mb-4">Debug Information</h3>
          <div class="space-y-2 text-sm">
            <div>
              <span class="font-medium">User Role:</span>
              <span class="ml-2 font-mono bg-gray-100 px-2 py-1 rounded">{{ store.userRole }}</span>
            </div>
            <div>
              <span class="font-medium">Is Valid Staff:</span>
              <span class="ml-2" :class="store.isValidStaff ? 'text-green-600' : 'text-red-600'">
                {{ store.isValidStaff ? 'Yes' : 'No' }}
              </span>
            </div>
            <div>
              <span class="font-medium">Role Category:</span>
              <span class="ml-2">{{ store.roleCategory }}</span>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { Info } from 'lucide-vue-next'
import { useUserStore } from '@/stores/user'

const store = useUserStore()

const handleLogout = () => {
  store.logout()
}
</script>