package farid.guliyev.mblockly.core.serialization

import farid.guliyev.mblockly.domain.model.instruction.Instruction
import farid.guliyev.mblockly.domain.model.instruction.InstructionRuntime
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement

// Serializer for InstructionRuntime
object InstructionRuntimeSerializer : KSerializer<InstructionRuntime> {
    override val descriptor = Instruction.serializer().descriptor

    override fun serialize(encoder: Encoder, value: InstructionRuntime) {
        // serialize the underlying Instruction (base)
        val element: JsonElement = Json.encodeToJsonElement(value.base)
        encoder.encodeSerializableValue(JsonElement.serializer(), element)
    }

    override fun deserialize(decoder: Decoder): InstructionRuntime {
        // decode JSON into Instruction first
        val element = decoder.decodeSerializableValue(JsonElement.serializer())
        val base = Json.decodeFromJsonElement(Instruction.serializer(), element)

        // wrap back into the correct runtime
        return base.buildRuntime()
    }
}