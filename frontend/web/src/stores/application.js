import { defineStore } from "pinia"
import axios from "@/lib/axios" // Ensure this path is correct for your axios instance

export const useApplicationStore = defineStore("application", {
  actions: {
    async createInsuranceApplication(applicationTypeData, insuranceFieldsData) {
      try {
        const payload = {
          displayName: applicationTypeData.displayName,
          description: applicationTypeData.description,
          requiredAiAnalyses: applicationTypeData.requiredAiAnalyses,
          fields: insuranceFieldsData.map((field) => {
            const fieldDto = {
              keyName: field.keyName,
              fieldType: field.fieldType,
              displayName: field.displayName,
              note: field.note,
              is_required: field.is_required,
            }
            // Add file metadata only if fieldType is 'FILE' and hasCoordinate is true
            if (field.fieldType === "FILE" && field.hasCoordinate) {
              fieldDto.hasCoordinate = field.hasCoordinate
              fieldDto.coordinate = field.coordinate
            } else if (field.fieldType === "FILE" && !field.hasCoordinate) {
              fieldDto.hasCoordinate = false // Explicitly set to false if not required
            }
            return fieldDto
          }),
        }

        console.log("Submitting application payload:", JSON.stringify(payload, null, 2))

        // Use the full URL as specified
        const response = await axios.post("/insurances", payload)

        console.log("Application created successfully:", response.data)
        return { success: true, data: response.data }
      } catch (error) {
        console.error("Error creating application type:", error.response?.data || error.message)
        return { success: false, error: error.response?.data || error.message }
      }
    },
  },
})
