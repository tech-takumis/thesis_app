import axios from "@/lib/axios"
import { defineStore, acceptHMRUpdate } from "pinia"

export const useUserStore = defineStore("users", {
  state: () => ({
    userData: {},
  }),

  getters: {
    isAuthenticate: (state) => Object.values(state.userData).length > 0,
    hasVerified: (state) =>
      Object.keys(state.userData).length > 0 ? state.userData.email_verified_at !== null : false,

    // User basic info
    userFullName: (state) => state.userData?.fullname || "",
    userEmail: (state) => state.userData?.email || "",
    userRoles: (state) => state.userData?.roles || [], // Now returns array of roles
    userPrimaryRole: (state) => {
      // Get the highest priority role for display purposes
      const roles = state.userData?.roles || []
      const rolePriority = {
        Admin: 4,
        Underwriter: 3,
        Claims_Processor: 2,
        Teller: 1,
      }

      return roles.reduce((highest, role) => {
        const currentPriority = rolePriority[role] || 0
        const highestPriority = rolePriority[highest] || 0
        return currentPriority > highestPriority ? role : highest
      }, roles[0] || null)
    },
    userGender: (state) => state.userData?.gender || "",
    userContactNumber: (state) => state.userData?.contactNumber || "",
    userCivilStatus: (state) => state.userData?.civilStatus || "",
    userAddress: (state) => state.userData?.address || "",
    userPosition: (state) => state.userData?.position || "",
    userDepartment: (state) => state.userData?.department || "",
    userLocation: (state) => state.userData?.location || "",

    // Role-specific checks
    isTeller: (state) => (state.userData?.roles || []).includes("Teller"),
    isAdmin: (state) => (state.userData?.roles || []).includes("Admin"),
    isUnderwriter: (state) => (state.userData?.roles || []).includes("Underwriter"),
    isClaimsProcessor: (state) => (state.userData?.roles || []).includes("Claims_Processor"),

    // Permission levels (hierarchical) - checks if user has any role at this level
    isManagementLevel: (state) => {
      const managementRoles = ["Admin"]
      const userRoles = state.userData?.roles || []
      return managementRoles.some((role) => userRoles.includes(role))
    },

    isSupervisoryLevel: (state) => {
      const supervisoryRoles = ["Admin", "Underwriter"]
      const userRoles = state.userData?.roles || []
      return supervisoryRoles.some((role) => userRoles.includes(role))
    },

    isSpecialistLevel: (state) => {
      // Renamed from previous 'isSpecialistLevel' to reflect remaining roles
      const specialistRoles = ["Underwriter", "Claims_Processor"]
      const userRoles = state.userData?.roles || []
      return specialistRoles.some((role) => userRoles.includes(role))
    },

    isOperationalLevel: (state) => {
      const operationalRoles = ["Teller"]
      const userRoles = state.userData?.roles || []
      return operationalRoles.some((role) => userRoles.includes(role))
    },

    // Check if user is valid PCIC staff (has at least one valid role)
    isValidStaff: (state) => {
      const validRoles = ["Teller", "Admin", "Underwriter", "Claims_Processor"]
      const userRoles = state.userData?.roles || []
      console.log("Checking roles:", userRoles, "Valid roles:", validRoles)
      return userRoles.some((role) => validRoles.includes(role))
    },

    // Get user display info
    userDisplayInfo: (state) => {
      if (!state.userData || Object.keys(state.userData).length === 0) return null

      return {
        name: state.userData.fullname,
        email: state.userData.email,
        roles: state.userData.roles,
        primaryRole: state.userData.roles?.[0] || null,
        position: state.userData.position,
        department: state.userData.department,
        location: state.userData.location,
        contact: state.userData.contactNumber,
        address: state.userData.address,
        gender: state.userData.gender,
        civilStatus: state.userData.civilStatus,
      }
    },

    // Get role category for UI purposes (based on primary role)
    roleCategory: (state) => {
      const primaryRole = state.userData?.roles?.[0]
      if (!primaryRole) return "Unknown"

      if (["Admin"].includes(primaryRole)) return "Management"
      if (["Underwriter", "Claims_Processor"].includes(primaryRole)) {
        return "Insurance Operations"
      }
      if (primaryRole === "Teller") return "Financial Services"

      return "Staff"
    },

    // Get all role categories user belongs to
    allRoleCategories: (state) => {
      const roles = state.userData?.roles || []
      const categories = new Set()

      roles.forEach((role) => {
        if (["Admin"].includes(role)) categories.add("Management")
        if (["Underwriter", "Claims_Processor"].includes(role)) {
          categories.add("Insurance Operations")
        }
        if (role === "Teller") categories.add("Financial Services")
      })

      return Array.from(categories)
    },
  },

  actions: {
    async getData() {
      try {
        const response = await axios.get("/auth/users")
        this.userData = response.data

        console.log("User data received:", this.userData)

        // Validate that user has at least one valid PCIC role
        const validRoles = ["Teller", "Admin", "Underwriter", "Claims_Processor"]

        const userRoles = this.userData?.roles || []
        const hasValidRole = userRoles.some((role) => validRoles.includes(role))

        if (!hasValidRole) {
          console.error("No valid roles detected:", userRoles)
          throw new Error("Access denied: Valid PCIC staff role required")
        }

        console.log("Role validation passed for roles:", userRoles)
      } catch (error) {
        if (error.response?.status === 409) {
          this.router.push("/verify-email")
        } else {
          console.error("getData error:", error)
          throw error
        }
      }
    },

    async login(form, setErrors, processing) {
      try {
        processing.value = true

        // First login
        await axios.post("/web/login", form.value)

        // Then fetch the authenticated user
        const response = await axios.get("/auth/users")

        if (response.status == 200 || response.status == 204) {
          this.userData = response.data

          console.log("Login successful, user data:", this.userData)

          // Validate PCIC staff access (check if user has at least one valid role)
          const validRoles = ["Teller", "Admin", "Underwriter", "Claims_Processor"]

          const userRoles = this.userData?.roles || []
          const hasValidRole = userRoles.some((role) => validRoles.includes(role))

          if (!hasValidRole) {
            console.error("Access denied for roles:", userRoles)
            setErrors.value = ["Access denied: This portal is for PCIC staff members only."]
            await this.logout()
            return
          }

          console.log("Access granted for roles:", userRoles)

          // Role-based redirect
          const redirectPath = this.getRedirectPath()
          console.log("Redirecting to:", redirectPath)
          this.router.push(redirectPath)
        }
      } catch (error) {
        console.error("Login error:", error)
        if (error.response && error.response.status === 422) {
          setErrors.value = Object.values(error.response.data.errors).flat()
        } else if (error.response && error.response.status === 401) {
          setErrors.value = ["Invalid username or password."]
        } else {
          setErrors.value = ["Login failed. Please try again."]
        }
      } finally {
        processing.value = false
      }
    },

    // Get redirect path based on user's highest priority role
    getRedirectPath() {
      const roles = this.userData?.roles || []

      // Priority order for redirect (highest to lowest)
      const rolePriority = ["Admin", "Underwriter", "Claims_Processor", "Teller"]

      // Find the highest priority role the user has
      for (const role of rolePriority) {
        if (roles.includes(role)) {
          switch (role) {
            case "Admin":
              return { name: "admin-dashboard" }
            case "Underwriter":
              return { name: "underwriter-dashboard" }
            case "Claims_Processor":
              return { name: "claims-processor-dashboard" }
            case "Teller":
              return { name: "teller-dashboard" }
          }
        }
      }

      // Fallback to staff dashboard (or login if no valid role)
      return { name: "login" } // Changed fallback to login if no valid role is found
    },

    // Check if user has specific role
    hasRole(role) {
      const userRoles = this.userData?.roles || []
      return userRoles.includes(role)
    },

    // Check if user has any of the specified roles
    hasAnyRole(roles) {
      if (!Array.isArray(roles)) return false
      const userRoles = this.userData?.roles || []
      return roles.some((role) => userRoles.includes(role))
    },

    // Check if user has all of the specified roles
    hasAllRoles(roles) {
      if (!Array.isArray(roles)) return false
      const userRoles = this.userData?.roles || []
      return roles.every((role) => userRoles.includes(role))
    },

    // Check if user has permission level (hierarchical)
    hasPermissionLevel(level) {
      const hierarchy = {
        Admin: 4,
        Underwriter: 3,
        Claims_Processor: 2,
        Teller: 1,
      }

      const userRoles = this.userData?.roles || []
      const userMaxLevel = Math.max(...userRoles.map((role) => hierarchy[role] || 0))
      const requiredLevel = hierarchy[level] || 0

      return userMaxLevel >= requiredLevel
    },

    // Check if user can access specific features
    canAccess(feature) {
      const permissions = {
        // Management features
        "staff-management": ["Admin"],
        "system-administration": ["Admin"],
        "financial-reports": ["Admin"],

        // Insurance operations
        "policy-underwriting": ["Admin", "Underwriter"],
        "risk-assessment": ["Admin", "Underwriter"],
        "policy-issuance": ["Admin", "Underwriter"],

        // Claims management
        "claims-processing": ["Admin", "Claims_Processor"],
        "claims-investigation": ["Admin", "Claims_Processor"],
        "claims-approval": ["Admin", "Claims_Processor"],

        // Financial operations
        "payment-processing": ["Admin", "Teller"],
        "transaction-management": ["Admin", "Teller"],

        // General access
        "view-policies": ["Admin", "Underwriter", "Claims_Processor"],
        "view-claims": ["Admin", "Claims_Processor"],
        "basic-reports": ["Admin", "Underwriter", "Claims_Processor", "Teller"],
      }

      const allowedRoles = permissions[feature] || []
      return this.hasAnyRole(allowedRoles)
    },

    // Get user's combined responsibilities from all roles
    getAllResponsibilities() {
      const roles = this.userData?.roles || []
      const allResponsibilities = new Set()

      const responsibilities = {
        Teller: [
          "Handle financial transactions",
          "Process payments and receipts",
          "Maintain transaction records",
          "Customer service for financial matters",
        ],
        Admin: [
          "System administration",
          "User management",
          "Administrative oversight",
          "Policy and procedure management",
        ],
        Underwriter: [
          "Risk assessment and analysis",
          "Determine coverage and premiums",
          "Review and approve policies",
          "Underwriting guidelines compliance",
        ],
        Claims_Processor: [
          "Process insurance claims",
          "Verify claim documentation",
          "Calculate claim settlements",
          "Maintain claims database",
        ],
      }

      roles.forEach((role) => {
        const roleResponsibilities = responsibilities[role] || []
        roleResponsibilities.forEach((resp) => allResponsibilities.add(resp))
      })

      return Array.from(allResponsibilities)
    },

    async registerStaff(staffData) {
      try {
        console.log("Registering staff with data:", staffData)
        const response = await axios.post("/staffs", staffData) // Assuming this endpoint
        console.log("Staff registered successfully:", response.data)
        return { success: true, data: response.data }
      } catch (error) {
        console.error("Error registering staff:", error.response?.data || error.message)
        return { success: false, error: error.response?.data || error.message }
      }
    },

    async forgotPassword(form, setStatus, setErrors, processing) {
      processing.value = true

      axios
        .post("/forgot-password", form.value)
        .then((response) => {
          setStatus.value = response.data.status
          processing.value = false
        })
        .catch((error) => {
          if (error.response.status !== 422) throw error

          setErrors.value = Object.values(error.response.data.errors).flat()
          processing.value = false
        })
    },

    async resetPassword(form, setErrors, processing) {
      processing.value = true

      axios
        .post("/reset-password", form.value)
        .then((response) => {
          this.router.push("/login?reset=" + btoa(response.data.status))
          processing.value = false
        })
        .catch((error) => {
          if (error.response.status !== 422) throw error

          setErrors.value = Object.values(error.response.data.errors).flat()
          processing.value = false
        })
    },

    resendEmailVerification(setStatus, processing) {
      processing.value = true

      axios.post("/email/verification-notification").then((response) => {
        setStatus.value = response.data.status
        processing.value = false
      })
    },

    async logout() {
      await axios
        .post("/logout")
        .then(() => {
          this.$reset()
          this.router.push({ name: "login" })
        })
        .catch((error) => {
          if (error.response.status !== 422) throw error
        })
    },
  },
})

if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useUserStore, import.meta.hot))
}
