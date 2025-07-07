import axios from '@/lib/axios'
import { defineStore, acceptHMRUpdate } from 'pinia'
import { useRouter } from 'vue-router'

export const useUserStore = defineStore('users', {
    state: () => ({
        userData: {},
    }),

    getters: {
        isAuthenticate: state => Object.values(state.userData).length > 0,
        hasVerified: state =>
            Object.keys(state.userData).length > 0
                ? state.userData.email_verified_at !== null
                : false,
        
        // User basic info
        userFullName: state => state.userData?.fullname || '',
        userEmail: state => state.userData?.email || '',
        userRole: state => state.userData?.role || null,
        userGender: state => state.userData?.gender || '',
        userContactNumber: state => state.userData?.contactNumber || '',
        userCivilStatus: state => state.userData?.civilStatus || '',
        userAddress: state => state.userData?.address || '',
        userPosition: state => state.userData?.position || '',
        userDepartment: state => state.userData?.department || '',
        userLocation: state => state.userData?.location || '',
        
        // Role-specific checks (updated without Roman numerals)
        isTeller: state => state.userData?.role === 'Teller',
        isInsuranceSpecialist: state => state.userData?.role === 'Insurance Specialist',
        isAdminOfficer: state => state.userData?.role === 'Administrative Services Officer',
        isUnderwriter: state => state.userData?.role === 'Insurance Underwriter',
        isDivisionChief: state => state.userData?.role === 'Division Chief',
        isClaimsProcessor: state => state.userData?.role === 'Claims Processor',
        isInsuranceAdjuster: state => state.userData?.role === 'Insurance Adjuster',
        
        // Permission levels (hierarchical)
        isManagementLevel: state => {
            const managementRoles = ['Division Chief']
            return managementRoles.includes(state.userData?.role)
        },
        
        isSupervisoryLevel: state => {
            const supervisoryRoles = ['Division Chief', 'Administrative Services Officer']
            return supervisoryRoles.includes(state.userData?.role)
        },
        
        isSpecialistLevel: state => {
            const specialistRoles = [
                'Insurance Specialist', 
                'Insurance Underwriter', 
                'Claims Processor', 
                'Insurance Adjuster'
            ]
            return specialistRoles.includes(state.userData?.role)
        },
        
        isOperationalLevel: state => {
            const operationalRoles = ['Teller']
            return operationalRoles.includes(state.userData?.role)
        },
        
        // Check if user is valid PCIC staff (updated roles)
        isValidStaff: state => {
            const validRoles = [
                'Teller',
                'Insurance Specialist',
                'Administrative Services Officer',
                'Insurance Underwriter',
                'Division Chief',
                'Claims Processor',
                'Insurance Adjuster'
            ]
            console.log('Checking role:', state.userData?.role, 'Valid roles:', validRoles)
            return validRoles.includes(state.userData?.role)
        },

        // Get user display info
        userDisplayInfo: state => {
            if (!state.userData || Object.keys(state.userData).length === 0) return null
            
            return {
                name: state.userData.fullname,
                email: state.userData.email,
                role: state.userData.role,
                position: state.userData.position,
                department: state.userData.department,
                location: state.userData.location,
                contact: state.userData.contactNumber,
                address: state.userData.address,
                gender: state.userData.gender,
                civilStatus: state.userData.civilStatus
            }
        },

        // Get role category for UI purposes
        roleCategory: state => {
            const role = state.userData?.role
            if (!role) return 'Unknown'
            
            if (role === 'Division Chief') return 'Management'
            if (role === 'Administrative Services Officer') return 'Administration'
            if (['Insurance Specialist', 'Insurance Underwriter', 'Claims Processor', 'Insurance Adjuster'].includes(role)) {
                return 'Insurance Operations'
            }
            if (role === 'Teller') return 'Financial Services'
            
            return 'Staff'
        }
    },

    actions: {
        async getData() {
            try {
                const response = await axios.get('/auth/user')
                this.userData = response.data
                
                console.log('User data received:', this.userData)
                
                // Validate that user has a valid PCIC role
                const validRoles = [
                    'Teller',
                    'Insurance Specialist',
                    'Administrative Services Officer',
                    'Insurance Underwriter',
                    'Division Chief',
                    'Claims Processor',
                    'Insurance Adjuster'
                ]
                
                if (!validRoles.includes(this.userData?.role)) {
                    console.error('Invalid role detected:', this.userData?.role)
                    throw new Error('Access denied: Valid PCIC staff role required')
                }
                
                console.log('Role validation passed for:', this.userData?.role)
                
            } catch (error) {
                if (error.response?.status === 409) {
                    this.router.push('/verify-email')
                } else {
                    console.error('getData error:', error)
                    throw error
                }
            }
        },

        async login(form, setErrors, processing) {
            try {
                processing.value = true
                
                // First login
                await axios.post('/login', form.value);
                
                // Then fetch the authenticated user
                const response = await axios.get('/auth/user');
                
                if(response.status == 200 || response.status == 204){
                    this.userData = response.data;
                    
                    console.log('Login successful, user data:', this.userData)
                    
                    // Validate PCIC staff access
                    const validRoles = [
                        'Teller',
                        'Insurance Specialist',
                        'Administrative Services Officer',
                        'Insurance Underwriter',
                        'Division Chief',
                        'Claims Processor',
                        'Insurance Adjuster'
                    ]
                    
                    if (!validRoles.includes(this.userData?.role)) {
                        console.error('Access denied for role:', this.userData?.role)
                        setErrors.value = ['Access denied: This portal is for PCIC staff members only.']
                        await this.logout()
                        return
                    }
                    
                    console.log('Access granted for role:', this.userData?.role)
                    
                    // Role-based redirect
                    const redirectPath = this.getRedirectPath()
                    console.log('Redirecting to:', redirectPath)
                    this.router.push(redirectPath)
                }
                
            } catch (error) {
                console.error('Login error:', error)
                if(error.response && error.response.status === 422){
                    setErrors.value = Object.values(error.response.data.errors).flat()
                } else if(error.response && error.response.status === 401) {
                    setErrors.value = ['Invalid username or password.']
                } else {
                    setErrors.value = ['Login failed. Please try again.']
                }
            } finally {
                processing.value = false
            }
        },

        // Get redirect path based on user role (updated without Roman numerals)
        getRedirectPath() {
            const role = this.userData?.role

            switch(role) {
                case 'Division Chief':
                    return { name: 'division-chief-dashboard' }
                case 'Administrative Services Officer':
                    return { name: 'admin-officer-dashboard' }
                case 'Insurance Underwriter':
                    return { name: 'underwriter-dashboard' }
                case 'Insurance Specialist':
                    return { name: 'insurance-specialist-dashboard' }
                case 'Claims Processor':
                    return { name: 'claims-processor-dashboard' }
                case 'Insurance Adjuster':
                    return { name: 'insurance-adjuster-dashboard' }
                case 'Teller':
                    return { name: 'teller-dashboard' }
                default:
                    return { name: 'staff-dashboard' }
            }
        },

        // Check if user has specific role
        hasRole(role) {
            return this.userData?.role === role
        },

        // Check if user has any of the specified roles
        hasAnyRole(roles) {
            return Array.isArray(roles) && 
                   roles.includes(this.userData?.role)
        },

        // Check if user has permission level (hierarchical) - updated roles
        hasPermissionLevel(level) {
            const hierarchy = {
                'Division Chief': 5,                        // Management
                'Administrative Services Officer': 4,       // Administration
                'Insurance Underwriter': 3,                 // Senior Specialist
                'Insurance Specialist': 3,                  // Senior Specialist
                'Claims Processor': 2,                      // Specialist
                'Insurance Adjuster': 2,                    // Specialist
                'Teller': 1                                 // Operational
            }
            
            const userLevel = hierarchy[this.userData?.role] || 0
            const requiredLevel = hierarchy[level] || 0
            
            return userLevel >= requiredLevel
        },

        // Check if user can access specific features (updated roles)
        canAccess(feature) {
            const permissions = {
                // Management features
                'staff-management': ['Division Chief'],
                'system-administration': ['Division Chief', 'Administrative Services Officer'],
                'financial-reports': ['Division Chief', 'Administrative Services Officer'],
                
                // Insurance operations
                'policy-underwriting': ['Division Chief', 'Insurance Underwriter'],
                'risk-assessment': ['Division Chief', 'Insurance Underwriter', 'Insurance Specialist'],
                'policy-issuance': ['Division Chief', 'Insurance Underwriter', 'Insurance Specialist'],
                
                // Claims management
                'claims-processing': ['Division Chief', 'Claims Processor', 'Insurance Adjuster'],
                'claims-investigation': ['Division Chief', 'Insurance Adjuster'],
                'claims-approval': ['Division Chief', 'Insurance Adjuster'],
                
                // Financial operations
                'payment-processing': ['Division Chief', 'Administrative Services Officer', 'Teller'],
                'transaction-management': ['Division Chief', 'Administrative Services Officer', 'Teller'],
                
                // General access
                'view-policies': ['Division Chief', 'Administrative Services Officer', 'Insurance Underwriter', 'Insurance Specialist', 'Claims Processor', 'Insurance Adjuster'],
                'view-claims': ['Division Chief', 'Administrative Services Officer', 'Claims Processor', 'Insurance Adjuster'],
                'basic-reports': ['Division Chief', 'Administrative Services Officer', 'Insurance Underwriter', 'Insurance Specialist', 'Claims Processor', 'Insurance Adjuster', 'Teller']
            }
            
            const allowedRoles = permissions[feature] || []
            return allowedRoles.includes(this.userData?.role)
        },

        // Get user's primary responsibilities (updated roles)
        getPrimaryResponsibilities() {
            const role = this.userData?.role
            const responsibilities = {
                'Teller': [
                    'Handle financial transactions',
                    'Process payments and receipts',
                    'Maintain transaction records',
                    'Customer service for financial matters'
                ],
                'Insurance Specialist': [
                    'Policy issuance and management',
                    'Customer consultation on insurance products',
                    'Process insurance applications',
                    'Specialized insurance-related tasks'
                ],
                'Administrative Services Officer': [
                    'Personnel management',
                    'Records management',
                    'Procurement oversight',
                    'Administrative coordination'
                ],
                'Insurance Underwriter': [
                    'Risk assessment and analysis',
                    'Determine coverage and premiums',
                    'Review and approve policies',
                    'Underwriting guidelines compliance'
                ],
                'Division Chief': [
                    'Division leadership and management',
                    'Strategic planning and oversight',
                    'Staff supervision and development',
                    'Performance monitoring and reporting'
                ],
                'Claims Processor': [
                    'Process insurance claims',
                    'Verify claim documentation',
                    'Calculate claim settlements',
                    'Maintain claims database'
                ],
                'Insurance Adjuster': [
                    'Investigate insurance claims',
                    'Assess damages and losses',
                    'Determine claim validity',
                    'Prepare adjustment reports'
                ]
            }
            
            return responsibilities[role] || []
        },

        async forgotPassword(form, setStatus, setErrors, processing) {
            processing.value = true

            axios
                .post('/forgot-password', form.value)
                .then(response => {
                    setStatus.value = response.data.status
                    processing.value = false
                })
                .catch(error => {
                    if (error.response.status !== 422) throw error

                    setErrors.value = Object.values(
                        error.response.data.errors,
                    ).flat()
                    processing.value = false
                })
        },

        async resetPassword(form, setErrors, processing) {
            processing.value = true

            axios
                .post('/reset-password', form.value)
                .then(response => {
                    this.router.push(
                        '/login?reset=' + btoa(response.data.status),
                    )
                    processing.value = false
                })
                .catch(error => {
                    if (error.response.status !== 422) throw error

                    setErrors.value = Object.values(
                        error.response.data.errors,
                    ).flat()
                    processing.value = false
                })
        },

        resendEmailVerification(setStatus, processing) {
            processing.value = true

            axios.post('/email/verification-notification').then(response => {
                setStatus.value = response.data.status
                processing.value = false
            })
        },

        async logout() {
            await axios
                .post('/logout')
                .then(() => {
                    this.$reset()
                    this.router.push({ name: 'login' })
                })
                .catch(error => {
                    if (error.response.status !== 422) throw error
                })
        },
    },
})

if (import.meta.hot) {
    import.meta.hot.accept(acceptHMRUpdate(useUserStore, import.meta.hot))
}