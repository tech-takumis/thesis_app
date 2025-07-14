<template>
  <div class="flex h-screen bg-gray-100">
    <!-- Sidebar -->
    <div class="hidden md:flex md:w-64 md:flex-col">
      <div class="flex flex-col flex-grow pt-5 overflow-y-auto bg-white border-r border-gray-200">
        <!-- Logo -->
        <div class="flex items-center flex-shrink-0 px-4">
          <img 
            src="https://hebbkx1anhila5yf.public.blob.vercel-storage.com/pcic.jpg-ymdsA0RBXJ1O58Wx4oDrmGSD8rRBY0.jpeg" 
            alt="PCIC Logo" 
            class="h-10 w-auto"
          />
          <div class="ml-3">
            <h1 class="text-lg font-semibold text-gray-900">PCIC</h1>
            <p class="text-xs text-gray-500">{{ roleTitle }}</p>
          </div>
        </div>

        <!-- Navigation -->
        <nav class="mt-8 flex-1 px-2 space-y-1">
          <template v-for="item in navigation" :key="item.name">
            <!-- Single Navigation Item -->
            <router-link
              v-if="!item.children"
              :to="item.href"
              :class="[
                isActive(item.href) 
                  ? 'bg-green-100 text-green-900 border-r-2 border-green-500' 
                  : 'text-gray-600 hover:bg-gray-50 hover:text-gray-900',
                'group flex items-center px-2 py-2 text-sm font-medium rounded-md transition-colors'
              ]"
            >
              <component 
                :is="item.icon" 
                :class="[
                  isActive(item.href) ? 'text-green-500' : 'text-gray-400 group-hover:text-gray-500',
                  'mr-3 h-5 w-5'
                ]" 
              />
              {{ item.name }}
            </router-link>

            <!-- Navigation Group with Children -->
            <div v-else>
              <button
                @click="toggleGroup(item.name)"
                :class="[
                  'text-gray-600 hover:bg-gray-50 hover:text-gray-900',
                  'group w-full flex items-center justify-between px-2 py-2 text-sm font-medium rounded-md transition-colors'
                ]"
              >
                <div class="flex items-center">
                  <component 
                    :is="item.icon" 
                    class="text-gray-400 group-hover:text-gray-500 mr-3 h-5 w-5" 
                  />
                  {{ item.name }}
                </div>
                <ChevronDown 
                  :class="[
                    expandedGroups.includes(item.name) ? 'rotate-180' : '',
                    'h-4 w-4 transition-transform'
                  ]"
                />
              </button>
              
              <!-- Submenu -->
              <div 
                v-show="expandedGroups.includes(item.name)"
                class="mt-1 space-y-1"
              >
                <router-link
                  v-for="child in item.children"
                  :key="child.name"
                  :to="child.href"
                  :class="[
                    isActive(child.href)
                      ? 'bg-green-50 text-green-700 border-r-2 border-green-500'
                      : 'text-gray-600 hover:bg-gray-50 hover:text-gray-900',
                    'group flex items-center pl-11 pr-2 py-2 text-sm font-medium rounded-md transition-colors'
                  ]"
                >
                  {{ child.name }}
                </router-link>
              </div>
            </div>
          </template>
        </nav>

        <!-- User Profile Section -->
        <div class="flex-shrink-0 border-t border-gray-200 p-4">
          <div class="flex items-center">
            <div class="flex-shrink-0">
              <div class="h-8 w-8 rounded-full bg-green-500 flex items-center justify-center">
                <span class="text-sm font-medium text-white">
                  {{ userInitials }}
                </span>
              </div>
            </div>
            <div class="ml-3 flex-1">
              <p class="text-sm font-medium text-gray-900 truncate">
                {{ store.userFullName }}
              </p>
              <p class="text-xs text-gray-500 truncate">
                {{ store.userEmail }}
              </p>
            </div>
            <button
              @click="handleLogout"
              class="ml-2 p-1 rounded-md text-gray-400 hover:text-gray-600 hover:bg-gray-100 transition-colors"
              title="Logout"
            >
              <LogOut class="h-4 w-4" />
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Mobile sidebar -->
    <div v-if="sidebarOpen" class="fixed inset-0 z-40 md:hidden">
      <div class="fixed inset-0 bg-gray-600 bg-opacity-75" @click="sidebarOpen = false"></div>
      <div class="relative flex-1 flex flex-col max-w-xs w-full bg-white">
        <div class="absolute top-0 right-0 -mr-12 pt-2">
          <button
            @click="sidebarOpen = false"
            class="ml-1 flex items-center justify-center h-10 w-10 rounded-full focus:outline-none focus:ring-2 focus:ring-inset focus:ring-white"
          >
            <X class="h-6 w-6 text-white" />
          </button>
        </div>
        <!-- Mobile navigation content (same as desktop) -->
        <div class="flex-1 h-0 pt-5 pb-4 overflow-y-auto">
          <div class="flex-shrink-0 flex items-center px-4">
            <img 
              src="https://hebbkx1anhila5yf.public.blob.vercel-storage.com/pcic.jpg-ymdsA0RBXJ1O58Wx4oDrmGSD8rRBY0.jpeg" 
              alt="PCIC Logo" 
              class="h-8 w-auto"
            />
            <span class="ml-2 text-lg font-semibold">PCIC</span>
          </div>
          <nav class="mt-5 px-2 space-y-1">
            <!-- Same navigation as desktop -->
          </nav>
        </div>
      </div>
    </div>

    <!-- Main content -->
    <div class="flex flex-col flex-1 overflow-hidden">
      <!-- Top bar for mobile -->
      <div class="md:hidden bg-white border-b border-gray-200 px-4 py-2 flex items-center justify-between">
        <button
          @click="sidebarOpen = true"
          class="p-2 rounded-md text-gray-400 hover:text-gray-600 hover:bg-gray-100"
        >
          <Menu class="h-6 w-6" />
        </button>
        <h1 class="text-lg font-semibold text-gray-900">{{ pageTitle }}</h1>
        <div class="w-10"></div> <!-- Spacer for centering -->
      </div>

      <!-- Page header -->
      <header v-if="$slots.header" class="bg-white shadow-sm border-b border-gray-200">
        <div class="px-4 py-4 sm:px-6 lg:px-8">
          <slot name="header" />
        </div>
      </header>

      <!-- Main content area -->
      <main class="flex-1 overflow-y-auto bg-gray-50">
        <div class="p-4 sm:p-6 lg:p-8">
          <slot />
        </div>
      </main>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute } from 'vue-router'
import { 
  Menu, X, LogOut, ChevronDown,
  DollarSign, CreditCard, TrendingUp, FileText, Settings, Users
} from 'lucide-vue-next'
import { useUserStore } from '@/stores/user'

const store = useUserStore()
const route = useRoute()

const props = defineProps({
  navigation: {
    type: Array,
    default: () => []
  },
  roleTitle: {
    type: String,
    default: 'Staff Portal'
  },
  pageTitle: {
    type: String,
    default: 'Dashboard'
  }
})

const sidebarOpen = ref(false)
const expandedGroups = ref([])

const userInitials = computed(() => {
  const name = store.userFullName
  return name.split(' ').map(n => n[0]).join('').toUpperCase()
})

const isActive = (href) => {
  return route.path === href || route.path.startsWith(href + '/')
}

const toggleGroup = (groupName) => {
  const index = expandedGroups.value.indexOf(groupName)
  if (index > -1) {
    expandedGroups.value.splice(index, 1)
  } else {
    expandedGroups.value.push(groupName)
  }
}

const handleLogout = () => {
  store.logout()
}
</script>