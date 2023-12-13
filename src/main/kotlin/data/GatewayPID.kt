package data

data class GatewayPID(
    val personas: Personas ? = null
){
    data class Persona(
        val dateCreated: String ? = null,
        val displayName: String ? = null,
        val isVisible: Boolean ? = null,
        val lastAuthenticated: String ? = null,
        val name: String ? = null,
        val namespaceName: String ? = null,
        val personaId: Long ? = null,
        val pidId: Long ? = null,
        val showPersona: String ? = null,
        val status: String ? = null,
        val statusReasonCode: String ? = null
    )
    data class Personas(
        val persona: List<Persona> ? = null
    )
}