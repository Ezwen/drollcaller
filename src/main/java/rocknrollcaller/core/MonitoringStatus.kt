package rocknrollcaller.core

enum class MonitoringStatus(private val label: String) {
    NO_PROBLEMS("✅️ NO PROBLEM"),
    NEW_PROBLEMS("⚠️ NEW PROBLEM(S)"),
    SAME_PROBLEMS("⚠️ SAME PROBLEM(S)"),
    PROBLEMS_CHANGED(
        "⚠️ DIFFERENT PROBLEM(S)"
    ),
    PROBLEMS_SOLVED("🎉 PROBLEM(S) SOLVED");

    override fun toString(): String {
        return label
    }
}