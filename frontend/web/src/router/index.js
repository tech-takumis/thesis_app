import { createWebHistory, createRouter } from "vue-router"
import { useUserStore } from "@/stores/user"

const APP_NAME = import.meta.env.VITE_APP_NAME

const routes = [
  // Admin Dashboard
  {
    path: "/admin/dashboard",
    name: "admin-dashboard",
    component: () => import("@/pages/admin/AdminDashboard.vue"),
    meta: {
      title: "Admin Dashboard",
      guard: "auth",
      roles: ["Admin"],
    },
  },

  // Insurance Underwriter Dashboard
  {
    path: "/underwriter/dashboard",
    name: "underwriter-dashboard",
    component: () => import("@/pages/underwriter/UnderwriterDashboard.vue"),
    meta: {
      title: "Insurance Underwriter Dashboard",
      guard: "auth",
      roles: ["Underwriter"],
    },
    
  },
  // New Application Page for Underwriter
  {
    path: "/underwriter/applications/new",
    name: "underwriter-new-application",
    component: () => import("@/pages/underwriter/applications/NewApplication.vue"),
    meta: {
      title: "Create New Application Type",
      guard: "auth",
      roles: ["Underwriter", "Admin"], // Admin can also create application types
    },
  },

  // Claims Processor Dashboard
  {
    path: "/claims-processor/dashboard",
    name: "claims-processor-dashboard",
    component: () => import("@/pages/claims-processor/ClaimsProcessorDashboard.vue"),
    meta: {
      title: "Claims Processor Dashboard",
      guard: "auth",
      roles: ["Claims_Processor"],
    },
  },

  // Teller Dashboard
  {
    path: "/teller/dashboard",
    name: "teller-dashboard",
    component: () => import("@/pages/teller/TellerDashboard.vue"),
    meta: {
      title: "Teller Dashboard",
      guard: "auth",
      roles: ["Teller"],
    },
  },

  // Multi-Role Dashboard (for users with multiple roles)
  {
    path: "/multi-role/dashboard",
    name: "multi-role-dashboard",
    component: () => import("@/pages/multi-role/MultiRoleDashboard.vue"),
    meta: {
      title: "Dashboard",
      guard: "auth",
      // No specific roles - accessible to any authenticated user
    },
  },

  
  // Legacy Dashboard (redirects to appropriate dashboard)
  {
    path: "/dashboard",
    name: "dashboard",
    redirect: (to) => {
      const store = useUserStore()
      return store.getRedirectPath()
    },
    meta: {
      title: "Dashboard",
      guard: "auth",
    },
  },

  // Login
  {
    path: "/",
    name: "login",
    component: () => import("@/pages/auth/Login.vue"),
    query: {
      reset: "reset",
    },
    meta: {
      title: "PCIC Staff Login",
      guard: "guest",
    },
  },

  // Error Pages
  {
    path: "/access-denied",
    name: "access-denied",
    component: () => import("@/pages/errors/AccessDenied.vue"),
    meta: {
      title: "Access Denied",
    },
  },
  {
    path: "/page-not-found",
    name: "page-not-found",
    component: () => import("@/pages/errors/404.vue"),
    meta: {
      title: "Page Not Found",
    },
  },
  {
    path: "/:pathMatch(.*)*",
    redirect: "/page-not-found",
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

// Main navigation guard
router.beforeEach(async (to, from, next) => {
  const store = useUserStore()

  const requiresAuth = to.matched.some((route) => route.meta.guard === "auth")
  const requiresGuest = to.matched.some((route) => route.meta.guard === "guest")
  const requiredRoles = to.meta.roles

  console.log(
    "Router guard - Route:",
    to.name,
    "Requires auth:",
    requiresAuth,
    "User authenticated:",
    store.isAuthenticate,
  )

  // Only fetch user data if route requires auth and user is not authenticated
  if (requiresAuth && !store.isAuthenticate) {
    try {
      console.log("Fetching user data...")
      await store.getData()
      console.log("User data fetched successfully")
    } catch (error) {
      console.error("Auth check failed:", error)
      return next({ name: "login" })
    }
  }

  // Check authentication and role-based access
  if (requiresAuth) {
    if (!store.isAuthenticate) {
      console.log("User not authenticated, redirecting to login")
      return next({ name: "login" })
    }

    // Validate PCIC staff access
    if (!store.isValidStaff) {
      console.error("User is not valid staff:", store.userData?.roles)
      return next({ name: "access-denied" })
    }

    console.log("User is valid staff with roles:", store.userData?.roles)

    // Check role-based access (user must have at least one of the required roles)
    if (requiredRoles && requiredRoles.length > 0) {
      const userRoles = store.userData?.roles || []

      console.log("Checking role access - User roles:", userRoles, "Required roles:", requiredRoles)

      const hasRequiredRole = requiredRoles.some((role) => userRoles.includes(role))

      if (!hasRequiredRole) {
        console.log("User roles not allowed for this route, redirecting to appropriate dashboard")
        // Redirect to appropriate dashboard based on role
        return next(store.getRedirectPath())
      }
    }

    // If user is authenticated and trying to access a general dashboard,
    // redirect to their role-specific dashboard
    if (to.name === "dashboard" || to.name === "staff-dashboard") {
      const redirectPath = store.getRedirectPath()
      if (redirectPath.name !== to.name) {
        console.log("Redirecting to role-specific dashboard:", redirectPath)
        return next(redirectPath)
      }
    }
  }

  // Guest routes (login, register, etc.)
  if (requiresGuest && store.isAuthenticate) {
    console.log("Authenticated user trying to access guest route, redirecting to dashboard")
    return next(store.getRedirectPath())
  }

  console.log("Navigation allowed to:", to.name)
  next()
})

// Page Title and Metadata
router.beforeEach((to, from, next) => {
  const nearestWithTitle = to.matched
    .slice()
    .reverse()
    .find((r) => r.meta && r.meta.title)

  const nearestWithMeta = to.matched
    .slice()
    .reverse()
    .find((r) => r.meta && r.meta.metaTags)

  if (nearestWithTitle) {
    document.title = nearestWithTitle.meta.title + " - " + APP_NAME
  } else {
    document.title = APP_NAME
  }

  Array.from(document.querySelectorAll("[data-vue-router-controlled]")).map((el) => el.parentNode.removeChild(el))

  if (!nearestWithMeta) return next()

  nearestWithMeta.meta.metaTags
    .map((tagDef) => {
      const tag = document.createElement("meta")

      Object.keys(tagDef).forEach((key) => {
        tag.setAttribute(key, tagDef[key])
      })

      tag.setAttribute("data-vue-router-controlled", "")

      return tag
    })
    .forEach((tag) => document.head.appendChild(tag))

  next()
})

export default router
