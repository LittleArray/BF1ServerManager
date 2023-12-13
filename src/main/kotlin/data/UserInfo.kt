package top.ffshaozi.data

data class UserInfo(
    val personas: Personas?=null
){
    data class Persona(
        val dateCreated: String,
        val displayName: String,
        val isVisible: Boolean,
        val lastAuthenticated: String,
        val name: String,
        val namespaceName: String,
        val personaId: Long,
        val pidId: Long,
        val showPersona: String,
        val status: String,
        val statusReasonCode: String
    )
    data class Personas(
        val persona: List<Persona>?=null
    )
}