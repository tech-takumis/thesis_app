<template>
  <AuthenticatedLayout 
    :navigation="underwriterNavigation" 
    role-title="Underwriter Portal"
    page-title="New Application Type"
  >
    <template #header>
      <div class="flex items-center justify-between">
        <div>
          <h1 class="text-2xl font-bold text-gray-900">Create New Application Type</h1>
          <p class="text-gray-600">Define the structure and fields for a new insurance application</p>
        </div>
        <router-link 
          to="/underwriter/dashboard"
          class="inline-flex items-center px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
        >
          <ArrowLeft class="h-4 w-4 mr-2" />
          Back to Dashboard
        </router-link>
      </div>
    </template>

    <div class="max-w-5xl mx-auto">
      <form @submit.prevent="submitApplication" class="space-y-6">
        <!-- Application Type Details -->
        <div class="bg-white rounded-lg shadow-sm border border-gray-200">
          <div class="px-6 py-4 border-b border-gray-200">
            <h2 class="text-lg font-semibold text-gray-900">Application Type Information</h2>
            <p class="text-sm text-gray-500">Define the basic details of this new insurance application type.</p>
          </div>
          <div class="p-6 space-y-4">
            <div>
              <label for="displayName" class="block text-sm font-medium text-gray-700 mb-1">
                Display Name <span class="text-red-500">*</span>
              </label>
              <input
                v-model="applicationType.displayName"
                type="text"
                id="displayName"
                required
                class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                placeholder="e.g., Rice Crop Insurance Application"
              />
            </div>
            <div>
              <label for="description" class="block text-sm font-medium text-gray-700 mb-1">
                Description
              </label>
              <textarea
                v-model="applicationType.description"
                id="description"
                rows="3"
                class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                placeholder="A brief description of what this application type is for."
              ></textarea>
            </div>
            <div class="flex items-center">
              <input
                v-model="applicationType.requiredAiAnalyses"
                type="checkbox"
                id="requiredAiAnalyses"
                class="h-4 w-4 text-indigo-600 border-gray-300 rounded focus:ring-indigo-500"
              />
              <label for="requiredAiAnalyses" class="ml-2 block text-sm text-gray-900">
                Requires AI Analysis (e.g., for satellite imagery, yield prediction)
              </label>
            </div>
          </div>
        </div>

        <!-- Application Fields -->
        <div class="bg-white rounded-lg shadow-sm border border-gray-200">
          <div class="px-6 py-4 border-b border-gray-200 flex items-center justify-between">
            <div>
              <h2 class="text-lg font-semibold text-gray-900">Application Fields</h2>
              <p class="text-sm text-gray-500">Define the individual fields that farmers will fill out.</p>
            </div>
            <button
              type="button"
              @click="addInsuranceField"
              class="inline-flex items-center px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
            >
              <Plus class="h-4 w-4 mr-2" />
              Add New Field
            </button>
          </div>
          
          <div class="p-6 space-y-6">
            <div v-if="insuranceFields.length === 0" class="text-center text-gray-500 py-4">
              No fields added yet. Click "Add New Field" to start.
            </div>

            <div 
              v-for="(field, index) in insuranceFields" 
              :key="index" 
              class="relative p-4 border border-gray-200 rounded-md bg-gray-50 space-y-4"
            >
              <button
                type="button"
                @click="removeInsuranceField(index)"
                class="absolute top-2 right-2 p-1 rounded-full text-gray-400 hover:text-red-600 hover:bg-red-100 transition-colors"
                title="Remove Field"
              >
                <Trash2 class="h-4 w-4" />
              </button>
              <h3 class="text-md font-semibold text-gray-800 mb-3">Field #{{ index + 1 }}</h3>

              <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <label :for="`keyName-${index}`" class="block text-sm font-medium text-gray-700 mb-1">
                    Key Name <span class="text-red-500">*</span>
                  </label>
                  <input
                    v-model="field.keyName"
                    type="text"
                    :id="`keyName-${index}`"
                    required
                    class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                    placeholder="e.g., farmer_name"
                  />
                </div>
                <div>
                  <label :for="`displayNameField-${index}`" class="block text-sm font-medium text-gray-700 mb-1">
                    Display Name <span class="text-red-500">*</span>
                  </label>
                  <input
                    v-model="field.displayName"
                    type="text"
                    :id="`displayNameField-${index}`"
                    required
                    class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                    placeholder="e.g., Farmer's Full Name"
                  />
                </div>
              </div>

              <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <label :for="`fieldType-${index}`" class="block text-sm font-medium text-gray-700 mb-1">
                    Field Type <span class="text-red-500">*</span>
                  </label>
                  <select
                    v-model="field.fieldType"
                    :id="`fieldType-${index}`"
                    required
                    class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                  >
                    <option value="">Select a type</option>
                    <option v-for="type in dataTypes" :key="type" :value="type">{{ type }}</option>
                  </select>
                </div>
                <div class="flex items-center">
                  <input
                    v-model="field.is_required"
                    type="checkbox"
                    :id="`isRequired-${index}`"
                    class="h-4 w-4 text-indigo-600 border-gray-300 rounded focus:ring-indigo-500"
                  />
                  <label :for="`isRequired-${index}`" class="ml-2 block text-sm text-gray-900">
                    Is Required
                  </label>
                </div>
              </div>

              <div>
                <label :for="`note-${index}`" class="block text-sm font-medium text-gray-700 mb-1">
                  Note (Optional)
                </label>
                <textarea
                  v-model="field.note"
                  :id="`note-${index}`"
                  rows="2"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                  placeholder="Any specific instructions or details for this field."
                ></textarea>
              </div>

              <!-- Conditional fields for FILE type -->
              <div v-if="field.fieldType === 'FILE'" class="space-y-4 border-t border-gray-200 pt-4 mt-4">
                <h4 class="text-md font-semibold text-gray-800">File Metadata</h4>
                <div class="flex items-center">
                  <input
                    v-model="field.hasCoordinate"
                    type="checkbox"
                    :id="`hasCoordinate-${index}`"
                    class="h-4 w-4 text-indigo-600 border-gray-300 rounded focus:ring-indigo-500"
                  />
                  <label :for="`hasCoordinate-${index}`" class="ml-2 block text-sm text-gray-900">
                    Requires Coordinate (e.g., for geo-tagging photos)
                  </label>
                </div>
                <div v-if="field.hasCoordinate">
                  <label :for="`coordinate-${index}`" class="block text-sm font-medium text-gray-700 mb-1">
                    Coordinate Field Name <span class="text-red-500">*</span>
                  </label>
                  <input
                    v-model="field.coordinate"
                    type="text"
                    :id="`coordinate-${index}`"
                    required
                    class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                    placeholder="e.g., photo_location_lat_lon"
                  />
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Submit Button -->
        <div class="flex justify-end space-x-4 pt-6">
          <button
            type="button"
            @click="resetForm"
            class="px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
          >
            Reset Form
          </button>
          <button
            type="submit"
            :disabled="processing"
            class="px-6 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-green-600 hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500 disabled:opacity-50"
          >
            <Loader2 v-if="processing" class="animate-spin h-4 w-4 mr-2" />
            {{ processing ? 'Saving...' : 'Create Application Type' }}
          </button>
        </div>
      </form>
    </div>
  </AuthenticatedLayout>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ArrowLeft, Plus, Trash2, Loader2, LayoutDashboard, FileText, Shield, Calculator, BarChart3 } from 'lucide-vue-next'
import AuthenticatedLayout from '../../../layouts/AuthenticatedLayout.vue'

const router = useRouter()

// Navigation for underwriter role (copied from UnderwriterDashboard for consistency)
const underwriterNavigation = [
  {
    name: 'Dashboard',
    href: '/underwriter/dashboard',
    icon: LayoutDashboard
  },
  {
    name: 'Applications',
    icon: FileText,
    children: [
      { name: 'New Applications', href: '/underwriter/applications/new' },
      { name: 'Pending Review', href: '/underwriter/applications/pending' },
      { name: 'Approved Applications', href: '/underwriter/applications/approved' },
      { name: 'Rejected Applications', href: '/underwriter/applications/rejected' }
    ]
  },
  {
    name: 'Risk Assessment',
    icon: Shield,
    children: [
      { name: 'Risk Factors', href: '/underwriter/risk/factors' },
      { name: 'Historical Data', href: '/underwriter/risk/history' },
      { name: 'Geographic Analysis', href: '/underwriter/risk/geo' }
    ]
  },
  {
    name: 'Guidelines & Tools',
    icon: Calculator,
    children: [
      { name: 'Eligibility Criteria', href: '/underwriter/guidelines/eligibility' },
      { name: 'Coverage & Premium', href: '/underwriter/guidelines/coverage' },
      { name: 'Underwriting Manual', href: '/underwriter/guidelines/manual' }
    ]
  },
  {
    name: 'Reports',
    href: '/underwriter/reports',
    icon: BarChart3
  }
]

// Data types for application fields
const dataTypes = [
  'TEXT',
  'NUMBER',
  'DATE',
  'BOOLEAN',
  'FILE',
  'ENUM', // Example: for dropdowns with predefined options
  'GEOLOCATION', // Example: for coordinates directly
]

const applicationType = ref({
  displayName: '',
  description: '',
  requiredAiAnalyses: false,
})

const insuranceFields = ref([])

const processing = ref(false)

const addInsuranceField = () => {
  insuranceFields.value.push({
    keyName: '',
    fieldType: '',
    displayName: '',
    note: '',
    is_required: false,
    hasCoordinate: false, // Specific to FILE type
    coordinate: '',       // Specific to FILE type
  })
}

const removeInsuranceField = (index) => {
  insuranceFields.value.splice(index, 1)
}

const submitApplication = async () => {
  processing.value = true
  
  // Construct the data payload based on DTOs
  const payload = {
    applicationType: {
      displayName: applicationType.value.displayName,
      description: applicationType.value.description,
      requiredAiAnalyses: applicationType.value.requiredAiAnalyses,
    },
    insuranceFields: insuranceFields.value.map(field => {
      const fieldDto = {
        keyName: field.keyName,
        fieldType: field.fieldType,
        displayName: field.displayName,
        note: field.note,
        is_required: field.is_required,
      }
      // Add file metadata if fieldType is FILE
      if (field.fieldType === 'FILE') {
        fieldDto.hasCoordinate = field.hasCoordinate;
        if (field.hasCoordinate) {
          fieldDto.coordinate = field.coordinate;
        }
      }
      return fieldDto
    }),
  }

  console.log('Submitting application:', JSON.stringify(payload, null, 2))

  try {
    // Simulate API call
    // In a real application, you would send this payload to your Spring Boot backend
    // await axios.post('/api/insurance-types', payload.applicationType);
    // Then, if successful, send fields:
    // await axios.post('/api/insurance-fields', payload.insuranceFields);
    await new Promise(resolve => setTimeout(resolve, 2000))
    
    alert('New application type created successfully!')
    router.push('/underwriter/dashboard') // Redirect back to dashboard
  } catch (error) {
    console.error('Error creating application type:', error)
    alert('Failed to create application type. Please check console for details.')
  } finally {
    processing.value = false
  }
}

const resetForm = () => {
  applicationType.value = {
    displayName: '',
    description: '',
    requiredAiAnalyses: false,
  }
  insuranceFields.value = []
}

// Add an initial field when the page loads
const addInitialField = () => {
  insuranceFields.value.push({
    keyName: '',
    fieldType: '',
    displayName: '',
    note: '',
    is_required: false,
    hasCoordinate: false, // Specific to FILE type
    coordinate: '',       // Specific to FILE type
  })
}

addInitialField()
</script>
