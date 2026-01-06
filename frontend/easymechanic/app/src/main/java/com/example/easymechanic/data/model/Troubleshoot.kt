package com.example.easymechanic.data.model

import com.google.gson.annotations.SerializedName

data class TroubleshootRequest(
    @SerializedName("problem_description") val problemDescription: String
)

data class TroubleshootResponse(
    @SerializedName("issue_type") val issueType: String? = null,
    @SerializedName("possible_causes") val possibleCauses: List<String>? = null,
    @SerializedName("step_by_step_solution") val stepByStepSolution: List<String>? = null,
    @SerializedName("safety_note") val safetyNote: String? = null,
    // Enhanced AI fields
    @SerializedName("greeting") val greeting: String? = null,
    @SerializedName("urgency_level") val urgencyLevel: String? = null,
    @SerializedName("analysis") val analysis: Analysis? = null,
    @SerializedName("next_steps") val nextSteps: List<String>? = null,
    @SerializedName("suggested_questions") val suggestedQuestions: List<String>? = null,
    @SerializedName("ai_powered") val aiPowered: Boolean = false
)

data class Analysis(
    @SerializedName("detected_category") val detectedCategory: String? = null,
    @SerializedName("requires_immediate_attention") val requiresImmediateAttention: Boolean = false,
    @SerializedName("safety_critical") val safetyCritical: Boolean = false
)

