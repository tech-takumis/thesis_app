import { LayoutDashboard, FileText, Shield, Calculator, BarChart3 } from "lucide-vue-next"

// Navigation for underwriter role
export const UNDERWRITER_NAVIGATION = [
  {
    name: "Dashboard",
    href: "/underwriter/dashboard",
    icon: LayoutDashboard,
  },
  {
    name: "Applications",
    icon: FileText,
    children: [
      { name: "New Applications", href: "/underwriter/applications/new" },
      { name: "Pending Review", href: "/underwriter/applications/pending" },
      { name: "Approved Applications", href: "/underwriter/applications/approved" },
      { name: "Rejected Applications", href: "/underwriter/applications/rejected" },
    ],
  },
  {
    name: "Risk Assessment",
    icon: Shield,
    children: [
      { name: "Risk Factors", href: "/underwriter/risk/factors" },
      { name: "Historical Data", href: "/underwriter/risk/history" },
      { name: "Geographic Analysis", href: "/underwriter/risk/geo" },
    ],
  },
  {
    name: "Guidelines & Tools",
    icon: Calculator,
    children: [
      { name: "Eligibility Criteria", href: "/underwriter/guidelines/eligibility" },
      { name: "Coverage & Premium", href: "/underwriter/guidelines/coverage" },
      { name: "Underwriting Manual", href: "/underwriter/guidelines/manual" },
    ],
  },
  {
    name: "Reports",
    href: "/underwriter/reports",
    icon: BarChart3,
  },
]

// Data types for application fields
export const DATA_TYPES = ["Text", "Number", "Boolean", "Timestamp", "File"]
