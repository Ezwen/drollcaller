package core

enum class MonitoringStatus(private val label: String) {
    NO_PROBLEMS("AUCUN PROBLÈME"),
    NEW_PROBLEMS("⚠ NOUVEAU(X) PROBLÈME(S)"),
    SAME_PROBLEMS("⚠ MÊME(S) PROBLÈME(S)"),
    PROBLEMS_CHANGED(
        "⚠ PROBLÈME(S) DIFFÉRENT(S)"
    ),
    PROBLEMS_SOLVED("🎉 PROBLÈME(S) RÉSOLU(S)");

    override fun toString(): String {
        return label
    }
}