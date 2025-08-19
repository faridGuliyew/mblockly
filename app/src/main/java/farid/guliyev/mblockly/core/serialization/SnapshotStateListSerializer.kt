package farid.guliyev.mblockly.core.serialization

import androidx.compose.runtime.snapshots.SnapshotStateList
import farid.guliyev.mblockly.ui.screens.builder_screen.InstructionBlock
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object SnapshotStateListSerializer :
    KSerializer<SnapshotStateList<InstructionBlock>> {

    private val delegate = ListSerializer(InstructionBlock.serializer())

    override val descriptor: SerialDescriptor = delegate.descriptor

    override fun serialize(encoder: Encoder, value: SnapshotStateList<InstructionBlock>) {
        delegate.serialize(encoder, value.toList())
    }

    override fun deserialize(decoder: Decoder): SnapshotStateList<InstructionBlock> {
        val list = delegate.deserialize(decoder)
        return SnapshotStateList<InstructionBlock>().apply { addAll(list) }
    }
}